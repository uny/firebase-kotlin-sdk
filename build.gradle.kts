import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.publish) apply false
}

gradle.projectsEvaluated {
    val fetchTasks = subprojects.mapNotNull { it.tasks.findByName("fetchSyntheticImportProjectPackages") }
    fetchTasks.zipWithNext { prev, next ->
        next.mustRunAfter(prev)
    }

    // Remove SwiftPM metadata artifacts with empty extension that Maven Central rejects.
    // Kotlin 2.4.0-Beta1 SwiftPM support publishes files like "artifact-swiftpm-metadata."
    // with a trailing dot and no extension.
    subprojects {
        pluginManager.withPlugin("maven-publish") {
            extensions.configure<PublishingExtension> {
                publications.withType<MavenPublication>().configureEach {
                    artifacts.removeAll {
                        it.classifier == "swiftpm-metadata" && it.extension.isNullOrEmpty()
                    }
                }
            }
        }
    }
}
