plugins {
    id("dev.ynagai.firebase.library")
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.app"
    }

    swiftPMDependencies {
        `package`(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from(libs.versions.firebase.apple.get()),
            products = listOf(product("FirebaseCore")),
        )
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.firebase.android.common)
        }
    }
}
