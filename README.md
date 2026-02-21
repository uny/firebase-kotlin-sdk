# Firebase Kotlin SDK

[![Maven Central](https://img.shields.io/maven-central/v/dev.ynagai.firebase/firebase-ai.svg)](https://search.maven.org/search?q=g:dev.ynagai.firebase)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CI](https://github.com/uny/firebase-kotlin-sdk/actions/workflows/ci.yml/badge.svg)](https://github.com/uny/firebase-kotlin-sdk/actions/workflows/ci.yml)

A Kotlin Multiplatform SDK for Firebase, with a focus on Firebase AI (Generative AI) capabilities.

## Modules

| Module | Description | API Coverage |
|--------|-------------|-------------|
| `firebase-app` | Core Firebase App initialization | [![75%](https://img.shields.io/badge/-75%25-orange?style=flat-square)](/firebase-app/src/commonMain/kotlin/dev/ynagai/firebase/) |
| `firebase-ai` | Firebase AI (Generative AI with Gemini) | [![80%](https://img.shields.io/badge/-80%25-green?style=flat-square)](/firebase-ai/src/commonMain/kotlin/dev/ynagai/firebase/ai/) |
| `firebase-auth` | Firebase Authentication | [![90%](https://img.shields.io/badge/-90%25-green?style=flat-square)](/firebase-auth/src/commonMain/kotlin/dev/ynagai/firebase/auth/) |
| `firebase-firestore` | Cloud Firestore (real-time database) | [![80%](https://img.shields.io/badge/-80%25-green?style=flat-square)](/firebase-firestore/src/commonMain/kotlin/dev/ynagai/firebase/firestore/) |
| `firebase-common` | Shared utilities and types | — |

## Supported Platforms

- Android
- iOS (arm64, simulatorArm64)

## Installation

### Version Catalog (Recommended)

Add to your `gradle/libs.versions.toml`:

```toml
[versions]
firebase-kotlin-sdk = "0.1.0"

[libraries]
firebase-app = { module = "dev.ynagai.firebase:firebase-app", version.ref = "firebase-kotlin-sdk" }
firebase-ai = { module = "dev.ynagai.firebase:firebase-ai", version.ref = "firebase-kotlin-sdk" }
firebase-auth = { module = "dev.ynagai.firebase:firebase-auth", version.ref = "firebase-kotlin-sdk" }
firebase-firestore = { module = "dev.ynagai.firebase:firebase-firestore", version.ref = "firebase-kotlin-sdk" }
```

### Dependencies

Add to your module's `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.firebase.app)
            implementation(libs.firebase.ai)
            implementation(libs.firebase.auth)
            implementation(libs.firebase.firestore)
        }
    }
}
```

## Usage

See each module's README for detailed usage and examples:

- **[Firebase AI](firebase-ai/README.md)** — Generative AI with Gemini (text generation, streaming, chat, content DSL)
- **[Firebase Auth](firebase-auth/README.md)** — Authentication (email/password, social providers, phone, MFA)
- **[Firebase Firestore](firebase-firestore/README.md)** — Cloud Firestore (CRUD, queries, real-time listeners, transactions)

## Platform Setup

### Android

Add your `google-services.json` to your app module and apply the Google Services plugin:

```kotlin
// build.gradle.kts (project)
plugins {
    id("com.google.gms.google-services") version "4.4.2" apply false
}

// build.gradle.kts (app)
plugins {
    id("com.google.gms.google-services")
}
```

### iOS

Add your `GoogleService-Info.plist` to your Xcode project and initialize Firebase in your app delegate:

```swift
import FirebaseCore

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        FirebaseApp.configure()
        return true
    }
}
```

## License

```
Copyright 2025 Yuki Nagai

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
