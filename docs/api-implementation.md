# API Implementation Guide (Ktor)

> **Scope.** This document specifies the full backend API surface FitWithAI needs and how to
> implement the client for it with **Ktor 3.5.0** inside the KMP module graph. It is the
> authoritative reference for building out `:core:network` and the per-vertical remote data
> sources in `:data`. Everything here is Android + iOS safe (nothing lives in `:app`).
>
> **Current state.** Networking today is scaffolding only: `:core:network` ships a bare
> `createHttpClient()` JSON factory, the `:app` `ApiService`/`RetrofitBuilder`/`ApiResponseHandler`
> classes are empty placeholders (Retrofit is being dropped in favour of Ktor), and every remote
> data source (`WorkoutRemoteDataSource`, `AuthRemoteDataSource`, `ProfileDataSource`,
> `ProgressDataSource`, the AI repos) is a stub returning empty data. This guide turns that into a
> real, offline-first, multiplatform networking layer.

---

## 1. Design conventions

These apply to **every** endpoint below. Implement them once in the shared client so no call site repeats them.

| Concern | Convention |
|---|---|
| **Base URL** | `https://api.fitwithai.app` — injected as `HttpClientConfig.baseUrl`, never hard-coded per call. |
| **Versioning** | Path-prefixed: `/v1/...`. Breaking changes bump to `/v2`. |
| **Transport** | HTTPS only. JSON request/response (`application/json; charset=utf-8`). |
| **Auth** | Bearer JWT in `Authorization: Bearer <accessToken>`. Access token short-lived; refresh token used to mint new ones (§3.2). |
| **IDs** | Server-generated string UUIDs. Client may send a `clientId` (UUID) for idempotent create/sync. |
| **Timestamps** | Epoch **milliseconds** (`Long`) on the wire — matches `Workout.performedAtEpochMs`. |
| **Pagination** | Cursor-based: `?limit=50&cursor=<opaque>`. Response returns `nextCursor` (null = end). |
| **Errors** | Uniform envelope (§1.2) with stable machine `code`. |
| **Idempotency** | Mutating calls accept `Idempotency-Key` header (client UUID) so retries don't double-write. |
| **Sync** | List endpoints accept `?updatedSince=<epochMs>` for delta sync (offline-first, §6). |

### 1.1 Standard response envelope

Successful responses return the resource (or a paginated list) directly:

```json
// GET /v1/workouts
{
  "data": [ { "id": "...", "name": "Push Day", "durationMinutes": 45, "calories": 380, "performedAtEpochMs": 1725148800000 } ],
  "nextCursor": "eyJvZmZzZXQiOjUwfQ=="
}
```

### 1.2 Standard error envelope

```json
{
  "error": {
    "code": "WORKOUT_NOT_FOUND",
    "message": "No workout exists with id 9f3c…",
    "details": { "id": "9f3c…" }
  }
}
```

`code` is a stable `SCREAMING_SNAKE_CASE` string the client switches on. HTTP status conveys class
(400/401/403/404/409/422/429/5xx); `code` conveys the specific reason. Client maps these to
`NetworkError` (§5).

---

## 2. API surface by vertical

Every vertical the app ships (auth, dashboard, workout, AI coach, heart, sleep, steps, progress,
nutrition, profile, settings) is covered below. Each row lists the domain model it feeds and whether
it is **cached** (persisted to SQLDelight for offline-first) or **live** (network-only).

### 2.1 Authentication & session — `AuthResult`, `User`

Backend exchanges third-party credentials for a first-party session (JWT). OAuth *acquisition*
(Google Credential Manager, Instagram web flow) stays on-device; only the resulting token is sent up.

| Method | Path | Body → Returns | Notes |
|---|---|---|---|
| `POST` | `/v1/auth/google` | `{ idToken }` → `AuthSession` | Verifies Google ID token, upserts user. Feeds `GoogleLoginRepository`. |
| `POST` | `/v1/auth/instagram` | `{ code, redirectUri }` → `AuthSession` | Exchanges Instagram OAuth code. Feeds `InstagramLoginRepository`. |
| `POST` | `/v1/auth/phone/request-otp` | `{ phoneE164 }` → `{ verificationId, expiresInSec }` | Phone/OTP login screen. |
| `POST` | `/v1/auth/phone/verify-otp` | `{ verificationId, code }` → `AuthSession` | |
| `POST` | `/v1/auth/refresh` | `{ refreshToken }` → `AuthSession` | Called by the auth refresh interceptor (§3.2). **No** bearer required. |
| `POST` | `/v1/auth/logout` | `{ refreshToken }` → `204` | Revokes refresh token; client clears `TokenManager`. |
| `GET`  | `/v1/auth/me` | → `User` | Current user; used by splash/session bootstrap. |

```jsonc
// AuthSession  (maps to domain AuthResult + token persistence)
{
  "accessToken": "eyJ…",
  "refreshToken": "def…",
  "expiresInSec": 3600,
  "isNewUser": true,
  "user": { "id": "usr_…", "displayName": "…", "email": "…", "photoUrl": "…" }
}
```

> `AuthResult` currently holds only `isNewUser` + `token`. Extend it (and `User`) as these DTOs
> land; keep the domain model free of transport-only fields (`expiresInSec`, `refreshToken` belong
> in `TokenManager`, not the domain).

### 2.2 User profile — `User`, profile vertical

| Method | Path | Returns | Cache |
|---|---|---|---|
| `GET`   | `/v1/profile` | `UserProfile` | cached |
| `PATCH` | `/v1/profile` | `UserProfile` | write-through |
| `PUT`   | `/v1/profile/avatar` | `{ photoUrl }` (multipart upload) | — |
| `GET`   | `/v1/profile/preferences` | `{ units, theme, goals, notificationsEnabled }` | cached |
| `PATCH` | `/v1/profile/preferences` | same | write-through |

`UserProfile`: `id, displayName, email, photoUrl, heightCm, weightKg, birthEpochMs, sex, activityLevel, bmi (server-computed), goals[]`.

### 2.3 Workouts — `Workout` (the fully-migrated vertical)

This is the reference vertical: SQLDelight is the source of truth, the network layer syncs it.

| Method | Path | Body → Returns | Cache |
|---|---|---|---|
| `GET`    | `/v1/workouts?updatedSince=&cursor=&limit=` | → `Page<WorkoutDto>` | cached (delta sync) |
| `GET`    | `/v1/workouts/{id}` | → `WorkoutDto` | cached |
| `POST`   | `/v1/workouts` | `WorkoutDto` → `WorkoutDto` | write-through + enqueue sync |
| `PUT`    | `/v1/workouts/{id}` | `WorkoutDto` → `WorkoutDto` | write-through |
| `DELETE` | `/v1/workouts/{id}` | → `204` | soft-delete + tombstone sync |
| `POST`   | `/v1/workouts/sync` | `{ changes: WorkoutDto[], deletions: id[], since }` → `{ applied, serverChanges, serverTime }` | batch delta sync |
| `GET`    | `/v1/workouts/catalog` | → `ExerciseDto[]` | cached (exercise library for the player) |

```jsonc
// WorkoutDto  (superset of domain Workout; extra fields drive detail/player screens)
{
  "id": "wk_…", "clientId": "uuid",
  "name": "Push Day", "durationMinutes": 45, "calories": 380,
  "performedAtEpochMs": 1725148800000,
  "exercises": [ { "exerciseId": "ex_…", "sets": 4, "reps": 10, "weightKg": 40 } ],
  "updatedAtEpochMs": 1725148900000, "deleted": false
}
```

### 2.4 AI coach — `AiRecommendation`, `NutritionPlan`, `HealthReport`

The chat coach and recommendation engine. On-device TFLite handles inference where possible; these
endpoints cover server-side LLM chat and plan generation.

| Method | Path | Body → Returns | Cache |
|---|---|---|---|
| `POST` | `/v1/coach/chat` | `{ conversationId?, message, context }` → `CoachReply` | live (persist history locally) |
| `GET`  | `/v1/coach/conversations` | → `Conversation[]` | cached |
| `GET`  | `/v1/coach/conversations/{id}/messages?cursor=` | → `Page<ChatMessage>` | cached |
| `GET`  | `/v1/coach/recommendations` | → `AiRecommendation[]` | cached |
| `POST` | `/v1/coach/plan/workout` | `{ goals, level, daysPerWeek }` → `WorkoutPlan` | cached |
| `POST` | `/v1/coach/plan/nutrition` | `{ goals, dietType, targetCalories }` → `NutritionPlan` | cached |

> **Streaming option.** For the chat screen, prefer SSE: `POST /v1/coach/chat/stream` returning
> `text/event-stream`. Ktor consumes it via `client.preparePost(...).execute { readChannel }`.
> Ship non-streaming first; add streaming behind the same repository method later.

### 2.5 Nutrition — `NutritionPlan`

| Method | Path | Returns | Cache |
|---|---|---|---|
| `GET`    | `/v1/nutrition/plan` | `NutritionPlan` | cached |
| `GET`    | `/v1/nutrition/log?date=` | `NutritionLog` (meals, macros, calories) | cached |
| `POST`   | `/v1/nutrition/log` | log a meal → `NutritionLog` | write-through |
| `DELETE` | `/v1/nutrition/log/{entryId}` | `204` | |
| `GET`    | `/v1/nutrition/foods?query=` | `FoodItem[]` (food database search) | live |

### 2.6 Health tracking — `HeartRate`, `SleepData`, `StepData`

Samples are captured on-device (sensors/BLE/ML) and **pushed up in batches**; aggregates are pulled
for charts.

| Method | Path | Body → Returns | Cache |
|---|---|---|---|
| `POST` | `/v1/health/heart-rate/batch` | `{ samples: [{ bpm, epochMs }] }` → `{ accepted }` | push (batched) |
| `GET`  | `/v1/health/heart-rate?from=&to=&bucket=` | `HeartRateSeries` | cached |
| `POST` | `/v1/health/steps/batch` | `{ samples: [{ steps, epochMs }] }` → `{ accepted }` | push |
| `GET`  | `/v1/health/steps?from=&to=&bucket=day` | `StepSeries` | cached |
| `POST` | `/v1/health/sleep/batch` | `{ sessions: [{ startEpochMs, endEpochMs, stages }] }` → `{ accepted }` | push |
| `GET`  | `/v1/health/sleep?from=&to=` | `SleepData[]` | cached |

`bucket` ∈ `{raw, minute, hour, day}` controls server-side downsampling for chart ranges.

### 2.7 Progress & dashboard — `HealthReport`, dashboard aggregate

| Method | Path | Returns | Cache |
|---|---|---|---|
| `GET` | `/v1/dashboard?date=` | `DashboardSnapshot` (today's steps, calories, HR, next workout, streak) | cached (short TTL) |
| `GET` | `/v1/progress/weekly?weekStartEpochMs=` | `WeeklyReport` | cached |
| `GET` | `/v1/progress/report?from=&to=` | `HealthReport` | cached |
| `GET` | `/v1/progress/goals` | `Goal[]` (progress vs targets) | cached |

`DashboardSnapshot` is a server-composed aggregate so the home screen makes **one** call instead of
fan-out. It's fine for the dashboard to also read the cached vertical tables and only hit this for the
composed streak/summary fields.

### 2.8 Settings & devices

| Method | Path | Body → Returns | Notes |
|---|---|---|---|
| `PUT`    | `/v1/devices/push-token` | `{ fcmToken, platform }` → `204` | FCM registration for notifications/workers. |
| `GET`    | `/v1/settings` | → `AppSettings` | Server-backed prefs (subset of on-device `AppPreferences`). |
| `PATCH`  | `/v1/settings` | → `AppSettings` | |
| `DELETE` | `/v1/account` | → `202` | GDPR account deletion (privacy-first requirement). |

---

## 3. Ktor client architecture

Everything shared lives in **`:core:network`** (`commonMain`). Per-vertical API classes live in
**`:data`** alongside the repositories that consume them. Nothing network-related goes in `:app`.

```
:core:network (commonMain)
 ├─ HttpClientFactory.kt        ← EXTEND: baseUrl, DefaultRequest, Auth, Logging, timeouts
 ├─ ApiConfig.kt                ← NEW: base URL + timeouts (per-build via expect/actual or DI)
 ├─ ApiRoutes.kt                ← NEW: centralised path constants
 ├─ NetworkError.kt             ← MOVE from :app + expand to the taxonomy in §5
 ├─ ApiEnvelope.kt              ← NEW: Page<T>, ErrorEnvelope, ErrorBody
 ├─ SafeApiCall.kt              ← NEW: runs a call, maps failures to ResultState.Err
 └─ TokenProvider.kt            ← NEW: interface bridging Auth plugin ↔ TokenManager

:data (commonMain)
 ├─ remote/dto/*.kt             ← NEW: @Serializable DTOs (WorkoutDto, AuthSession, …)
 ├─ remote/mapper/*.kt          ← NEW: DTO ↔ domain mappers
 └─ remote/*RemoteDataSource.kt ← FILL IN: WorkoutRemoteDataSource, AuthRemoteDataSource, …
```

The empty `:app` classes (`ApiService`, `RetrofitBuilder`, `ApiResponseHandler`, `NetworkModule`) are
Retrofit-era placeholders — **delete them** once callers move to the Ktor sources above.

### 3.1 The configured client

Replace the current bare factory in `HttpClientFactory.kt`:

```kotlin
package com.fitwithai.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest.DefaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun platformHttpEngine(): HttpClientEngine

private val appJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    explicitNulls = false
}

fun createHttpClient(
    config: ApiConfig,
    tokenProvider: TokenProvider,
    engine: HttpClientEngine = platformHttpEngine(),
): HttpClient = HttpClient(engine) {
    expectSuccess = true                       // non-2xx throws → mapped in SafeApiCall

    install(ContentNegotiation) { json(appJson) }

    install(HttpTimeout) {
        requestTimeoutMillis = config.requestTimeoutMs
        connectTimeoutMillis = config.connectTimeoutMs
        socketTimeoutMillis = config.socketTimeoutMs
    }

    install(DefaultRequest) {
        url(config.baseUrl)                    // "https://api.fitwithai.app/"
        contentType(ContentType.Application.Json)
        header("X-Client-Platform", config.platformName)
        header("X-Client-Version", config.appVersion)
    }

    install(Auth) {
        bearer {
            loadTokens {
                tokenProvider.current()?.let { BearerTokens(it.access, it.refresh) }
            }
            refreshTokens {
                // Called automatically on 401. Uses a bearer-free client internally.
                tokenProvider.refresh()?.let { BearerTokens(it.access, it.refresh) }
            }
            sendWithoutRequest { req -> !req.url.encodedPath.startsWith("/v1/auth/") }
        }
    }

    install(Logging) { level = config.logLevel } // NONE in release, BODY/HEADERS in debug
}
```

`ApiConfig` is a plain data class provided by DI (the Android/iOS platform Koin module can vary
`baseUrl`/`logLevel` per build type):

```kotlin
data class ApiConfig(
    val baseUrl: String,
    val platformName: String,      // from :core:platform currentPlatform()
    val appVersion: String,
    val requestTimeoutMs: Long = 30_000,
    val connectTimeoutMs: Long = 15_000,
    val socketTimeoutMs: Long = 30_000,
    val logLevel: LogLevel = LogLevel.NONE,
)
```

### 3.2 Token provider + refresh

The Ktor `Auth` plugin owns 401 refresh; it delegates persistence to the existing
`TokenManager` (`:core:datastore`) via a thin bridge so `:core:network` doesn't depend on datastore
internals:

```kotlin
package com.fitwithai.core.network

data class AuthTokens(val access: String, val refresh: String)

interface TokenProvider {
    suspend fun current(): AuthTokens?
    suspend fun refresh(): AuthTokens?   // returns null → caller must re-auth (logout)
}
```

Implement `TokenProvider` in `:data` (it can read `TokenManager` and call `/v1/auth/refresh` with a
bare client). On `refresh()` returning null, clear tokens and emit a session-expired signal the
`SplashViewModel`/nav graph observes to route back to login.

> **Dependencies to add** to `gradle/libs.versions.toml` (Ktor BOM already at 3.5.0):
> `ktor-client-auth`, `ktor-client-logging`. Add both to `:core:network` `commonMain`. Optionally
> `ktor-client-resources` if you want type-safe routes instead of `ApiRoutes` string constants.

---

## 4. DTO ↔ domain mapping

**Never** expose Ktor/serialization types past `:data`. DTOs are `@Serializable`, live in
`:data/remote/dto`, and map to the pure-Kotlin domain models in `:domain`.

```kotlin
// :data/remote/dto/WorkoutDto.kt
@Serializable
data class WorkoutDto(
    val id: String,
    val clientId: String? = null,
    val name: String,
    val durationMinutes: Int,
    val calories: Int,
    val performedAtEpochMs: Long,
    val updatedAtEpochMs: Long = 0,
    val deleted: Boolean = false,
)

// :data/remote/mapper/WorkoutMapper.kt
fun WorkoutDto.toDomain() = Workout(id, name, durationMinutes, calories, performedAtEpochMs)
fun Workout.toDto() = WorkoutDto(id, null, name, durationMinutes, calories, performedAtEpochMs)
```

The domain `Workout` stays lean; transport-only fields (`updatedAtEpochMs`, `deleted`, `clientId`)
live only on the DTO and the SQLDelight row, and drive sync — they never reach the UI.

---

## 5. Error handling

Move `NetworkError` into `:core:network` and expand it into a closed taxonomy, then funnel every call
through one helper that returns the app's `ResultState` (from `:core:common`).

```kotlin
sealed class NetworkError(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    data object NoConnectivity : NetworkError()
    data object Timeout : NetworkError()
    data class Unauthorized(val code: String?) : NetworkError()       // 401 after refresh failed
    data class Forbidden(val code: String?) : NetworkError()          // 403
    data class NotFound(val code: String?) : NetworkError()           // 404
    data class Conflict(val code: String?) : NetworkError()           // 409
    data class Validation(val code: String?, val fields: Map<String,String>) : NetworkError() // 422
    data class RateLimited(val retryAfterSec: Long?) : NetworkError() // 429
    data class Server(val status: Int, val code: String?) : NetworkError() // 5xx
    data class Serialization(val detail: String?) : NetworkError()
    data class Unknown(val detail: String?) : NetworkError()
}

suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): ResultState<T> =
    try {
        ResultState.Ok(block())
    } catch (e: ClientRequestException) {          // 4xx (expectSuccess=true)
        ResultState.Err(e.toNetworkError())
    } catch (e: ServerResponseException) {         // 5xx
        ResultState.Err(NetworkError.Server(e.response.status.value, e.errorCode()))
    } catch (e: HttpRequestTimeoutException) {
        ResultState.Err(NetworkError.Timeout)
    } catch (e: IOException) {
        ResultState.Err(NetworkError.NoConnectivity)
    } catch (e: SerializationException) {
        ResultState.Err(NetworkError.Serialization(e.message))
    } catch (e: Throwable) {
        ResultState.Err(NetworkError.Unknown(e.message))
    }
```

`toNetworkError()` reads the §1.2 `ErrorEnvelope` from the response body to populate `code`/`fields`.
Repositories return `ResultState<T>`; ViewModels map that to `UiState` for the screen.

---

## 6. Offline-first sync strategy

Matches how the Workout vertical already works (SQLDelight `Flow` is the UI's source of truth).

1. **Read:** UI observes SQLDelight via the repository `Flow`. Network never blocks the UI.
2. **Refresh:** repository calls the remote source with `?updatedSince=<lastSyncEpochMs>`, upserts
   the delta into SQLDelight, and stores the new high-water mark in `AppPreferences`.
3. **Write:** write to SQLDelight immediately (optimistic) with a `pendingSync` flag, then push. On
   success clear the flag; on failure leave it for the sync worker to retry.
4. **Delete:** soft-delete locally (tombstone) → `DELETE`/sync → hard-delete on confirmation.
5. **Batch/background:** the existing `:app` workers (`WorkoutSyncManager`, health sync) drain
   pending changes via `POST /v1/workouts/sync` and the `/batch` health endpoints, honouring
   `Idempotency-Key` so retries are safe.

Health samples (§2.6) are **push-mostly**: capture locally, batch-upload on a cadence, and only pull
aggregates for charts — never round-trip raw samples through the UI.

---

## 7. DI wiring (Koin)

Register the client in `:core:network`'s own Koin module so both platforms get it (per CLAUDE.md, put
shared deps in the owning module, not `appModule`):

```kotlin
// :core:network  networkModule
val networkModule = module {
    single { createHttpClient(get(), get()) }     // ApiConfig, TokenProvider
}
// :data  dataModule (extend)
single { WorkoutRemoteDataSource(get()) }
single<TokenProvider> { DefaultTokenProvider(get(), get()) }  // TokenManager + bare client
```

The **platform** modules supply `ApiConfig` (Android `androidPlatformModule`, iOS
`iosPlatformModule`) so base URL / log level vary per build. Add `networkModule` to `sharedModules`
in `:shared`, then **re-run `SharedKoinGraphTest`** (`koin-test verify()` over the shared graph) after
wiring — required by the DI conventions in CLAUDE.md.

---

## 8. Worked example — Workout remote source

Fills in the current `WorkoutRemoteDataSource` stub:

```kotlin
class WorkoutRemoteDataSource(private val client: HttpClient) {

    suspend fun fetch(updatedSince: Long? = null, cursor: String? = null): ResultState<Page<WorkoutDto>> =
        safeApiCall {
            client.get(ApiRoutes.WORKOUTS) {
                updatedSince?.let { parameter("updatedSince", it) }
                cursor?.let { parameter("cursor", it) }
                parameter("limit", 50)
            }.body()
        }

    suspend fun upsert(dto: WorkoutDto): ResultState<WorkoutDto> = safeApiCall {
        client.post(ApiRoutes.WORKOUTS) {
            header("Idempotency-Key", dto.clientId ?: dto.id)
            setBody(dto)
        }.body()
    }

    suspend fun delete(id: String): ResultState<Unit> = safeApiCall {
        client.delete("${ApiRoutes.WORKOUTS}/$id"); Unit
    }

    suspend fun sync(changes: List<WorkoutDto>, deletions: List<String>, since: Long): ResultState<SyncResultDto> =
        safeApiCall {
            client.post("${ApiRoutes.WORKOUTS}/sync") {
                setBody(SyncRequestDto(changes, deletions, since))
            }.body()
        }
}
```

`WorkoutRepositoryImpl` (already SQLDelight-backed) calls these, merges into the DB, and keeps the UI
`Flow` as the single source of truth.

---

## 9. Testing

Use Ktor's `MockEngine` — no real server, runs on the JVM alongside the existing `:app` unit tests.

```kotlin
@Test
fun `fetch maps workout page`() = runTest {
    val engine = MockEngine { respond(
        content = """{"data":[{"id":"wk_1","name":"Push","durationMinutes":45,"calories":380,"performedAtEpochMs":1725148800000}],"nextCursor":null}""",
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json"),
    ) }
    val client = createHttpClient(testConfig, FakeTokenProvider, engine)
    val result = WorkoutRemoteDataSource(client).fetch()
    assertTrue(result is ResultState.Ok)
}
```

Cover per source: happy path, 401→refresh→retry, 404/422 code mapping, timeout, malformed JSON. Add
a MockEngine test that asserts the `Authorization` header is attached to a non-auth route and absent
on `/v1/auth/*`. Keep the existing `SharedKoinGraphTest` green after DI changes.

---

## 10. Implementation checklist

- [ ] Add `ktor-client-auth` + `ktor-client-logging` to `libs.versions.toml` and `:core:network`.
- [ ] Extend `HttpClientFactory.kt` (baseUrl, DefaultRequest, Auth, Logging, timeouts, `expectSuccess`).
- [ ] Add `ApiConfig`, `ApiRoutes`, `ApiEnvelope` (`Page`/`ErrorEnvelope`), `SafeApiCall`, `TokenProvider`.
- [ ] Move + expand `NetworkError` into `:core:network`; delete `:app` Retrofit placeholders.
- [ ] Add `networkModule`; supply `ApiConfig` from each platform module; add to `sharedModules`.
- [ ] Implement `DefaultTokenProvider` over `TokenManager` + `/v1/auth/refresh`.
- [ ] Per vertical: DTOs → mappers → fill the remote data source → wire into the repository.
- [ ] MockEngine tests per source + auth-header test; re-run `SharedKoinGraphTest`.
- [ ] Verify shared code compiles: `./gradlew :core:network:compileAndroidMain :data:compileAndroidMain`, then `./gradlew assembleDebug :app:testDebugUnitTest`.

---

## 11. Endpoint index (quick reference)

```
AUTH      POST /v1/auth/google | /instagram | /phone/request-otp | /phone/verify-otp
          POST /v1/auth/refresh | /logout            GET /v1/auth/me
PROFILE   GET|PATCH /v1/profile     PUT /v1/profile/avatar     GET|PATCH /v1/profile/preferences
WORKOUT   GET|POST /v1/workouts     GET|PUT|DELETE /v1/workouts/{id}
          POST /v1/workouts/sync    GET /v1/workouts/catalog
COACH     POST /v1/coach/chat[/stream]    GET /v1/coach/conversations[/{id}/messages]
          GET /v1/coach/recommendations   POST /v1/coach/plan/workout | /plan/nutrition
NUTRITION GET /v1/nutrition/plan    GET|POST|DELETE /v1/nutrition/log     GET /v1/nutrition/foods
HEALTH    POST /v1/health/{heart-rate|steps|sleep}/batch
          GET  /v1/health/{heart-rate|steps|sleep}
PROGRESS  GET /v1/dashboard   /v1/progress/weekly   /v1/progress/report   /v1/progress/goals
SYSTEM    PUT /v1/devices/push-token   GET|PATCH /v1/settings   DELETE /v1/account
```
