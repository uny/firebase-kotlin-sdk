モジュールが増えていくことを想定した場合、**「SPM（Package.swift）は1箇所に集約し、ターゲット（Scheme）でモジュールごとの生成物を切り分ける」** という構成が最も管理しやすく、ビルド効率も良いです。

各Gradleモジュール（`firebase-ai`, `firebase-auth`など）ごとに `Package.swift` を分散させてしまうと、Firebase SDK全体のバージョン不整合（例：Coreはv10.0だがAIはv11.0を参照している等）が起きやすく、管理が破綻します。

以下に、スケーラビリティを考慮した推奨構成を提示します。

---

### 📂 推奨ディレクトリ構成

プロジェクトルート直下の `native/` ディレクトリを、**ネイティブ依存関係の「コントロールセンター」** として機能させます。

```text
uny-firebase-kotlin-sdk/
├── native/
│   └── firebase-wrapper/        <-- ここに全モジュール分の定義を集約
│       ├── Package.swift        <-- 依存関係（firebase-objc-sdkなど）を一元管理
│       └── Sources/
│           ├── FirebaseAIWrapper/    <-- AI用のダミーソース（必要な場合）
│           └── FirebaseAppWrapper/   <-- App(Core)用のダミーソース
│
├── build-logic/                 <-- Gradleプラグインでビルドロジックを共通化
│
├── firebase-ai/
│   ├── build.gradle.kts         <-- "FirebaseAIWrapper" スキームを指定してビルド
│   └── src/appleMain/cinterop/
│
├── firebase-app/
│   ├── build.gradle.kts         <-- "FirebaseAppWrapper" スキームを指定してビルド
│   └── src/appleMain/cinterop/
│
└── ... (将来のモジュール: firebase-auth, firebase-firestore等)

```

---

### 🛠️ 1. 集約型 Package.swift の作成

`native/firebase-wrapper/Package.swift` で、将来増えるモジュールごとのターゲット（Target）を定義します。これにより、必要なライブラリだけをFramework化できるようになります。

```swift
// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "FirebaseWrapper",
    platforms: [.iOS(.v15)],
    products: [
        // Gradleから指定するビルド対象（ライブラリ）
        .library(name: "FirebaseAppWrapper", targets: ["FirebaseAppWrapper"]),
        .library(name: "FirebaseAIWrapper", targets: ["FirebaseAIWrapper"]),
        // 将来追加: .library(name: "FirebaseAuthWrapper", targets: ["FirebaseAuthWrapper"]),
    ],
    dependencies: [
        // バージョン管理はここで一元化されます
        .package(url: "https://github.com/uny/firebase-objc-sdk.git", branch: "main")
    ],
    targets: [
        // FirebaseApp (Core) 用のラッパー
        .target(
            name: "FirebaseAppWrapper",
            dependencies: [
                .product(name: "FirebaseCoreObjC", package: "firebase-objc-sdk")
            ]
        ),
        // FirebaseAI 用のラッパー
        .target(
            name: "FirebaseAIWrapper",
            dependencies: [
                .product(name: "FirebaseAILogicObjC", package: "firebase-objc-sdk"),
                // 必要であればターゲット間依存も定義可能
                // "FirebaseAppWrapper" 
            ]
        )
    ]
)

```

※ `Sources/FirebaseAppWrapper/dummy.swift` など、各ターゲット用フォルダに空のファイルを置く必要がある場合があります。

---

### 🛠️ 2. Build Logic (Convention Plugin) の拡張

各モジュールの `build.gradle.kts` に毎回 `xcodebuild` タスクを書くのは冗長です。
`build-logic` 内に**「スキーム名を渡せばFrameworkを作ってくれる機能」**を実装します。

**`build-logic/.../FirebaseNativeConventionPlugin.kt` (イメージ)**

```kotlin
// 拡張関数やプラグインとして実装
fun Project.configureNativeBuild(schemeName: String) {
    val nativeDir = rootProject.file("native/firebase-wrapper")
    val buildDir = nativeDir.resolve("build/DerivedData/${project.name}")

    // SPMビルドタスク
    val buildTask = tasks.register<Exec>("buildWrapperFramework") {
        workingDir = nativeDir
        commandLine(
            "xcodebuild", "build",
            "-scheme", schemeName, // ← ここでモジュールごとのターゲットを切り替え
            "-configuration", "Release",
            "-destination", "generic/platform=iOS",
            "-derivedDataPath", buildDir.absolutePath,
            "SKIP_INSTALL=NO",
            "BUILD_LIBRARY_FOR_DISTRIBUTION=YES"
        )
    }

    // Cinterop設定の自動化
    kotlin {
        targets.withType<KotlinNativeTarget>().configureEach {
            compilations.getByName("main") {
                cinterops.create(project.name) { // cinterop名をモジュール名に
                    val frameworkPath = buildDir.resolve("Build/Products/Release-iphoneos")
                    
                    // defファイルの場所規約を決めておくと楽です
                    defFile(file("src/appleMain/cinterop/${project.name}.def"))
                    
                    compilerOpts("-F$frameworkPath")
                    linkerOpts("-F$frameworkPath")
                }
                compileTaskProvider.configure { dependsOn(buildTask) }
            }
        }
    }
}

```

---

### 🛠️ 3. 各モジュールでの利用

これにより、各モジュールの `build.gradle.kts` は非常にシンプルになり、新しいモジュールを追加する際も数行で済みます。

**`firebase-ai/build.gradle.kts`**

```kotlin
plugins {
    id("dev.ynagai.firebase.library")
    // Native用プラグインがあれば適用
}

// 拡張関数を呼び出し
configureNativeBuild(schemeName = "FirebaseAIWrapper")

```

**`firebase-app/build.gradle.kts`**

```kotlin
configureNativeBuild(schemeName = "FirebaseAppWrapper")

```

---

### 🌟 この構成のメリット

1. **バージョン整合性の保証**:
   `firebase-ai` も `firebase-auth` も、全てのネイティブ依存関係は `native/firebase-wrapper/Package.resolved` という単一のファイルで管理されます。「Authだけ古いバージョンのFirebase Coreを参照していた」といった事故を防げます。
2. **ビルド時間の短縮**:
   Gradleからは必要なスキーム（例: `FirebaseAIWrapper`）だけを指定して `xcodebuild` を叩くため、関係ないモジュール（例: Firestoreなど）のビルドが走りません。
3. **モジュール追加が容易**:
   新しい機能を追加する際は、
1. `native/firebase-wrapper/Package.swift` にターゲットを追加
2. 新しいGradleモジュールで `configureNativeBuild("NewTarget")` を呼ぶ
   だけで完了します。



この構成であれば、今後モジュールが数十個に増えても `native` フォルダ以外が散らかることなく、整然と管理できます。