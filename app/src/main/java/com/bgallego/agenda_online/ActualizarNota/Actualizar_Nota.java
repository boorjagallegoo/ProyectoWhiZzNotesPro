package com.bgallego.agenda_online.ActualizarNota;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bgallego.agenda_online.R;

public class Actualizar_Nota extends AppCompatActivity {

    TextView Id_nota_A, Uid_Usuario_A, Correo_usuario_A, Fecha_registro_A, Fecha_A, Estado_A;
    EditText Titulo_A, Descripcion_A;
    Button Btn_Calendario_A;

    // DECLARAR LOS STRING PARA ALMACENAR LOS DATOS RECUPERADOS DE LA ACTIVIDAD LISTAR NOTA
    String id_nota_R , uid_usuario_R , correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_nota);
        InicializarVistas();
        RecuperarDatos();
        SetearDatos();
    }

    private void InicializarVistas() {
        // Inicializar las vistas con sus respectivos IDs del layout
        Id_nota_A = findViewById(R.id.Id_nota_A);
        Uid_Usuario_A = findViewById(R.id.Uid_Usuario_A);
        Correo_usuario_A = findViewById(R.id.Correo_usuario_A);
        Fecha_registro_A = findViewById(R.id.Fecha_registro_A);
        Fecha_A = findViewById(R.id.Fecha_A);
        Estado_A = findViewById(R.id.Estado_A);
        Titulo_A = findViewById(R.id.Titulo_A);
        Descripcion_A = findViewById(R.id.Descripcion_A);
        Btn_Calendario_A = findViewById(R.id.Btn_Calendario_A);

    }

    private void RecuperarDatos() {
        Bundle intent = getIntent().getExtras();
        // Almacenamos los datos recuperados del intent, key = intent.putExtra de CD_Actualizar
        id_nota_R = intent.getString("id_nota");
        uid_usuario_R = intent.getString("uid_usuario");
        correo_usuario_R = intent.getString("correo_usuario");
        fecha_registro_R = intent.getString("fecha_registro");
        titulo_R = intent.getString("titulo");
        descripcion_R = intent.getString("descripcion");
        fecha_R = intent.getString("fecha_nota");
        estado_R = intent.getString("estado");

    }

    private void SetearDatos() {
        // Setear los datos recuperados en las vistas correspondientes
        Id_nota_A.setText(id_nota_R);
        Uid_Usuario_A.setText(uid_usuario_R);
        Correo_usuario_A.setText(correo_usuario_R);
        Fecha_registro_A.setText(fecha_registro_R);
        Titulo_A.setText(titulo_R);
        Descripcion_A.setText(descripcion_R);
        Fecha_A.setText(fecha_R);
        Estado_A.setText(estado_R);

    }
}