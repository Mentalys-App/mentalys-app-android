plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-parcelize")
    alias(libs.plugins.google.secrets)
}

android {
    namespace = "com.mentalys.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mentalys.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GEMINI_API_KEY", "\"${project.findProperty("GEMINI_API_KEY")}\"")
        buildConfigField("String", "MAPS_API_KEY", "\"${project.findProperty("MAPS_API_KEY")}\"")
        buildConfigField("String", "BASE_URL_MAIN", "\"${project.findProperty("BASE_URL_MAIN")}\"")
        buildConfigField("String", "BASE_URL_ARTICLES", "\"${project.findProperty("BASE_URL_ARTICLES")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
//            buildConfigField("String", "GEMINI_API_KEY", "\"${project.properties['GEMINI_API_KEY']}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)

    // Coroutines (for Room & Retrofit)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Okhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Settings Preferences
    implementation(libs.androidx.preference)
    implementation(libs.androidx.datastore.preferences)

    // Paging
    implementation(libs.androidx.paging.runtime.ktx)

    // Gemini AI
    implementation(libs.generativeai)

    // Markwon
    implementation(libs.core)

    // Camera X
    implementation(libs.androidx.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Liquid Swipe
    implementation(libs.liquid.swipe)

    // Meow Bottom Navigation View
    implementation(files("libs/meow-bottom-navigation-java-1.2.0.aar"))

    // Circle ImageView & Glide
    implementation(libs.circleimageview)
    implementation(libs.glide)

    // Shimmer Effect
    implementation(libs.shimmer)

    // Midtrans
    implementation("com.midtrans:uikit:2.0.0-SANDBOX")

    // ViewPager
    implementation(libs.material.v1110)
    implementation(libs.androidx.viewpager2)

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    // Browser
    implementation("androidx.browser:browser:1.8.0")

}