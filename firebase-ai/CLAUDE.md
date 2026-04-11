# firebase-ai — Module Instructions

## Reference

- [Android Firebase AI API docs](https://firebase.google.com/docs/reference/kotlin/com/google/firebase/ai/package-summary)

## Key Types

- `FirebaseAI` — module entry point for obtaining models
- `GenerativeModel` — main entry point for content generation
- `Chat` — multi-turn conversation state
- `Content`, `Part`, `TextPart`, `InlineDataPart` — content building blocks
- `FileDataPart` — file reference by URI
- `FunctionCallPart`, `FunctionResponsePart` — function calling parts
- `GenerateContentResponse` — response wrapper
- `GenerationConfig`, `ResponseModality` — temperature, maxOutputTokens, modalities, etc.
- `SafetySetting`, `HarmCategory`, `HarmBlockThreshold` — safety filtering
- `CountTokensResponse` — token counting result
- `GenerativeBackend` — backend selection (Google AI / Vertex AI)
- `Tool` (sealed class) — `FunctionDeclarations`, `GoogleSearch`, `UrlContext`, `CodeExecution`
- `FunctionDeclaration`, `ToolConfig`, `Schema` — function calling / structured output
- `ThinkingConfig`, `ThinkingLevel` — model thinking configuration
- `ExecutableCodePart`, `CodeExecutionResultPart`, `CodeExecutionOutcome` — code execution parts
- `GroundingMetadata`, `GroundingChunk`, `WebGroundingChunk`, `GroundingSupport`, `Segment`, `SearchEntryPoint` — grounding metadata
- `UrlContextMetadata`, `UrlMetadata`, `UrlRetrievalStatus` — URL context metadata
- `ContentModality`, `ModalityTokenCount` — token usage by modality
- `AudioTranscriptionConfig` — audio transcription in live sessions
- `ImagenModel` (**deprecated**) — image generation using Imagen
- `ImagenGenerationConfig`, `ImagenImageFormat` (**deprecated**) — image generation configuration
- `ImagenSafetySettings`, `ImagenSafetyFilterLevel`, `ImagenPersonFilterLevel` (**deprecated**) — image safety filtering
- `ImagenGenerationResponse`, `ImagenInlineImage` (**deprecated**) — image generation response
- `LiveGenerativeModel` — real-time streaming model
- `LiveSession` — bidirectional live session
- `LiveGenerationConfig`, `SpeechConfig`, `Voice` — live generation configuration
- `LiveServerMessage` — sealed interface for live server messages (Content, ToolCall, ToolCallCancellation, GoingAway)
- `FirebaseAIException` — base exception class and subclasses

## Platform Status

| Platform | Status |
|----------|--------|
| Android | Functional — wraps `com.google.firebase:firebase-ai` |
| iOS | Functional — wraps Firebase iOS SDK via cinterop fork |

## iOS Fork

iOS cinterop depends on a **fork** of `firebase-ios-sdk` that exposes ObjC headers for the Vertex AI module. The fork repo is configured in `build.gradle.kts` via `swiftPMDependencies`.

ObjC prefix: `KFB` (fork-specific prefix for the AI module).

## Not Yet Implemented

- Cached content
- `JsonSchema` / `generateObject` (structured output — Android only, not yet in iOS fork)
- `GenerativeModelSession` (iOS-only Foundation Models API — not wrappable as common API)
