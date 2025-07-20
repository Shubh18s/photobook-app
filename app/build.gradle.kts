plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.gudwinfailsafe.android.photobook"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gudwinfailsafe.android.photobook"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures { viewBinding = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    // implementation(libs.appcompat)
    // implementation(libs.material)
    // testImplementation(libs.junit)
    // androidTestImplementation(libs.ext.junit)
    // androidTestImplementation(libs.espresso.core)

    implementation("com.google.android.material:material:1.12.0") // Or a later stable version
    // Make sure you also have appcompat, as Material Components depends on it
    implementation("androidx.appcompat:appcompat:1.6.1") // Or a later stable version

    // For local JARs:
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.recyclerview)
    testImplementation("junit:junit:4.13-beta-3")
//    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.squareup.picasso:picasso:2.71828") {
        // Picasso 2.71828 requires 'java.util.concurrent.Future' which is
        // available in Guava. AndroidX and older Guava versions can conflict.
        // This is a common exclusion to prevent duplicate class errors.
        // If you get duplicate class errors with Guava later, you might need this.
        exclude(group = "org.json", module = "json") // Common for older SDK
    }
    implementation("com.github.bumptech.glide:glide:4.9.0")

    implementation("com.google.android.gms:play-services-ads:23.0.0")
}