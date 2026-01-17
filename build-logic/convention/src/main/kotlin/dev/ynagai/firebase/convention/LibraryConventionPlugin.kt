package dev.ynagai.firebase.convention

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class LibraryConventionPlugin : Plugin<Project> {
    @Suppress("UnstableApiUsage")
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("maven-publish")
                apply("signing")
            }
            group = "dev.ynagai.firebase"
            version = findProperty("VERSION_NAME")?.toString() ?: "0.1.0-SNAPSHOT"

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
                }
            }

            extensions.configure<PublishingExtension> {
                repositories {
                    maven {
                        name = "sonatype"
                        val isSnapshot = version.toString().endsWith("-SNAPSHOT")
                        url = uri(
                            if (isSnapshot) {
                                "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                            } else {
                                "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                            }
                        )
                        credentials {
                            username = findProperty("OSSRH_USERNAME")?.toString()
                                ?: System.getenv("OSSRH_USERNAME")
                            password = findProperty("OSSRH_PASSWORD")?.toString()
                                ?: System.getenv("OSSRH_PASSWORD")
                        }
                    }
                }
                publications.withType<MavenPublication>().configureEach {
                    pom {
                        name.set(project.name)
                        description.set("Firebase Kotlin Multiplatform SDK - ${project.name}")
                        url.set("https://github.com/pois0/firebase-kotlin-sdk")
                        licenses {
                            license {
                                name.set("Apache License, Version 2.0")
                                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                            }
                        }
                        developers {
                            developer {
                                id.set("pois0")
                                name.set("pois0")
                                url.set("https://github.com/pois0")
                            }
                        }
                        scm {
                            connection.set("scm:git:git://github.com/pois0/firebase-kotlin-sdk.git")
                            developerConnection.set("scm:git:ssh://github.com:pois0/firebase-kotlin-sdk.git")
                            url.set("https://github.com/pois0/firebase-kotlin-sdk")
                        }
                    }
                }
            }

            val signingKey = findProperty("SIGNING_KEY")?.toString()
                ?: System.getenv("SIGNING_KEY")
            val signingKeyId = findProperty("SIGNING_KEY_ID")?.toString()
                ?: System.getenv("SIGNING_KEY_ID")
            val signingPassword = findProperty("SIGNING_PASSWORD")?.toString()
                ?: System.getenv("SIGNING_PASSWORD")

            // Only configure signing if credentials are available
            if (!signingKey.isNullOrBlank()) {
                extensions.configure<SigningExtension> {
                    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
                    sign(extensions.getByType<PublishingExtension>().publications)
                }
            }
        }
    }
}
