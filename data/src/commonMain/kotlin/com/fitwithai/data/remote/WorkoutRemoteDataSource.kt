package com.fitwithai.data.remote

import com.fitwithai.common.state.ResultState
import com.fitwithai.core.network.ApiRoutes
import com.fitwithai.core.network.Page
import com.fitwithai.core.network.safeApiCall
import com.fitwithai.data.remote.dto.SyncRequestDto
import com.fitwithai.data.remote.dto.SyncResultDto
import com.fitwithai.data.remote.dto.WorkoutDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/**
 * Ktor remote source for the Workout vertical (§8). Every call is wrapped in [safeApiCall] so
 * failures surface as `ResultState.Err(NetworkError)`. The authed [HttpClient] attaches the bearer
 * token and applies the base URL via `DefaultRequest`.
 */
class WorkoutRemoteDataSource(private val client: HttpClient) {

    /** Delta fetch: pass `updatedSince` to pull only changes since the last sync high-water mark. */
    suspend fun fetch(
        updatedSince: Long? = null,
        cursor: String? = null,
        limit: Int = 50,
    ): ResultState<Page<WorkoutDto>> = safeApiCall {
        client.get(ApiRoutes.WORKOUTS) {
            updatedSince?.let { parameter("updatedSince", it) }
            cursor?.let { parameter("cursor", it) }
            parameter("limit", limit)
        }.body()
    }

    /** Idempotent create/update — the `Idempotency-Key` makes retries safe. */
    suspend fun upsert(dto: WorkoutDto): ResultState<WorkoutDto> = safeApiCall {
        client.post(ApiRoutes.WORKOUTS) {
            header("Idempotency-Key", dto.clientId ?: dto.id)
            setBody(dto)
        }.body()
    }

    suspend fun delete(id: String): ResultState<Unit> = safeApiCall {
        client.delete("${ApiRoutes.WORKOUTS}/$id")
        Unit
    }

    /** Batch delta sync — pushes local changes/deletions and returns server-side changes. */
    suspend fun sync(
        changes: List<WorkoutDto>,
        deletions: List<String>,
        since: Long,
    ): ResultState<SyncResultDto> = safeApiCall {
        client.post(ApiRoutes.WORKOUTS_SYNC) {
            setBody(SyncRequestDto(changes, deletions, since))
        }.body()
    }
}
