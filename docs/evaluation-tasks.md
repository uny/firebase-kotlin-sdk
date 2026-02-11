# Firebase Kotlin SDK Evaluation & Task List

## Project Overview

Firebase Android KTX-compatible API wrapper for KMP (Android + iOS).
Modules: `firebase-app`, `firebase-ai`, `firebase-common`, `firebase-firestore`.

---

## A. Build & API Design (Immediate)

### A-1. `firebase-app` not resolved as transitive dependency [DONE]

Changed `implementation(projects.firebaseApp)` to `api(projects.firebaseApp)` in:
- `firebase-firestore/build.gradle.kts`
- `firebase-ai/build.gradle.kts`

### A-2. `Firebase.firestore` property access [DONE]

Added `expect val Firebase.firestore: FirebaseFirestore` property for default instance access.
Kept `expect fun Firebase.firestore(app: FirebaseApp): FirebaseFirestore` for custom app.

---

## B. firebase-ai Module

### Status

| Feature | Android | iOS |
|---------|---------|-----|
| Text generation | OK | TODO stub |
| Streaming | OK | TODO stub |
| Multi-turn Chat | OK | TODO stub |
| Token count | OK | TODO stub |
| Safety settings | OK | N/A |
| GenerationConfig DSL | OK | N/A |
| Backend selection | OK | Partial |

### P0 (Critical)

| # | Description | Status |
|---|-------------|--------|
| B-1 | iOS implementation — `GenerativeModel.apple.kt`, `Chat.apple.kt` are all `TODO()` stubs | TODO |

### P1 (High)

| # | Description | Status |
|---|-------------|--------|
| B-2 | Function Calling / Tool Use — `Tool`, `FunctionDeclaration`, `ToolConfig` not wrapped | TODO |
| B-3 | Error handling — no custom exception classes | TODO |
| B-4 | JSON Schema mode (responseSchema) | TODO |

### P2 (Medium)

| # | Description | Status |
|---|-------------|--------|
| B-5 | Embeddings API | TODO |
| B-6 | File API (`FileDataPart` / upload) | TODO |
| B-7 | Enum mapping fallback is silent | TODO |
| B-8 | README SafetySetting sample constant names | DONE |

### P3 (Low)

| # | Description | Status |
|---|-------------|--------|
| B-9 | Audio/video input | TODO |
| B-10 | Model listing API | TODO |
| B-11 | Additional GenerationConfig parameters | TODO |

---

## C. firebase-firestore Module

### Status

| Feature | Android | iOS |
|---------|---------|-----|
| CRUD (get/set/update/delete) | OK | OK |
| Query (where/orderBy/limit/pagination) | OK | OK |
| Transaction | OK | OK |
| Batch write | OK | OK |
| Real-time listener (Flow) | OK | OK |
| Collection group | OK | OK |
| Emulator | OK | OK |
| FieldValue | OK | OK |

### P1 (High)

| # | Description | Status |
|---|-------------|--------|
| C-1 | kotlinx-serialization data class support (`toObject<T>()` / `toObjects<T>()`) | TODO |
| C-2 | `getField<T>()` type safety — silent `as?` cast failure | TODO |
| C-3 | `DocumentSnapshot.getTimestamp()` | TODO |
| C-4 | Listener options (`MetadataChanges` / `includeMetadataChanges`) | TODO |

### P2 (Medium)

| # | Description | Status |
|---|-------------|--------|
| C-5 | `GeoPoint`, `Blob` type wrappers | TODO |
| C-6 | `@Suppress("UNCHECKED_CAST")` in 3 places | TODO |
| C-7 | Apple `CollectionReference.add()` callback fragility | TODO |
| C-8 | Query validation | TODO |

### P3 (Low)

| # | Description | Status |
|---|-------------|--------|
| C-9 | `Query.count()` (Aggregation Query) | TODO |
| C-10 | `Query.aggregate()` (sum/average) | TODO |
| C-11 | Firestore Settings API (cache size, etc.) | TODO |

---

## D. Cross-cutting

### P1 (High)

| # | Description | Status |
|---|-------------|--------|
| D-1 | Tests — no unit/integration tests exist | TODO |
| D-2 | AGENTS.md missing firebase-firestore | DONE |
| D-3 | README missing firebase-firestore | DONE |

### P2 (Medium)

| # | Description | Status |
|---|-------------|--------|
| D-4 | `firebase-common` module nearly empty | TODO |
| D-5 | Custom `firebase-objc-sdk` fork dependency | TODO |
| D-6 | Non-standard Kotlin version (`2.2.21-titan-211`) | TODO |

---

## Overall Assessment

**Architecture: Good** — expect/actual pattern, DSL builders, Flow-based real-time listeners follow KMP best practices.

**firebase-ai: Android-only production ready** — iOS is entirely unimplemented. Function Calling and other major features missing.

**firebase-firestore: Both platforms functional** — CRUD, queries, transactions, listeners all work. Missing serialization support hurts UX.

**Quality assurance: Insufficient** — No tests exist.
