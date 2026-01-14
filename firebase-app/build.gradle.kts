plugins {
    id("dev.ynagai.firebase.library")
}

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
