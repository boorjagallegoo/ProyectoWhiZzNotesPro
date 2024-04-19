package com.bgallego.agenda_online.Contactos;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bgallego.agenda_online.R;
import com.bumptech.glide.Glide;

public class Detalle_contacto extends AppCompatActivity {

    ImageView Imagen_C_D;
    TextView Id_C_D, Uid_Usuario_D, Nombres_C_D, Apellidos_C_D, Correo_C_D, Telefono_C_D, Edad_C_D, Direccion_C_D;

    /* String donde almacenaremos los datos del contacto seleccionado */ String id_c, uid_usuario, nombres_c, apellidos_c, correo_c, telefono_c, edad_c, direccion_c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contacto);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Detalle contacto");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InicializarVariables();
        RecuperarDatosContacto();
        SetearDatosContacto();
        ObtenerImagen();

    }

    private void InicializarVariables() {
        Imagen_C_D = findViewById(R.id.Imagen_C_D);
        Id_C_D = findViewById(R.id.Id_C_D);
        Uid_Usuario_D = findViewById(R.id.Uid_Usuario_D);
        Nombres_C_D = findViewById(R.id.Nombres_C_D);
        Apellidos_C_D = findViewById(R.id.Apellidos_C_D);
        Correo_C_D = findViewById(R.id.Correo_C_D);
        Telefono_C_D = findViewById(R.id.Telefono_C_D);
        Edad_C_D = findViewById(R.id.Edad_C_D);
        Direccion_C_D = findViewById(R.id.Direccion_C_D);

    }

    private void RecuperarDatosContacto() {
        Bundle bundle = getIntent().getExtras();

        id_c = bundle.getString("id_c");
        uid_usuario = bundle.getString("uid_usuario");
        nombres_c = bundle.getString("nombres_c");
        apellidos_c = bundle.getString("apellidos_c");
        correo_c = bundle.getString("correo_c");
        telefono_c = bundle.getString("telefono_c");
        edad_c = bundle.getString("edad_c");
        direccion_c = bundle.getString("direccion_c");

    }

    private void SetearDatosContacto() {
        Id_C_D.setText(id_c);
        Uid_Usuario_D.setText(uid_usuario);
        Nombres_C_D.setText("Nombres: " + nombres_c);
        Apellidos_C_D.setText("Apellidos: " + apellidos_c);
        Correo_C_D.setText("Correo: " + correo_c);
        Telefono_C_D.setText("Teléfono: " + telefono_c);
        Edad_C_D.setText("Edad: " + edad_c);
        Direccion_C_D.setText("Dirección: " + direccion_c);
    }

    private void ObtenerImagen() {
        String imagen = getIntent().getStringExtra("imagen_c");

        try {
            Glide.with(getApplicationContext()).load(imagen).placeholder(R.drawable.imagen_contacto).into(Imagen_C_D);

        } catch (Exception e) {
            Toast.makeText(this, "Esperando imagen", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        // Acción que nos permite regresar a la actividad anterior
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}