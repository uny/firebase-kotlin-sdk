import dev.ynagai.firebase.convention.configureAppleBridge

plugins {
    id("dev.ynagai.firebase.library")
}

configureAppleBridge(schemeName = "FirebaseAIBridge")

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
