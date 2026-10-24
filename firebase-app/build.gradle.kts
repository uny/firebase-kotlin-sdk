import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("dev.ynagai.firebase.library")
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.app"
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
            products = listOf(product("FirebaseCore")),
        )
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.firebase.android.common)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
