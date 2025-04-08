// In your root build.gradle.kts (project-level)
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.8.0") // Or the latest stable Android Gradle Plugin version
        classpath("com.google.gms:google-services:4.4.1") // Example: Update to the latest stable Google Services Plugin version
    }
    repositories {
        google()
        mavenCentral()
    }
}

allprojects {
    repositories {

    }
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}