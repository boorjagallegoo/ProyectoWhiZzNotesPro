package com.bgallego.agenda_online;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Pantalla_De_Bienvenida extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_bienvenida);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        firebaseAuth = FirebaseAuth.getInstance();

        int tiempo = 3500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              /*  startActivity(new Intent(PantallaDeCarga.this, MainActivity.class));
                finish(); */
                VerificarUsuario();
            }
        }, tiempo);
    }

    private void VerificarUsuario() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            startActivity(new Intent(Pantalla_De_Bienvenida.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(Pantalla_De_Bienvenida.this, MenuPrincipal.class));
            finish();
        }
    }
}