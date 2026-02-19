# Firebase Kotlin SDK — Task List

Updated: 2026-02-18

Previous evaluation: [evaluation-tasks.md](./evaluation-tasks.md)

---

## firebase-ai

### P0 (Bug / Broken)

| # | Task | Detail | Status |
|---|------|--------|--------|
| AI-1 | `generateContentStream` の例外ラッピング | Android で `generateContent()` は `wrapAndroidException` で例外変換しているが、`generateContentStream()` はラップなし。ストリーミング中に Android SDK 固有の例外がリークする。`GenerativeModel.android.kt:27-33` | DONE |

### P1 (High)

| # | Task | Detail | Status |
|---|------|--------|--------|
| AI-2 | `GenerateContentResponse.functionCalls` プロパティ追加 | `text` と同様に function call 一覧を取得する convenience property がない。現状 `candidates[0].content.parts.filterIsInstance<FunctionCallPart>()` が必要 | DONE |
| AI-3 | `FileDataPart` の追加 | Android SDK の `FileDataPart`（URI/Cloud Storage ファイル参照）が未実装。`Content.kt`, `ContentBuilder.kt`, 各 platform mapper に追加が必要 | DONE |
| AI-4 | `GenerationConfig` に不足パラメータ追加 | `responseModalities`（画像生成時に必要）、`presencePenalty`, `frequencyPenalty` がない | DONE |
| AI-5 | CLAUDE.md の iOS ステータス更新 | CLAUDE.md に "TODO stubs — not functional" とあるが、`GenerativeModel.apple.kt` 等は実装済み。実態と乖離 | DONE |

### P2 (Medium)

| # | Task | Detail | Status |
|---|------|--------|--------|
| AI-6 | `Schema.type` を enum 化 | `String` 型で `"STRING"`, `"INTEGER"` 等の生文字列を使用。typo リスクあり。`enum class SchemaType` にすべき | DONE |
| AI-7 | `ContentBuilder` に `image()` ヘルパー追加 | `text()`, `inlineData()` はあるが、画像用の便利メソッドがない | DONE |
| AI-8 | `ImagenModel` / `imagenModel()` | Android SDK の `FirebaseAI.imagenModel()` による画像生成機能が欠落 | DONE |
| AI-9 | `LiveGenerativeModel` / `LiveSession` | Firebase AI SDK のリアルタイムストリーミング API が未実装 | DONE |

### P3 (Low)

| # | Task | Detail | Status |
|---|------|--------|--------|
| AI-10 | Embeddings API | 未実装 | BLOCKED |
| AI-11 | Audio/video input parts | 未実装 | DONE |

---

## firebase-firestore

### P0 (Bug / Broken)

| # | Task | Detail | Status |
|---|------|--------|--------|
| FS-1 | iOS `getTimestamp()` が `UnsupportedOperationException` | `DocumentSnapshot.apple.kt:38-43` で常に例外を投げる。Firestore の基本機能が壊れている | DONE |
| FS-2 | `Serialization.kt` の `Timestamp` 未処理 | `anyToJsonElement()` に `Timestamp` のケースがない。Timestamp フィールドを含むドキュメントの `toObject<T>()` が壊れる | DONE |

### P1 (High)

| # | Task | Detail | Status |
|---|------|--------|--------|
| FS-3 | 書き込み用シリアライゼーション `encodeToMap()` | `toObject<T>()` の逆。`@Serializable` オブジェクトを `Map<String, Any?>` に変換するヘルパーがなく、`set()` / `update()` / `add()` で毎回手動 Map 構築が必要 | DONE |
| FS-4 | `SnapshotMetadata` の公開 | `DocumentSnapshot.metadata` / `QuerySnapshot.metadata` がない。`hasPendingWrites`, `isFromCache` を確認できない | DONE |
| FS-5 | `DocumentChange` の追加 | `QuerySnapshot.documentChanges` がなく、リアルタイムリスナーで ADDED/MODIFIED/REMOVED の変更種別を判別できない | DONE |
| FS-6 | `SetOptions` の拡張 | `set(data, merge = true)` のみ。`mergeFields(List<String>)` / `mergeFieldPaths()` で部分フィールドマージができない | DONE |

### P2 (Medium)

| # | Task | Detail | Status |
|---|------|--------|--------|
| FS-7 | `FieldPath` サポート | ネストフィールド参照、`FieldPath.documentId()` が使えない。Query の where 条件でドキュメント ID 指定時に必要 | DONE |
| FS-8 | `AggregateQuery` (`count`, `sum`, `average`) | 集計クエリが未実装 | DONE |
| FS-9 | `FirebaseFirestoreSettings` | キャッシュサイズ、永続化設定、カスタムホスト指定等が不可 | DONE |
| FS-10 | `GeoPoint` / `Blob` 型 | Firestore ネイティブ型が欠落 | DONE |
| FS-11 | `CollectionReference.document()` のデフォルト引数確認 | `document(documentPath: String = "")` — 空文字列で自動 ID 生成が正しく動くか要検証 | DONE |

### P3 (Low)

| # | Task | Detail | Status |
|---|------|--------|--------|
| FS-12 | `DocumentSnapshot.getDate()` | `getTimestamp(field)?.toInstant()` で代替可能。専用メソッド不要 | WONTFIX |
| FS-13 | `startAt(DocumentSnapshot)` 等カーソルページネーション | `startAt/startAfter/endAt/endBefore` が `vararg Any` のみ。`DocumentSnapshot` ベースのカーソルページネーションが未実装。実アプリのページネーションで最頻出 | DONE |
| FS-14 | `limitToLast()` | `Query.limit()` はあるが `limitToLast()` がない。降順クエリの末尾 N 件取得に必要 | DONE |

---

## firebase-app

### P2 (Medium)

| # | Task | Detail | Status |
|---|------|--------|--------|
| APP-1 | `FirebaseApp.name` / `FirebaseApp.options` / `FirebaseOptions` | アプリ設定を inspect できない | DONE |
| APP-2 | カスタム `FirebaseApp` 作成 | `FirebaseOptions` からの `FirebaseApp.initializeApp()` がない | DONE |

---

## firebase-auth

### P1 (High)

| # | Task | Detail | Status |
|---|------|--------|--------|
| AUTH-2 | Email Link Sign-In | `sendSignInLinkToEmail`, `isSignInWithEmailLink`, `signInWithEmailLink` の実装。ActionCodeSettings は既存 | DONE |
| AUTH-3 | Phone Auth Verification Flow | `PhoneAuthProvider.verifyPhoneNumber()` の OTP フロー実装。現状 credential 作成のみ | DONE |
| AUTH-4 | Email Action Codes | `checkActionCode`, `applyActionCode`, `confirmPasswordReset` の実装。パスワードリセット・メール検証フロー完成に必要 | DONE |

### P2 (Medium)

| # | Task | Detail | Status |
|---|------|--------|--------|
| AUTH-5 | Additional Auth Providers | Facebook, GitHub, Apple, Twitter 等の `AuthProvider.getCredential()` 追加。現状 Email/Google/Phone/OAuth のみ | DONE |
| AUTH-6 | `sendPasswordResetEmail` ActionCodeSettings オーバーロード | `sendPasswordResetEmail(email, actionCodeSettings)` 追加。カスタムリダイレクト URL 指定が必要なケース対応 | DONE |
| AUTH-7 | `sendEmailVerification` ActionCodeSettings オーバーロード | `FirebaseUser.sendEmailVerification(actionCodeSettings)` 追加 | DONE |
| AUTH-8 | `fetchSignInMethodsForEmail` | メールに紐づくサインイン方法一覧取得。アカウントリンク・リカバリフローに必要 | DONE |

### P3 (Low)

| # | Task | Detail | Status |
|---|------|--------|--------|
| AUTH-9 | Multi-Factor Authentication (MFA) | `MultiFactor`, `MultiFactorSession`, `MultiFactorAssertion` 等。エンタープライズ向け | TODO |
| AUTH-10 | Exception コード拡充 | Android SDK の追加エラーコード対応 (`INVALID_DYNAMIC_LINK`, `MISSING_EMAIL` 等) | TODO |

---

## Cross-cutting

### P1 (High)

| # | Task | Detail | Status |
|---|------|--------|--------|
| X-1 | expect class の `equals` / `hashCode` | `DocumentReference`, `CollectionReference` 等に未定義。common コードでの等値比較が正しく動かない | DONE |
| X-2 | テスト追加 | platform 実装のテストなし。エミュレータ integration test もない | DONE |

### P2 (Medium)

| # | Task | Detail | Status |
|---|------|--------|--------|
| X-3 | `firebase-common` モジュール活用 | `Timestamp` を `firebase-firestore` から `firebase-common` に移動。`toMillis()` / `fromMillis()` 追加。非推奨 typealias で後方互換維持 | DONE (#21) |
| X-4 | `Timestamp` に `toInstant()` / `now()` 追加 | Android Firebase SDK の API に合わせて `toInstant()`, `now()`, `fromInstant()` を追加。`kotlinx-datetime` を依存に導入し commonMain で実装 | DONE |
| X-5 | `Timestamp` の `nanoseconds` バリデーション | `nanoseconds` が `[0, 999_999_999]` の範囲内であることを `init` ブロックで検証。PR #21 CodeRabbit 指摘対応 | DONE (#21) |

---

## Completed (from previous evaluation)

| # | Description | Completed in |
|---|-------------|-------------|
| A-1 | `firebase-app` を `api()` で公開 | evaluation-tasks.md |
| A-2 | `Firebase.firestore` プロパティ追加 | evaluation-tasks.md |
| B-1 | iOS `GenerativeModel` / `Chat` 実装 | #8 |
| B-2 | Function Calling (`Tool`, `FunctionDeclaration`, `ToolConfig`) | #8 |
| B-3 | カスタム例外クラス (`FirebaseAIException` hierarchy) | #8 |
| B-4 | JSON Schema mode (`responseSchema` in `GenerationConfig`) | #8 |
| B-8 | SafetySetting サンプル定数名修正 | evaluation-tasks.md |
| C-1 | `toObject<T>()` / `toObjects<T>()` シリアライゼーション | #8 |
| C-2 | `getField<T>()` type safety | #8 |
| C-3 | Android `getTimestamp()` 実装 | #8 |
| C-4 | `MetadataChanges` / `includeMetadataChanges` | #8 |
| D-2 | AGENTS.md に firebase-firestore 追加 | evaluation-tasks.md |
| D-3 | README に firebase-firestore 追加 | evaluation-tasks.md |
| AUTH-1 | `firebase-auth` モジュール追加 | #25 |
