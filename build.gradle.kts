// In your root build.gradle.kts (project-level)
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.9.1") // Or the latest stable Android Gradle Plugin version
        classpath("com.google.gms:google-services:4.4.1") // Example: Update to the latest stable Google Services Plugin version
    }
    repositories {
        google()
        mavenCentral()
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}