plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mindspace"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mindspace"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        mlModelBinding = true
    }

}

dependencies {

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.gson)
    implementation(libs.androidx.datastore.preferences)
    implementation (libs.retrofit2.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.jetbrains.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
    implementation(platform(libs.firebase.bom))
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation(libs.generativeai)
    implementation("com.google.android.gms:play-services-auth:20.5.0")
    implementation("com.google.firebase:firebase-auth-ktx:23.1.0")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("com.google.guava:guava:31.0.1-android")
    implementation("org.reactivestreams:reactive-streams:1.0.4")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation("org.commonmark:commonmark:0.18.2")
//    implementation("com.google.firebase:firebase-ml-modeldownloader:24.2.1")
//    implementation("org.tensorflow:tensorflow-lite-task-text:0.4.4")
    implementation (libs.tensorflow.lite)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material.v190)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.recyclerview)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.tensorflow.lite.gpu)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}