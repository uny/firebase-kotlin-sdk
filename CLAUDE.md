# CLAUDE.md

## Build & Test

```bash
./gradlew assemble check --build-cache
```

No tests exist yet.

## Architecture

KMP wrapper library around official Firebase SDKs (Android & iOS).

- **Common**: `expect class` / pure Kotlin interfaces & data classes
- **Android**: `actual class` holding native `com.google.firebase.*` instance
- **iOS (Apple)**: `actual class` holding native instance via cinterop/SwiftPM

Async: Android uses `Task<T>.await()`, iOS uses `suspendCancellableCoroutine`. Streaming uses `Flow<T>`.

Data classes are defined in `commonMain` (not `expect/actual`), with internal mapper extensions in platform source sets.

## Module Structure

| Module | Package |
|--------|---------|
| `firebase-app` | `dev.ynagai.firebase` |
| `firebase-ai` | `dev.ynagai.firebase.ai` |
| `firebase-firestore` | `dev.ynagai.firebase.firestore` |
| `firebase-common` | `dev.ynagai.firebase` |

## Conventions

- Class/method names mirror the **Android Firebase SDK** reference docs
- Namespace: `dev.ynagai.firebase.<module>`
- Convention plugin: `dev.ynagai.firebase.library` (defined in `build-logic/`)
- Dependencies managed via Version Catalog (`gradle/libs.versions.toml`)

## iOS / SwiftPM Interop

- SwiftPM deps declared in `build.gradle.kts` via `swiftPMDependencies`
- cinterop bindings: `swiftPMImport.firebase.kotlin.sdk...`
- ObjC naming: watch for prefixes (`FIR`, `KFB`)
- `firebase-ai` iOS uses a **fork** of firebase-ios-sdk that exposes ObjC headers for Vertex AI

## Gotchas

- **Kotlin version**: Uses non-standard build (`titan`) for SwiftPM compatibility. Will update to standard after upstream support lands.
- **Tests**: No tests exist yet.
- **firebase-ai iOS**: Implemented as TODO stubs — not functional on iOS.
