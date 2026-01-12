Gradle Wrapper（v9.2.1）の導入が完了した前提で、`dev.ynagai.firebase` プロジェクトの基盤となる `build-logic` の構築から `firebase-common` の実装までの手順を整理して提示します。

この構成は、将来的な「iOS導入の摩擦解消」や「AIネイティブ対応」という戦略的目標を支えるための土台となります。

---

### 🛠 `firebase-common` 実装までのロードマップ

#### ステップ 1: ルート設定ファイルの構築

プロジェクト全体の構造とプラグイン管理を定義します。

**1. `settings.gradle.kts` (Root)**

```kotlin
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "uny-firebase-kotlin-sdk"

include(
    ":firebase-common",
)

```

**2. `gradle/libs.versions.toml**`
依存関係を中央管理します。

```toml
[versions]
kotlin = "2.1.0"
androidGradlePlugin = "8.7.0"
kotlinxCoroutines = "1.10.1"

[libraries]
kotlin-gradlePlugin = { group = "org.jetbrains.kotlin", name = "kotlin-gradle-plugin", version.ref = "kotlin" }
android-gradlePlugin = { group = "com.android.tools.build", name = "gradle", version.ref = "androidGradlePlugin" }
kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "kotlinxCoroutines" }

[plugins]
# カスタムプラグイン適用のための定義はここでは行わず、build-logicで直接指定します

```

---

#### ステップ 2: `build-logic` (Convention Plugins) の構築

共通のビルド規約を定義します。

**1. ディレクトリ作成**

```text
build-logic/
├── settings.gradle.kts
├── convention/
│   ├── build.gradle.kts
│   └── src/main/kotlin/dev/ynagai/firebase/convention/
│       └── FirebaseMultiplatformConventionPlugin.kt

```

**2. `build-logic/settings.gradle.kts**`

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

```

**3. `build-logic/convention/build.gradle.kts**`

```kotlin
plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("firebaseMultiplatform") {
            id = "dev.ynagai.firebase.multiplatform"
            implementationClass = "dev.ynagai.firebase.convention.FirebaseMultiplatformConventionPlugin"
        }
    }
}

```

**4. `FirebaseMultiplatformConventionPlugin.kt**`

```kotlin
package dev.ynagai.firebase.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FirebaseMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
            }

            extensions.configure(KotlinMultiplatformExtension::class.java) {
                androidTarget {
                    publishLibraryVariants("release")
                }
                iosX64()
                iosArm64()
                iosSimulatorArm64()
            }
        }
    }
}

```

ご提示いただいたコードは、Googleの新しいKMPプラグイン (`com.android.kotlin.multiplatform.library`) を使用する際の**正しいアプローチ**です。

`androidTarget` が廃止され、`kotlin { }` ブロックの中に直接 Android の設定を書く形（`androidLibrary { }`）に変わりました。

ご要望の「設定値の解説」と「最適な設定（Best Practice）」を含めた、完成形のコードを提示します。

### 最適化された Convention Plugin

以下の修正・追加を行っています。

1. **Version Catalogの利用**: `35` などをハードコードせず、`libs` から取得するように変更（保守性向上）。
2. **JVMターゲットの明示**: Java/Kotlinのバイトコード互換性を保証。
3. **公開バリアントの指定**: ライブラリとして配布する際の無駄を省く設定。
4. **`withJava()` の制御**: ビルド速度向上のための設定。

```kotlin
package dev.ynagai.firebase.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                // Google製 Android KMP ライブラリ用プラグイン
                apply("com.android.kotlin.multiplatform.library")
                // Jetbrains製 KMP プラグイン
                apply("org.jetbrains.kotlin.multiplatform")
            }

            // Version Catalogの取得 (libs.versions.toml)
            val libs = extensions.getByType<org.gradle.accessors.dm.LibrariesForLibs>()

            extensions.configure<KotlinMultiplatformExtension> {
                // 新しいプラグインでは androidTarget() ではなく androidLibrary {} を使用します
                // ※ 最新のAGP/KGPでは android {} ブロックもサポートされていますが、
                //    KMPライブラリプラグインの文脈では androidLibrary {} が明示的です。
                androidLibrary {
                    // 【必須】コンパイルSDKバージョン
                    compileSdk = libs.versions.android.compileSdk.get().toInt()
                    
                    // 【推奨】最小SDKバージョン
                    minSdk = libs.versions.android.minSdk.get().toInt()

                    // 【最適化】Javaソースのコンパイル設定
                    // 新プラグインはデフォルトでJavaコンパイルが無効（ビルド高速化のため）。
                    // 既存のJavaコードがある場合や、Javaのみのライブラリを使う場合にのみ有効化します。
                    // withJava() 

                    // 【最適化】JVMターゲットの設定
                    // KotlinとJavaのバイトコードバージョンを揃えます (例: Java 11 or 17)
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_11)
                    }
                    
                    // 【最適化】公開バリアントの設定
                    // ライブラリ利用者には "release" ビルドのみを公開し、
                    // 無駄な "debug" ビルドの依存解決を防ぎます。
                    publishLibraryVariants("release")
                }
                
                // 共通の依存関係などがあればここに記述可能
                sourceSets.commonMain.dependencies {
                     // implementation(libs.kotlinx.coroutines.core)
                }
            }
        }
    }
}

```

### 設定項目の詳細解説

#### 1. `androidLibrary { ... }`

これが新しいプラグインの核となる設定ブロックです。従来の `android { ... }` (AGP拡張) と `kotlin { androidTarget() }` (KGP拡張) が統合されたイメージです。

* **注意**: `namespace`（パッケージ名）は、Convention Plugin ではなく**各モジュールの `build.gradle.kts` で設定する**のが一般的です（モジュールごとに一意である必要があるため）。

#### 2. `withJava()`

新しいプラグインの大きな特徴です。

* **デフォルト**: 無効（Javaのコンパイルタスクが走らないため、ビルドが高速です）。
* **設定**: Javaで書かれたコードがプロジェクトに含まれている場合や、一部の古いツールチェーンが必要な場合は `withJava()` を記述して有効化します。純粋なKotlinプロジェクトなら書かないのがベストです。

#### 3. `publishLibraryVariants("release")`

KMPライブラリにおいて非常に重要です。

* 通常、Androidライブラリは `debug` と `release` の両方を作りますが、ライブラリとして他のモジュールやアプリから参照される際、明示しないと `debug` 版が使われてしまうなどのトラブルが起きがちです。
* `release` のみを公開することで、コンシューマー（利用者）側の依存解決をシンプルにし、ビルドサイズやパフォーマンスを安定させます。

#### 4. `compileSdk` / `minSdk`

これらはトップレベルの `android {}` ブロックから、この `androidLibrary {}` ブロック内に移動しました。Convention Plugin で一元管理し、Version Catalog (`libs`) から値を引く運用が最も堅牢です。

### 各モジュール (`build.gradle.kts`) の記述イメージ

Convention Plugin 適用後の、各モジュール側は非常にシンプルになります。

```kotlin
plugins {
    id("dev.ynagai.firebase.multiplatform.library") // 作成したプラグイン
}

kotlin {
    androidLibrary {
        // モジュール固有の名前空間はここで指定
        namespace = "dev.ynagai.firebase.core"
    }
}

```

---

#### ステップ 3: `firebase-common` の実装

SDK の基盤となるコードを実装します。

**1. ディレクトリとビルド設定**

```text
firebase-common/
├── build.gradle.kts
└── src/commonMain/kotlin/dev/ynagai/firebase/

```

**2. `firebase-common/build.gradle.kts**`

```kotlin
plugins {
    id("dev.ynagai.firebase.multiplatform")
}

group = "dev.ynagai"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "dev.ynagai.firebase.common"
}

```

**3. コアコードの実装 (`commonMain`)**
AI が理解しやすい構造化された設計を導入します。

* **`exceptions/FirebaseException.kt`**:
```kotlin
package dev.ynagai.firebase.common.exceptions

open class FirebaseException(
    val code: String,
    message: String,
    cause: Throwable? = null
) : Exception("[$code] $message", cause)

```


* **`Firebase.kt`**:
```kotlin
package dev.ynagai.firebase

object Firebase {
    val app: FirebaseApp
        get() = TODO("Platform implementation required")
}

```


* **`FirebaseApp.kt`**:
```kotlin
package dev.ynagai.firebase

expect class FirebaseApp

```



---

### ✅ 完了後の確認

すべてのファイルを作成後、ルートディレクトリで以下を実行してビルドが通るか確認してください。

```bash
./gradlew help

```

成功すれば、`dev.ynagai.firebase.multiplatform` プラグインが正常に `firebase-common` に適用され、KMP SDK 開発の第一歩が完了します。

https://qiita.com/hiro404/items/485c5b56f90fd49c0979
