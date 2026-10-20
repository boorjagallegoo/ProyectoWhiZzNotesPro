# 📱 Proyecto Final de Ciclo: Agenda de Notas Online

¡Hola! En este repositorio podrás ver mi proyecto de fin de ciclo desarrollado en **Android Studio** utilizando **Java** y **Firebase**.

---

## 🛠️ Configuración e Instalación

Para poner en marcha el proyecto en tu entorno local, sigue estos pasos:

### PASO 1: Dependencias
Añade las siguientes dependencias en tu archivo `Gradle Script` -> `build.gradle` (dentro de `Module: app`):

```groovy
dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.0")
    
    // Lottie
    implementation("com.airbnb.android:lottie:3.0.1")
    
    // LiveData & ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    
    // Firebase
    implementation("com.google.firebase:firebase-analytics:21.6.2")
    implementation("com.google.firebase:firebase-auth:22.3.1")            /* AUTENTICACIÓN */
    implementation("com.google.firebase:firebase-database:20.3.1")        /* BASE DE DATOS */
    implementation("com.firebaseui:firebase-ui-database:8.0.0")           /* FIREBASE UI */
    implementation("com.google.firebase:firebase-storage:20.3.0")         /* ALMACENAMIENTO */
    
    // Glide (Imágenes)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    
    // Country Code Picker
    implementation("com.hbb20:ccp:2.5.0")

    // Tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.7")
    testImplementation("io.mockk:mockk:1.12.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

### PASO 2: Sincronización
Haz clic en Gradle Sync en Android Studio para descargar e integrar todas las librerías necesarias.
