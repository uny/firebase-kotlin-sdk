plugins {
    id("dev.ynagai.firebase.library")
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.ai"
    }

    swiftPMDependencies {
        `package`(
            url = url("https://github.com/uny/firebase-objc-sdk.git"),
            version = from(libs.versions.firebase.objc.get()),
            products = listOf(product("FirebaseAILogicObjC")),
        )
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.firebase.android.ai)
        }
        commonMain.dependencies {
            implementation(projects.firebaseApp)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
