# firebase-objc-sdk 引き継ぎ事項

firebase-kotlin-sdk 側で firebase-ios-sdk 12.12.0 / firebase-android-sdk 34.11.0 対応を行いました。
以下は firebase-objc-sdk 側で対応が必要な項目です。

## 必須: `KFBExecutableCodeLanguage` に `isEqual` と `rawValue` を追加

**現状:** `KFBExecutableCodeLanguage` は `isEqual(_:)` をオーバーライドしておらず、`rawValue` プロパティもない。  
`static var python` は computed property のため、呼び出すたびに新しいインスタンスが生成され、`==` によるポインタ比較が失敗する。

**Kotlin 側の回避策:** 言語を `"PYTHON"` にハードコードしている（`Content.apple.kt:72`）。

**対応案:** 他の enum-like ラッパー（`KFBCodeExecutionOutcome` など）と同様に `isEqual` と `hash` をオーバーライドする。さらに `rawValue: String` を公開すると、Kotlin 側で `rawValue()` ベースのマッピングに統一できる。

```swift
// ExecutableCodePart.swift — KFBExecutableCodeLanguage
@objc public var rawValue: String {
    String(describing: value)
}

override public func isEqual(_ object: Any?) -> Bool {
    guard let other = object as? ExecutableCodeLanguage else { return false }
    return value == other.value
}

override public var hash: Int {
    String(describing: value).hashValue
}
```

## 推奨: `KFBURLRetrievalStatus` に `rawValue` を追加

**現状:** `isEqual` はオーバーライド済みなので `==` 比較は動作する。しかし他の enum-like ラッパー（`KFBHarmCategory`, `KFBContentModality` 等）は `rawValue` を公開している。`KFBURLRetrievalStatus` は未対応。

**Kotlin 側の現状:** `==` 比較で動作しているが、`rawValue()` パターンと不統一。

**対応案:**
```swift
// URLMetadata.swift — KFBURLRetrievalStatus
@objc public var rawValue: String {
    String(describing: value)
}
```

## 推奨: `KFBCodeExecutionOutcome` に `rawValue` を追加

**現状:** `isEqual` はオーバーライド済み。`rawValue` は未公開。

**Kotlin 側の現状:** `==` 比較で動作しているが、他の enum 系と不統一。

**対応案:** URLRetrievalStatus と同様に `rawValue` を追加。

## 参考: Kotlin 側で新たにマッピングしている KFB 型一覧

以下の型は firebase-kotlin-sdk で新たに使用開始したものです。cinterop 経由のマッピングが正しく動作することを確認済みですが、参考として記載します。

| KFB 型 | Kotlin 側マッピングファイル | 備考 |
|--------|---------------------------|------|
| `KFBThinkingConfig`, `KFBThinkingLevel` | `GenerationConfig.apple.kt` | `rawValue` 不要（Kotlin→iOS 方向のみ） |
| `KFBGroundingMetadata`, `KFBGroundingChunk`, `KFBGroundingSupport` | `GenerateContentResponse.apple.kt` | iOS→Kotlin 方向。プロパティアクセスのみ |
| `KFBURLContextMetadata`, `KFBURLMetadata` | `GenerateContentResponse.apple.kt` | iOS→Kotlin 方向 |
| `KFBContentModality`, `KFBModalityTokenCount` | `GenerateContentResponse.apple.kt` | `rawValue()` 使用（既に公開済み） |
| `KFBExecutableCodePart`, `KFBCodeExecutionResultPart` | `Content.apple.kt` | 上記の問題あり |
| `KFBCodeExecutionOutcome` | `Content.apple.kt` | `isEqual` 済み、`rawValue` 推奨 |
| `KFBAudioTranscriptionConfig` | `LiveMapper.apple.kt` | 空コンストラクタで使用 |
| `KFBTool.googleSearchDefault()` | `Tool.apple.kt` | 正常動作 |
| `KFBTool.urlContext()` | `Tool.apple.kt` | 正常動作 |
| `KFBTool.codeExecution()` | `Tool.apple.kt` | 正常動作 |

## 対応不要

以下は firebase-kotlin-sdk 側で「共通 API に含めない」と判断した項目です。firebase-objc-sdk 側での追加作業は不要です。

- **`KFBGenerativeModelSession`** — iOS 専用（Foundation Models）。共通 KMP API に載せられない
- **`JsonSchema` / `generateObject`** — Android のみ。iOS 側未実装のため共通 API 化不可
- **`LiveAudioConversationConfig`** — Android 専用（`AudioRecord`/`AudioTrack` 依存）
