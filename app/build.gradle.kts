plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // For Firebase integration
}

android {
    namespace = "com.example.attendancemanager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.attendancemanager"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
        // dataBinding = true
    }
}

configurations.all {
    resolutionStrategy.force("androidx.recyclerview:recyclerview:1.4.0")
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0-alpha03")
    implementation("com.google.android.material:material:1.12.0-alpha03")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation(libs.firebase.database)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Import the Firebase BoM (Bill of Materials) - RECOMMENDED
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))

    // Add the Firebase SDKs that you need (specific products)
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
}