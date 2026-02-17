plugins {
    id("dev.ynagai.firebase.library")
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.common"
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.firebase.android.common)
        }
        commonMain.dependencies {
            api(libs.kotlinx.datetime)
        }
    }
}
