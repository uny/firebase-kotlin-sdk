import io.github.frankois944.spmForKmp.swiftPackageConfig
import io.github.frankois944.spmForKmp.utils.ExperimentalSpmForKmpFeature

plugins {
    id("dev.ynagai.firebase.library")
}

@OptIn(ExperimentalSpmForKmpFeature::class)
kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.ai"
    }
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.swiftPackageConfig(cinteropName = "FirebaseAIBridge") {
            minIos = "15.0"
            dependency {
                remotePackageVersion(
                    url = uri("https://github.com/firebase/firebase-ios-sdk.git"),
                    products = {
                        add("FirebaseAILogic", exportToKotlin = true)
                    },
                    version = libs.versions.firebase.apple.get(),
                )
            }
        }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.firebase.android.ai)
        }
        commonMain.dependencies {
            implementation(project(":firebase-app"))
        }
    }
}
