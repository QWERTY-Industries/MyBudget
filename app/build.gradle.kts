plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "qi.mybudget"
    compileSdk = 36

    defaultConfig {
        applicationId = "qi.mybudget"
        minSdk = 29
        targetSdk = 36
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures{
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}


dependencies {

    // Keep your existing core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // --- FIREBASE DEPENDENCIES (CLEANED UP) ---
    // 1. Import the Firebase BoM ONCE. Use the latest version.
    implementation(platform("com.google.firebase:firebase-bom:33.1.1")) // Using a recent stable version

    // 2. Add individual Firebase products WITHOUT versions. The BoM handles them.
    // Use the non-alias versions since you had them already.
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")

    // --- NAVIGATION ---
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // --- ROOM DATABASE (Updated to Stable Version) ---
    val room_version = "2.6.1" // Using the latest STABLE version
   // implementation("androidx.room:room-runtime:$room_version")
    //implementation("androidx.room:room-ktx:$room_version") // For coroutines support
    //ksp("androidx.room:room-compiler:$room_version")
    // You can remove other optional room dependencies if you are not using them (rxjava, guava, etc.)
    // testImplementation("androidx.room:room-testing:$room_version")

    // --- COROUTINES (Updated to Stable Version) ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // --- LIFECYCLE ---
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3") // Updated to latest stable

    // --- Other Dependencies ---
    implementation(libs.play.services.maps)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Graphs
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}