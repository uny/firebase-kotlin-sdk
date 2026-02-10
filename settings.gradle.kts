pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven("https://packages.jetbrains.team/maven/p/kt/dev")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        maven("https://packages.jetbrains.team/maven/p/kt/dev")
        google()
        mavenCentral()
    }
}

rootProject.name = "firebase-kotlin-sdk"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":firebase-ai",
    ":firebase-app",
    ":firebase-common",
    ":firebase-firestore",
)
