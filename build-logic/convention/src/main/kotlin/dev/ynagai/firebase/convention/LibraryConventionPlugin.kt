package dev.ynagai.firebase.convention

import com.android.build.api.dsl.androidLibrary
import com.vanniktech.maven.publish.MavenPublishBaseExtension
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
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("com.vanniktech.maven.publish")
                apply("org.jetbrains.kotlin.multiplatform")
            }
            group = "dev.ynagai.firebase"
            version = property("version") as String
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
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
                withSourcesJar(publish = true)
                sourceSets.apply {
                    androidMain.dependencies {
                        implementation(
                            project.dependencies.platform(
                                libs.findLibrary("firebase-bom").get()
                            )
                        )
                    }
                    commonTest.dependencies {
                        implementation(libs.findLibrary("kotlin-test").get())
                        implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                    }
                }
            }
            extensions.configure<MavenPublishBaseExtension> {
                publishToMavenCentral(automaticRelease = true)
                signAllPublications()

                pom {
                    name.set(project.name)
                    description.set("Firebase Kotlin Multiplatform SDK - ${project.name}")
                    url.set("https://github.com/uny/firebase-kotlin-sdk")
                    licenses {
                        license {
                            name.set("Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("uny")
                            name.set("Yuki Nagai")
                            url.set("https://github.com/uny")
                        }
                    }
                    scm {
                        url.set("https://github.com/uny/firebase-kotlin-sdk")
                        connection.set("scm:git:https://github.com/uny/firebase-kotlin-sdk.git")
                        developerConnection.set("scm:git:https://github.com/uny/firebase-kotlin-sdk.git")
                    }
                }
            }
        }
    }
}
