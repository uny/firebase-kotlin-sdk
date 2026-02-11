plugins {
    id("dev.ynagai.firebase.library")
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.firestore"
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
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
