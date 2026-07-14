pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "firebase-kotlin-sdk"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":firebase-ai",
    ":firebase-analytics",
    ":firebase-analytics-advertising",
    ":firebase-app",
    ":firebase-auth",
    ":firebase-common",
    ":firebase-firestore",
)
