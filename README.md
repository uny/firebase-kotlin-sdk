# Firebase Kotlin SDK

[![Maven Central](https://img.shields.io/maven-central/v/dev.ynagai.firebase/firebase-ai.svg)](https://search.maven.org/search?q=g:dev.ynagai.firebase)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CI](https://github.com/uny/firebase-kotlin-sdk/actions/workflows/ci.yml/badge.svg)](https://github.com/uny/firebase-kotlin-sdk/actions/workflows/ci.yml)

A Kotlin Multiplatform SDK for Firebase, with a focus on Firebase AI (Generative AI) capabilities.

## Modules

| Module | Description |
|--------|-------------|
| `firebase-app` | Core Firebase App initialization |
| `firebase-ai` | Firebase AI (Generative AI with Gemini) |
| `firebase-firestore` | Cloud Firestore (real-time database) |
| `firebase-common` | Shared utilities and types |

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
            implementation(libs.firebase.firestore)
        }
    }
}
```

## Usage

### Initialize Firebase AI

```kotlin
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.ai.ai
import dev.ynagai.firebase.ai.GenerativeBackend

// Using Google AI (default)
val ai = Firebase.ai()

// Using Vertex AI
val ai = Firebase.ai(backend = GenerativeBackend.vertexAI("us-central1"))
```

### Create a Generative Model

```kotlin
import dev.ynagai.firebase.ai.generationConfig

val model = ai.generativeModel(
    modelName = "gemini-2.0-flash",
    generationConfig = generationConfig {
        temperature = 0.7f
        maxOutputTokens = 1024
    }
)
```

### Generate Content

```kotlin
// Simple text generation
val response = model.generateContent("Explain quantum computing in simple terms")
println(response.text)

// Streaming response
model.generateContentStream("Tell me a story").collect { chunk ->
    print(chunk.text)
}
```

### Multi-turn Chat

```kotlin
val chat = model.startChat()

val response1 = chat.sendMessage("My name is Alice")
println(response1.text)

val response2 = chat.sendMessage("What's my name?")
println(response2.text) // References "Alice" from context

// Stream chat responses
chat.sendMessageStream("Tell me more").collect { chunk ->
    print(chunk.text)
}
```

### Content DSL

Build complex content with text and images:

```kotlin
import dev.ynagai.firebase.ai.content

val content = content {
    text("What's in this image?")
    inlineData("image/png", imageBytes)
}

val response = model.generateContent(content)
```

### Safety Settings

Configure content filtering:

```kotlin
import dev.ynagai.firebase.ai.SafetySetting
import dev.ynagai.firebase.ai.HarmCategory
import dev.ynagai.firebase.ai.HarmBlockThreshold

val model = ai.generativeModel(
    modelName = "gemini-2.0-flash",
    safetySettings = listOf(
        SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.MEDIUM_AND_ABOVE),
        SafetySetting(HarmCategory.HATE_SPEECH, HarmBlockThreshold.LOW_AND_ABOVE)
    )
)
```

### Count Tokens

Estimate token usage before generation:

```kotlin
val tokenResponse = model.countTokens("Hello, world!")
println("Tokens: ${tokenResponse.totalTokens}")
```

## Firebase Firestore

### Get a Firestore Instance

```kotlin
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.firestore.firestore

val db = Firebase.firestore
```

### Add and Read Data

```kotlin
// Add a document
val docRef = db.collection("users").add(
    mapOf("name" to "Alice", "age" to 30L)
)

// Read a document
val snapshot = db.collection("users").document("alice").get()
val name = snapshot.getString("name")
```

### Query Documents

```kotlin
val query = db.collection("users")
    .whereEqualTo("city", "Tokyo")
    .orderBy("age")
    .limit(10)

val results = query.get()
results.forEach { doc ->
    println("${doc.id}: ${doc.data}")
}
```

### Real-time Updates

```kotlin
db.collection("users").document("alice").snapshots.collect { snapshot ->
    println("Current data: ${snapshot.data}")
}
```

### Transactions and Batched Writes

```kotlin
// Transaction
db.runTransaction {
    val snapshot = get(docRef)
    val newCount = (snapshot.getLong("count") ?: 0) + 1
    update(docRef, mapOf("count" to newCount))
}

// Batched write
db.batch().apply {
    set(docRef1, mapOf("name" to "Alice"))
    update(docRef2, mapOf("age" to 31L))
    delete(docRef3)
}.commit()
```

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
