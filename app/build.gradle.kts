plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
//    id("com.android.application")
}

android {
    namespace = "com.example.eventlottery"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.eventlottery"
        minSdk = 24
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
    packagingOptions {
        exclude ("META-INF/DEPENDENCIES")
    }
}

dependencies {
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation(platform(libs.firebase.bom))
    implementation ("com.google.android.material:material:1.2.0")

//    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-messaging:21.0.1")   //java)
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))


    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    // https://developer.android.com/training/testing/espresso/intents#java
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

//    implementation(files("/home/chirayu/Android/Sdk/platforms/android-34/android.jar"))
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.com.google.firebase.firebase.messaging)

    implementation (libs.google.auth.library.oauth2.http)
    implementation (libs.volley)

}