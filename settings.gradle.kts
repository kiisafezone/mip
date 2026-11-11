pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}
rootProject.name = "MIP"
include(":apps:android", ":apps:android:presentation", ":data:local", ":shared:domain", ":shared:contracts", ":shared:engine")
