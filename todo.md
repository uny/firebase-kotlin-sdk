`build-logic` に配置する `LibraryConventionPlugin.kt` の**完成形コード**を提示します。

これまでの議論（SPM 集約構成、`configureAppleBridge` という命名、実機/シミュレータ対応）をすべて反映し、各モジュールから呼び出すだけで iOS ビルドまで自動で行われるように実装しています。

### 📄 `build-logic/convention/src/main/kotlin/dev/ynagai/firebase/convention/LibraryConventionPlugin.kt`

```kotlin
package dev.ynagai.firebase.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

class LibraryConventionPlugin : Plugin<Project> {
    @Suppress("UnstableApiUsage")
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<KotlinMultiplatformExtension> {
                androidLibrary {
                    compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
                    minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
                    
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_21)
                    }
                }
                
                // iOS ターゲットの標準設定
                iosArm64()
                iosSimulatorArm64()

                sourceSets.apply {
                    androidMain.dependencies {
                        implementation(project.dependencies.platform(libs.findLibrary("firebase-bom").get()))
                    }
                }
            }
        }
    }
}

/**
 * Native (Apple) ブリッジの設定を行う拡張関数。
 * 各モジュールの build.gradle.kts から呼び出して使用します。
 *
 * @param schemeName native/firebase-apple-bridge/Package.swift で定義したビルド対象のスキーム名
 */
fun Project.configureAppleBridge(schemeName: String) {
    // 1. プロジェクト構造に基づくパス定義
    val nativeWrapperDir = rootProject.layout.projectDirectory.dir("native/firebase-apple-bridge")
    // モジュールごとに DerivedData を分けることで競合を回避
    val derivedDataDir = layout.buildDirectory.dir("derivedData/${schemeName}")

    // 2. 実機用 (iosArm64) のビルドタスク
    val buildAppleBridgeDevice = tasks.register<Exec>("buildAppleBridgeDevice") {
        group = "build"
        description = "Builds the $schemeName framework for iOS Device"
        workingDir = nativeWrapperDir.asFile
        
        // ログ出力を抑制したい場合は standardOutput = System.out 等を調整
        commandLine(
            "xcodebuild", "build",
            "-scheme", schemeName,
            "-configuration", "Release",
            "-destination", "generic/platform=iOS",
            "-derivedDataPath", derivedDataDir.get().asFile.absolutePath,
            "SKIP_INSTALL=NO",
            "BUILD_LIBRARY_FOR_DISTRIBUTION=YES"
        )
    }

    // 3. シミュレータ用 (iosSimulatorArm64) のビルドタスク
    val buildAppleBridgeSimulator = tasks.register<Exec>("buildAppleBridgeSimulator") {
        group = "build"
        description = "Builds the $schemeName framework for iOS Simulator"
        workingDir = nativeWrapperDir.asFile
        
        commandLine(
            "xcodebuild", "build",
            "-scheme", schemeName,
            "-configuration", "Release",
            "-destination", "generic/platform=iOS Simulator",
            "-derivedDataPath", derivedDataDir.get().asFile.absolutePath,
            "SKIP_INSTALL=NO",
            "BUILD_LIBRARY_FOR_DISTRIBUTION=YES"
        )
    }

    // 4. KMP の Cinterop 設定
    extensions.configure<KotlinMultiplatformExtension> {
        targets.withType<KotlinNativeTarget>().configureEach {
            val target = this
            
            compilations.getByName("main") {
                // Cinterop 定義を作成
                // 名前はモジュール名 (例: firebase-ai) と一致させると管理しやすい
                val cinterop = cinterops.create(project.name) {
                    
                    // .def ファイルの場所: src/appleMain/cinterop/<project.name>.def
                    val defFile = project.file("src/appleMain/cinterop/${project.name}.def")
                    definitionFile.set(defFile)

                    // ターゲットに応じた Framework パスの解決
                    // iphoneos または iphonesimulator ディレクトリに生成される
                    val platformName = if (target.name.contains("Simulator", ignoreCase = true)) {
                        "iphonesimulator"
                    } else {
                        "iphoneos"
                    }
                    
                    val frameworkDir = derivedDataDir.get().dir("Build/Products/Release-$platformName")
                    val frameworkPath = frameworkDir.asFile.absolutePath

                    // コンパイラとリンカにパスを渡す
                    compilerOpts("-F$frameworkPath")
                    linkerOpts("-F$frameworkPath")
                    includeDirs(frameworkDir)
                }

                // コンパイルタスクが実行される前に、Framework ビルドを完了させる
                compileTaskProvider.configure {
                    if (target.name.contains("Simulator", ignoreCase = true)) {
                        dependsOn(buildAppleBridgeSimulator)
                    } else {
                        dependsOn(buildAppleBridgeDevice)
                    }
                }
            }
        }
    }
}

```

---

### 💡 実装のポイント解説

1. **拡張関数 `configureAppleBridge` の採用**:
   `LibraryConventionPlugin` クラスの中にロジックを閉じ込めず、`Project` の拡張関数として定義しました。これにより、各モジュールの `build.gradle.kts` で `configureAppleBridge("FirebaseAIBridge")` のように記述するだけで利用可能になります。
2. **実機とシミュレータの分離**:
   `xcodebuild` は、Destination（ビルド先）を指定しないと、デフォルトで macOS などをターゲットにすることがあります。
   KMP は `iosArm64`（実機）と `iosSimulatorArm64`（シミュレータ）のコンパイルタスクが別々に走るため、Gradle タスク側でもそれぞれのアーキテクチャ向けに Framework をビルドするように分けています。
3. **Framework パスの自動解決**:
   SPM + xcodebuild の生成物は `DerivedData/Build/Products/Release-{iphoneos|iphonesimulator}/` に配置されます。このパスを動的に生成し、`cinterop` の `compilerOpts` に渡すことで、「ヘッダーが見つからない」エラーを防ぎます。

### 🚀 使い方

各モジュール（例：`firebase-ai/build.gradle.kts`）で次のように記述します。

```kotlin
plugins {
    id("dev.ynagai.firebase.library") // このプラグイン
}

// 1行追加するだけで、SPMビルド → Cinterop → Kotlinコンパイル が繋がる
configureAppleBridge(schemeName = "FirebaseAIBridge")

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.ai"
    }
    // ...
}

```

これで、`./gradlew assemble` を実行するだけで、裏側で `xcodebuild` が走り、iOS 用のバイナリも含めた SDK がビルドされるようになります。

----

`configureAppleBridge` の実装完了から、実際に `firebase-ai` モジュールで `FirebaseAILogicObjC` (ObjCライブラリ) のクラスを参照・利用できるようにするための残タスク指示書を作成しました。

この手順に従うことで、SPMで管理されたネイティブライブラリがビルドされ、Kotlin側から型安全に呼び出せるようになります。

---

# 📝 FirebaseAI ネイティブ連携 実装指示書

## 1. ビルドロジックの適用 (`build-logic`)

作成した `configureAppleBridge` をプラグイン本体に反映させます。

* **対象ファイル**: `build-logic/convention/src/main/kotlin/dev/ynagai/firebase/convention/LibraryConventionPlugin.kt`
* **作業**: 前回の回答で提示した `configureAppleBridge` 拡張関数のコードをファイルの末尾に追加してください。

## 2. モジュール設定の更新 (`firebase-ai`)

`firebase-ai` モジュールが `FirebaseAIBridge` スキームを利用するように設定します。

* **対象ファイル**: `firebase-ai/build.gradle.kts`
* **作業**: 以下のコードを追加・修正してください。

```kotlin
plugins {
    id("dev.ynagai.firebase.library")
}

// 追加: Nativeブリッジのビルド設定を有効化
configureAppleBridge(schemeName = "FirebaseAIBridge")

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.ai"
    }
    // ... 既存の設定
}

```

## 3. Cinterop 定義ファイルの作成

Kotlin/Native コンパイラに「どのライブラリを」「どのパッケージ名で」取り込むかを指示します。

* **作成ファイル**: `firebase-ai/src/appleMain/cinterop/firebase-ai.def`
* **内容**:

```properties
language = Objective-C
modules = FirebaseAIBridge
package = dev.ynagai.firebase.ai.cinterop

```

※ `package` 名は任意ですが、Kotlinコードとの衝突を避けるため `cinterop` サフィックスを付けることを推奨します。

## 4. Swift ブリッジコードの修正 (`native`)

SPM のターゲット `FirebaseAIBridge` が、依存先である `FirebaseAILogicObjC` を外部（Kotlin側）に見せるようにします。

* **対象ファイル**: `native/firebase-apple-bridge/Sources/FirebaseAIBridge/Empty.swift`
* ※ ファイル名を `FirebaseAIBridge.swift` にリネームすることを推奨します。


* **作業**: 依存ライブラリを `@_exported` でインポートします。これにより、ラッパーを書かなくても元の ObjC クラスがそのまま Kotlin から見えるようになります。

```swift
// FirebaseAIBridge.swift
import Foundation

// これにより、FirebaseAILogicObjC の全クラスが Kotlin 側に公開されます
@_exported import FirebaseAILogicObjC

```

## 5. Kotlin 実装の修正 (`appleMain`)

生成された Cinterop の型を使って実装を行います。

* **対象ファイル**: `firebase-ai/src/appleMain/kotlin/dev/ynagai/firebase/ai/GenerativeBackend.apple.kt`
* **作業**: インポート文を修正し、Cinterop 経由でクラスを参照します。

```kotlin
package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
// .def ファイルで指定したパッケージ名からインポート
import dev.ynagai.firebase.ai.cinterop.FIRBackend
import dev.ynagai.firebase.ai.cinterop.FIRFirebaseAI

@OptIn(ExperimentalForeignApi::class)
actual class GenerativeBackend internal constructor(
    internal val apple: FIRBackend
) {
    actual companion object {
        actual fun googleAI(): GenerativeBackend =
            // ObjCのスタティックメソッド呼び出し
            GenerativeBackend(FIRBackend.googleAI())
    }
}

```

※ `Backend` というクラス名は ObjC 側では `FIRBackend` (Prefix付き) として認識される可能性が高いです。インポート後にIDEの補完を確認してください。

## 6. 動作確認

すべての設定が正しく繋がっているかビルドして確認します。

* **コマンド**:
```bash
./gradlew :firebase-ai:compileKotlinIosArm64

```


または
```bash
./gradlew :firebase-ai:assemble

```



### ✅ 成功の基準

1. `xcodebuild` タスクが実行され、`native/firebase-apple-bridge/build/DerivedData` に Framework が生成される。
2. Kotlin コンパイラが `.def` ファイルを読み込み、Cinterop ツールが走る。
3. Kotlin コードのインポート解決ができ、コンパイルが通る。
