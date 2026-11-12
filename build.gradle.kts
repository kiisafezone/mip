plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
}
tasks.register("assembleDebug") {
    group = "build"
    description = "Assembles the Android debug APK."
    dependsOn(":apps:android:assembleDebug")
}
val verifySharedCoreNoAndroidImports = tasks.register("verifySharedCoreNoAndroidImports") {
    group = "verification"
    description = "Rejects Android framework imports from shared Kotlin source sets."
    val sharedSources = fileTree(layout.projectDirectory.dir("shared")) { include("**/*.kt") }
    inputs.files(sharedSources)
    doLast {
        val violations = sharedSources.files.flatMap { source ->
            source.readLines().mapIndexedNotNull { index, line ->
                if (Regex("""^\s*import\s+(android|androidx)\.""").containsMatchIn(line)) {
                    "${source.relativeTo(rootDir)}:${index + 1}: ${line.trim()}"
                } else null
            }
        }
        check(violations.isEmpty()) {
            "Android imports are forbidden in shared core:\n${violations.joinToString("\n")}"
        }
    }
}
subprojects {
    tasks.matching { it.name == "preBuild" }.configureEach {
        dependsOn(rootProject.tasks.named("verifySharedCoreNoAndroidImports"))
    }
}
