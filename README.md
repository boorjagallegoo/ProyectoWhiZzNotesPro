# [Proyecto : Android STUDIO - JAVA]

**Hola, en este repositorio podrás ver mi proyecto de fin de ciclo en Android Studio usando Java.**

PASO 1 : Añade las dependencias `Gradle Script` -> `build.gradle` -> `Module:App-Name.app`
```
dependencies{

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.annotation:annotation:1.7.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.0")
    // Lottie
    implementation("com.airbnb.android:lottie:3.0.1")
    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    // Firebase
    implementation ("com.google.firebase:firebase-analytics:21.6.2")
    implementation("com.google.firebase:firebase-auth:22.3.1") /*AUTENTICACIÓN DE FIREBASE*/
    implementation("com.google.firebase:firebase-database:20.3.1") /*BASE DE DATOS DE FIREBASE*/
    implementation ("com.firebaseui:firebase-ui-database:8.0.0") /*FIREBASE UI*/
    implementation ("com.google.firebase:firebase-storage:20.3.0") /*ALMACENAMIENTO DE FIREBASE*/
    // Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    // Country code picker android github
    implementation ("com.hbb20:ccp:2.5.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.robolectric:robolectric:4.7")
    testImplementation ("io.mockk:mockk:1.12.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

Paso 2 :``Gradle Sync ``



 ![Cool](https://tenor.com/view/mochi-peach-cat-cat-cute-happy-smile-gif-16624313.gif )
