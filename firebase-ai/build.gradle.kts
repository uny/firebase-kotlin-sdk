plugins {
    id("dev.ynagai.firebase.library")
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.ai"
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.firebase.android.ai)
        }
        commonMain.dependencies {
            implementation(projects.firebaseApp)
        }
    }
}
