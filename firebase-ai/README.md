# Firebase AI

Kotlin Multiplatform wrapper for Firebase AI (Generative AI with Gemini). Provides text generation, streaming, multi-turn chat, and a content DSL — all sharing a single Kotlin API across Android and iOS.

## Installation

```kotlin
implementation("dev.ynagai.firebase:firebase-ai:<version>")
```

Or via Version Catalog — see the [root README](../README.md#installation).

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
