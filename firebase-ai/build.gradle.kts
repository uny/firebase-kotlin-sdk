plugins {
    id("dev.ynagai.firebase.library")
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.ai"
    }

    swiftPMDependencies {
        swiftPackage(
            url = url("https://github.com/uny/firebase-objc-sdk.git"),
            version = from(libs.versions.firebase.objc.get()),
            products = listOf(product("FirebaseAILogicObjC")),
        )
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.firebase.android.ai)
            // Hybrid Inference (experimental, no-SLA): on-device fallback for firebase-ai.
            // https://firebase.google.com/docs/ai-logic/hybrid/android/get-started
            implementation(libs.firebase.android.ai.ondevice)
            implementation(libs.kotlinx.serialization.json)
        }
        commonMain.dependencies {
            api(projects.firebaseApp)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
