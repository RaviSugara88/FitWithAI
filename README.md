# FitWithAI

FitWithAI is an AI-powered fitness and health tracking Android app. It helps users monitor walking, running, sleep, and heart rate using on-device intelligence, smart reminders, and personalized fitness guidance—designed for offline use, privacy-first data handling, and optimized performance.

## Features

| Area | Description |
|------|-------------|
| **Authentication** | Google Sign-In, Instagram login, and phone number + OTP flows with session management |
| **Dashboard & home** | Daily overview, quick actions, and navigation hub |
| **Workouts** | Workout list, detail views, and exercise player scaffolding |
| **AI coach** | Chat-style AI fitness coach UI |
| **Health tracking** | Heart rate, sleep, steps, and progress / weekly reports |
| **Nutrition** | Nutrition and diet plan screens |
| **Profile** | User profile, BMI, units, and fitness preferences |
| **Settings** | App preferences and theme support (DataStore) |
| **Background work** | Sync, health sync, notifications, and cleanup workers |
| **Sensors & wearables** | Android sensor managers and BLE scaffolding for wearables |
| **On-device ML** | TensorFlow Lite models for health, sleep, calories, and workout recommendations |

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3, Navigation Compose, Lottie
- **Architecture:** Clean architecture (presentation → domain → data)
- **DI:** Koin
- **Auth:** Firebase Auth, Google Identity / Credentials API
- **Storage:** DataStore Preferences, Room (database scaffolding)
- **Networking:** Retrofit-style API layer (core network module)
- **Distribution:** Firebase App Distribution (debug / release builds)

| Requirement | Version |
|-------------|---------|
| Min SDK | 26 |
| Target / compile SDK | 36 |
| JDK | 11 |

## Architecture

The app follows **clean architecture** with clear separation between UI, business logic, and data sources. Shared concerns (networking, database, sensors, ML) live in dedicated packages under `com.fitwithai`.

```
┌─────────────────────────────────────────────────────────────┐
│  UI (Compose screens, ViewModels, components, theme)        │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│  Domain (models, repository interfaces, use cases)          │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│  Data (repository impls, local / remote data sources)       │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│  Core (network, database, sensors, services, bluetooth)     │
└─────────────────────────────────────────────────────────────┘
```

### Offline-first data flow

1. UI subscribes to `StateFlow` exposed by ViewModels.
2. ViewModels call domain use cases.
3. Repositories combine local persistence (Room / DataStore) with remote API data.
4. Background workers and sync helpers coordinate refreshes, caching, and conflict resolution.

See [docs/project-structure.md](docs/project-structure.md) for the target module layout and layering conventions.

## Project structure

Single-module Android app (`:app`). Source lives under `app/src/main/java/com/fitwithai/`:

```
app/
└── src/main/java/com/fitwithai/
    ├── app/              # Application, MainActivity, AppInitializer
    ├── auth/             # TokenManager, SessionManager, AuthInterceptor
    ├── common/           # Constants, extensions, resources, UiState / ResultState
    ├── core/             # Network, Room DB, sensors, BLE, background services
    ├── data/             # Repository implementations and data sources
    ├── datastore/        # Auth, theme, user, and fitness preferences
    ├── di/               # Koin modules (app, network, DB, repo, sensor, AI, VM)
    ├── domain/           # Models, repository contracts, use cases
    ├── ml/               # TFLite models, preprocessing, inference
    ├── navigation/       # Routes and bottom-nav definitions
    ├── notification/     # Reminders and notification manager
    ├── security/         # Encryption, secure storage, biometrics
    ├── socket/           # Real-time chat socket scaffolding
    ├── ui/               # Compose screens, components, theme, navigation graph
    └── worker/           # WorkManager sync and cleanup jobs
```

### UI screens (`ui/screens/`)

- `splash` — Splash entry
- `login` — Social login, phone + OTP
- `dashboard` / `home` — Main hub
- `workout` — Workouts and exercise player
- `ai_coach` — AI coach chat
- `heart` / `sleep` / `progress` — Health metrics
- `nutrition` — Diet and nutrition
- `profile` — User profile and BMI
- `settings` — App settings

Package-level notes: [app/src/main/java/com/fitwithai/README.md](app/src/main/java/com/fitwithai/README.md).

## Dependency injection

Koin is initialized in `FitWithAiApp` with modules:

- `appModule` — Firebase Auth, token manager, login repositories / use cases
- `networkModule`, `databaseModule`, `repositoryModule`
- `sensorModule`, `aiModule`, `viewModelModule`

## Getting started

### Prerequisites

- Android Studio (latest stable recommended)
- JDK 11+
- Android SDK 36
- `google-services.json` in `app/` (Firebase project; not committed—add locally)

### Build and run

```bash
git clone https://github.com/RaviSugara88/FitWithAI.git
cd FitWithAI
./gradlew assembleDebug
```

Install the debug APK on a device or emulator, or run from Android Studio.

### Firebase App Distribution (optional)

```bash
./gradlew assembleDebug appDistributionUploadDebug
```

Release and debug build types are configured in `app/build.gradle.kts` with tester groups and release notes.

## Configuration

| File | Purpose |
|------|---------|
| `app/google-services.json` | Firebase (Auth, App Distribution)—add from Firebase Console |
| `app/build.gradle.kts` | SDK versions, dependencies, distribution settings |
| `gradle/libs.versions.toml` | Centralized dependency versions |

## Documentation

- [Project structure & architecture](docs/project-structure.md)
- [Source package layout](app/src/main/java/com/fitwithai/README.md)

## Roadmap

Many types under `core/`, `ml/`, `socket/`, and feature screens are **scaffolding placeholders** for upcoming implementation. Production logic is added incrementally in the same packages.

## License

Proprietary — see repository owner for usage terms.
