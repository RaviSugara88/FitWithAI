# FitWithAI Project Structure

This layout follows clean architecture with clear separation between presentation, domain, and data layers, plus shared core modules for cross-cutting concerns (networking, database, AI, sensors).

```
app/                    # Android app entry point + Compose UI
core/
  common/               # Result wrappers, dispatcher providers
  network/              # Ktor + OkHttp setup, error handling, caching hooks
  database/             # Room database + DAOs
  ai/                   # TensorFlow Lite wrappers
  sensors/              # Android Sensor APIs + fusion utilities
presentation/           # ViewModels, UI state, Compose-driven presentation logic
  state/
  viewmodel/
domain/                 # Pure business logic
  model/
  repository/
  usecase/
data/                   # Offline-first data sources + sync orchestration
  local/
  remote/
  repository/
  sync/
```

## Offline-first data flow
1. UI subscribes to `StateFlow` exposed by ViewModels.
2. ViewModels use domain use cases to stream data.
3. Repositories combine Room-backed local data with remote Ktor data sources.
4. Sync managers coordinate refreshes, caching, and conflict resolution.
