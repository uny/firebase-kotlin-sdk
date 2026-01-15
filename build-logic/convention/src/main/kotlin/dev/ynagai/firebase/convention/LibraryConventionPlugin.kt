package dev.ynagai.firebase.convention

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

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

/**
 * iOS platform configuration for xcodebuild and cinterop.
 */
private sealed class ApplePlatform(
    val displayName: String,
    val sdkName: String,
    val derivedDataSubdir: String,
) {
    /** iOS Device (arm64) */
    data object Device : ApplePlatform(
        displayName = "iOS",
        sdkName = "iphoneos",
        derivedDataSubdir = "Device",
    )

    /** iOS Simulator (arm64) */
    data object Simulator : ApplePlatform(
        displayName = "iOS Simulator",
        sdkName = "iphonesimulator",
        derivedDataSubdir = "Simulator",
    )

    /** xcodebuild output directory name (e.g., "Release-iphoneos") */
    val configurationDir: String get() = "Release-$sdkName"

    /** Directory name for generated module maps (e.g., "GeneratedModuleMaps-iphoneos") */
    val generatedModuleMapsDir: String get() = "GeneratedModuleMaps-$sdkName"

    companion object {
        fun fromKotlinTargetName(name: String): ApplePlatform =
            if (name.contains("Simulator", ignoreCase = true)) Simulator else Device
    }
}

fun Project.configureAppleBridge(schemeName: String) {
    val nativeSourceDir = rootProject.layout.projectDirectory.dir("native/firebase-apple-bridge")
    val baseDerivedDataDir = layout.buildDirectory.dir("DerivedData/$schemeName")

    fun derivedDataDir(platform: ApplePlatform): Provider<Directory> =
        baseDerivedDataDir.map { it.dir(platform.derivedDataSubdir) }

    // Register xcodebuild tasks for each platform
    val buildTasks = mapOf(
        ApplePlatform.Device to registerXcodeBuildTask(schemeName, ApplePlatform.Device, nativeSourceDir, derivedDataDir(ApplePlatform.Device)),
        ApplePlatform.Simulator to registerXcodeBuildTask(schemeName, ApplePlatform.Simulator, nativeSourceDir, derivedDataDir(ApplePlatform.Simulator)),
    )

    extensions.configure<KotlinMultiplatformExtension> {
        targets.withType<KotlinNativeTarget>().configureEach {
            val platform = ApplePlatform.fromKotlinTargetName(name)
            val derivedData = derivedDataDir(platform)

            compilations.getByName("main") {
                val cinterop = cinterops.create(project.name) {
                    definitionFile.set(project.file("src/appleMain/cinterop/${project.name}.def"))

                    val productsDir = derivedData.map { it.dir("Build/Products/${platform.configurationDir}") }
                    val generatedModuleMapsDir = derivedData.map { it.dir("Build/Intermediates.noindex/${platform.generatedModuleMapsDir}") }

                    // Compiler options for clang
                    compilerOpts(
                        "-fmodules",
                        "-fmodule-map-file=${generatedModuleMapsDir.get().file("$schemeName.modulemap").asFile.absolutePath}",
                        "-I${generatedModuleMapsDir.get().asFile.absolutePath}",
                        "-F${productsDir.get().asFile.absolutePath}",
                        "-F${productsDir.get().dir("PackageFrameworks").asFile.absolutePath}",
                    )

                    // Header search paths
                    includeDirs(generatedModuleMapsDir.get().asFile)
                    includeDirs(productsDir.get().asFile)
                    includeDirs(productsDir.get().dir("PackageFrameworks").asFile)
                }

                tasks.named(cinterop.interopProcessingTaskName).configure {
                    dependsOn(buildTasks.getValue(platform))
                }
            }
        }
    }
}

/**
 * Registers an xcodebuild task for the specified platform.
 */
private fun Project.registerXcodeBuildTask(
    schemeName: String,
    platform: ApplePlatform,
    nativeSourceDir: Directory,
    derivedDataDir: Provider<Directory>,
): TaskProvider<Exec> = tasks.register<Exec>("buildAppleBridge${platform.derivedDataSubdir}") {
    group = "build"
    description = "Builds the $schemeName framework for ${platform.displayName}"

    inputs.dir(nativeSourceDir)
    outputs.dir(derivedDataDir)
    workingDir = nativeSourceDir.asFile

    commandLine(
        "xcodebuild", "build",
        "-scheme", schemeName,
        "-configuration", "Release",
        "-destination", "generic/platform=${platform.displayName}",
        "-derivedDataPath", derivedDataDir.get().asFile.absolutePath,
        "SKIP_INSTALL=NO",
        "BUILD_LIBRARY_FOR_DISTRIBUTION=YES",
    )
}
