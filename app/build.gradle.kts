plugins {
    id("com.android.application")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

}

android {
    namespace = "com.bgallego.agenda_online"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bgallego.agenda_online"
        minSdk = 34
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.airbnb.android:lottie:3.0.1")
    implementation ("androidx.annotation:annotation:1.7.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("com.google.firebase:firebase-analytics:21.5.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.robolectric:robolectric:4.6.1") // Proporciona una simulación del entorno Android, permitiendo la ejecución de pruebas de unidades de manera aislada.
    implementation ("com.google.android.material:material:1.11.0")
    implementation("com.google.firebase:firebase-auth:22.3.0") /*AUTENTICACIÓN DE FIREBASE*/
    implementation("com.google.firebase:firebase-database:20.3.0") /*BASE DE DATOS DE FIREBASE*/
    implementation ("com.firebaseui:firebase-ui-database:8.0.0") /*FIREBASE UI*/
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}