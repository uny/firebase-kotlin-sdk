import dev.ynagai.firebase.convention.configureAppleBridge

plugins {
    id("dev.ynagai.firebase.library")
}

configureAppleBridge(schemeName = "FirebaseAppBridge")

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.app"
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.firebase.android.common)
        }
    }
}
