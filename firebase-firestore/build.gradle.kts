plugins {
    id("dev.ynagai.firebase.library")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.firestore"
        withDeviceTestBuilder {
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    swiftPMDependencies {
        `package`(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from(libs.versions.firebase.apple.get()),
            products = listOf(product("FirebaseFirestore")),
        )
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.firebase.android.firestore)
        }
        commonMain.dependencies {
            api(projects.firebaseApp)
            api(projects.firebaseCommon)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
        }
        getByName("androidDeviceTest").dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.androidx.test.core)
            implementation(libs.androidx.test.runner)
        }
    }
}
