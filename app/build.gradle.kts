plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "fr.ldnr.eatable"
    compileSdk = 34

    defaultConfig {
        applicationId = "fr.ldnr.eatable"
        minSdk = 23
        targetSdk = 34 // mettre la version de l'appareil le plus récent qui a testé l'application
        // maxSdk = 38

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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // implementation("org.xhtmlrenderer:flying-saucer-pdf:9.7.2")
}