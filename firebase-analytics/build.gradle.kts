import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("dev.ynagai.firebase.library")
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.analytics"
    }

    // No official Firebase client exists for JVM; jvmMain is a fake for desktop previews and tests.
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    swiftPMDependencies {
        swiftPackage(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from(libs.versions.firebase.apple.get()),
            // FirebaseAnalyticsCore は広告 ID(IDFA)を収集しない Analytics 本体。
            // FIRAnalytics API は FirebaseAnalytics と同一。広告 ID 収集(アトリビューション等)が
            // 必要な場合は firebase-analytics-advertising を追加でリンクする。
            products = listOf(product("FirebaseAnalyticsCore")),
        )
    }

    sourceSets {
        androidMain.dependencies {
            // 広告 ID(AAID)の収集を無効化するため play-services-ads-identifier を除外する
            // （iOS 側で IdentitySupport を外すのと対称）。収集が必要な場合は
            // firebase-analytics-advertising が再導入する。
            implementation(libs.firebase.android.analytics.get().module.toString()) {
                exclude(group = "com.google.android.gms", module = "play-services-ads-identifier")
            }
        }
        commonMain.dependencies {
            api(projects.firebaseApp)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
