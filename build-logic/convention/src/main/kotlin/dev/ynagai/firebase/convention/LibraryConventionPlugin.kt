package dev.ynagai.firebase.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class LibraryConventionPlugin : Plugin<Project> {
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
                targets.withType<KotlinMultiplatformAndroidLibraryTarget>().configureEach {
                    compileSdk =
                        libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
                    minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
                    compilations.configureEach {
                        compileTaskProvider.configure {
                            compilerOptions {
                                jvmTarget.set(JvmTarget.JVM_21)
                            }
                        }
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
            // Kotlin 2.4.0-Beta1 SwiftPM support attaches an artifact with classifier
            // "swiftpm-metadata" and an empty extension (trailing dot, no file type)
            // that Maven Central rejects.
            // Removing from publication.artifacts alone breaks GenerateModuleMetadata
            // because it derives componentArtifacts from the outgoing configuration.
            // TODO: Remove when fixed upstream (https://youtrack.jetbrains.com/issue/KT-85476)
            gradle.projectsEvaluated {
                fun isSwiftpmMetadata(classifier: String?, extension: String) =
                    classifier == "swiftpm-metadata" && extension.isEmpty()
                target.configurations
                    .findByName("swiftPMDependenciesMetadataElements")
                    ?.outgoing?.artifacts?.removeIf {
                        isSwiftpmMetadata(it.classifier, it.extension)
                    }
                target.extensions.configure<PublishingExtension> {
                    publications.withType<MavenPublication>().all {
                        artifacts.removeAll {
                            isSwiftpmMetadata(it.classifier, it.extension)
                        }
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
