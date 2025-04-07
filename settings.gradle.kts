pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.9.1" // Or any compatible version
        id("org.jetbrains.kotlin.android") version "1.9.22" // If using Kotlin
        include(":app")

    }
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // ... other repositories
    }
}
rootProject.name = "AttendanceManager"
include(":app")
}