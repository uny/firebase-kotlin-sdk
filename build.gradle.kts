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
}
