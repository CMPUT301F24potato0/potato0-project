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
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    //implementation(files("C:\\Users\\Chirayu Shah\\AppData\\Local\\Android\\Sdk\\platforms\\android-34\\android.jar"))
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