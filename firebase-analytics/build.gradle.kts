plugins {
    id("dev.ynagai.firebase.library")
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.analytics"
    }

    swiftPMDependencies {
        swiftPackage(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from(libs.versions.firebase.apple.get()),
            products = listOf(product("FirebaseAnalytics")),
        )
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.firebase.android.analytics)
        }
        commonMain.dependencies {
            api(projects.firebaseApp)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
