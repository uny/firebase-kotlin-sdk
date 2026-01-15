package dev.ynagai.firebase.convention

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class LibraryConventionPlugin : Plugin<Project> {
    @Suppress("UnstableApiUsage")
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            extensions.configure<KotlinMultiplatformExtension> {
                androidLibrary {
                    compileSdk =
                        libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
                    minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_21)
                    }
                }
                iosArm64()
                iosSimulatorArm64()
                sourceSets.apply {
                    androidMain.dependencies {
                        implementation(
                            project.dependencies.platform(
                                libs.findLibrary("firebase-bom").get()
                            )
                        )
                    }
                }
            }
        }
    }
}
