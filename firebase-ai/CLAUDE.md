# firebase-ai — Module Instructions

## Reference

- [Android Firebase AI API docs](https://firebase.google.com/docs/reference/kotlin/com/google/firebase/ai/package-summary)

## Key Types

- `GenerativeModel` — main entry point for content generation
- `Chat` — multi-turn conversation state
- `Content`, `Part`, `TextPart`, `BlobPart` — content building blocks
- `GenerateContentResponse` — response wrapper
- `GenerationConfig` — temperature, maxOutputTokens, etc.
- `SafetySetting`, `HarmCategory`, `HarmBlockThreshold` — safety filtering
- `CountTokensResponse` — token counting result
- `GenerativeBackend` — backend selection (Google AI / Vertex AI)

## Platform Status

| Platform | Status |
|----------|--------|
| Android | Functional — wraps `com.google.firebase:firebase-ai` |
| iOS | Functional — wraps Firebase iOS SDK via cinterop fork |

## iOS Fork

iOS cinterop depends on a **fork** of `firebase-ios-sdk` that exposes ObjC headers for the Vertex AI module. The fork repo is configured in `build.gradle.kts` via `swiftPMDependencies`.

ObjC prefix: `FIR` (standard Firebase iOS prefix).

## Not Yet Implemented

- Image generation
- Grounding / retrieval
- Audio / video parts
- Code execution
- Cached content
