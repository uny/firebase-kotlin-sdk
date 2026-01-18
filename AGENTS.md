# Project Context & Guidelines (firebase-kotlin-sdk)

## Project Overview
This project is a Kotlin Multiplatform (KMP) library that wraps the official Firebase SDKs (Android and iOS) to provide a unified Kotlin API.
The goal is to provide an API surface identical or very similar to the official **Firebase Android Kotlin Extensions (KTX)**, allowing developers to share Firebase logic across platforms.

## Tech Stack
- **Language:** Kotlin (Version 2.2.21+)
- **Build System:** Gradle (Kotlin DSL), Version Catalogs (`libs.versions.toml`)
- **Target Platforms:**
    - `androidTarget` (minSdk 24, compileSdk 36)
    - `iosArm64`, `iosSimulatorArm64` (accessing native SDKs via SwiftPM integration)
- **Convention Plugins:** Custom plugins defined in `build-logic` (id: `dev.ynagai.firebase.library`)

## Architectural Patterns

### 1. The Wrapper Pattern
We do not reimplement Firebase logic. We **wrap** the native SDKs.
- **Common:** Define `expect class` or pure Kotlin interfaces/data classes.
- **Android:** `actual class` holding a reference to the native Android instance (`com.google.firebase.*`).
- **iOS:** `actual class` holding a reference to the native iOS instance (via cinterop/SwiftPM).

**Code Style Example:**
```kotlin
// commonMain
expect class SomeFirebaseClass {
    fun doSomething()
}

// androidMain
actual class SomeFirebaseClass internal constructor(
    internal val android: com.google.firebase.SomeNativeClass
) {
    actual fun doSomething() {
        android.doSomething()
    }
}

// appleMain
actual class SomeFirebaseClass internal constructor(
    internal val apple: cocoapods.FirebaseSome.FIRSomeNativeClass
) {
    actual fun doSomething() {
        apple.doSomething()
    }
}

```

### 2. Async Handling

* **Android:** Convert `Task<T>` to Coroutines using `kotlinx.coroutines.tasks.await()`.
* **iOS:** Convert callbacks/completion handlers to `suspendCancellableCoroutine` or use generic interop utilities if available.
* **Streaming:** Use `Flow<T>`.

### 3. Data Classes & Enums

* Since `expect/actual` has limitations for data classes (constructors, properties), prefer defining **Pure Kotlin Data Classes** in `commonMain`.
* Provide **Mapper Extensions** (internal) in platform source sets to convert between the Common Data Class and the Native Platform Object.

## Module Structure

* `:firebase-app`: Core initialization (FirebaseApp, FirebaseOptions).
* `:firebase-ai`: Generative AI (Vertex AI/Google AI) wrappers.
* `:firebase-common`: Shared utilities.

## Naming Conventions

* Keep class names and method signatures consistent with the **Android Firebase SDK** reference documentation.
* Namespace: `dev.ynagai.firebase.<module>`

## SwiftPM / iOS Interop

* We use SwiftPM dependencies defined in `build.gradle.kts` via `swiftPMDependencies`.
* Generated cinterop bindings are usually under `swiftPMImport.firebase.kotlin.sdk...`.
* Be careful with ObjC naming conventions (prefixes like `FIR` or `KFB`).

## Specific Instructions for `firebase-ai`

* **Reference:** [Android com.google.firebase.ai Package](https://firebase.google.com/docs/reference/kotlin/com/google/firebase/ai/package-summary)
* The goal is to support `GenerativeModel.generateContent()`, `generateContentStream()`, and related types (`Content`, `Part`, `BlobPart`, `TextPart`).
