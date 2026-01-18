plugins {
    `kotlin-dsl`
}

group = "dev.ynagai.firebase.buildlogic"

dependencies {
    compileOnly(libs.plugin.android)
    compileOnly(libs.plugin.kotlin)
    compileOnly(libs.plugin.publish)
}

gradlePlugin {
    plugins {
        register("library") {
            id = "dev.ynagai.firebase.library"
            implementationClass = "dev.ynagai.firebase.convention.LibraryConventionPlugin"
        }
    }
}
