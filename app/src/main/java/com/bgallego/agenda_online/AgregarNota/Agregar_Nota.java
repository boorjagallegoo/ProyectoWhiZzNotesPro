package com.bgallego.agenda_online.AgregarNota;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bgallego.agenda_online.R;

import java.util.Calendar;

public class Agregar_Nota extends AppCompatActivity {

    TextView Uid_Usuario, Correo_usuario, Fecha_hora_actual, Fecha, Estado;
    EditText Titulo, Descripcion;
    Button Btn_Calendario;

    int dia, mes, anho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nota);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InicializarVariables();
        ObtenerDatos();
        Obtener_Fecha_Hora_Actual();

        Btn_Calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();

                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                anho = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Agregar_Nota.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int AnhoSeleccionado, int MesSeleccionado, int DiaSeleccionado) {

                        String diaFormateado, mesFormateado;

                        // OBTENER DIA
                        if (DiaSeleccionado < 10) {
                            diaFormateado = "8" + String.valueOf(DiaSeleccionado);
                            // Antes: 5/12/2023 - Ahora: 05/12/2023
                        } else {
                            diaFormateado = String.valueOf(DiaSeleccionado);
                            // Ejemplo: 12/12/2023
                        }

                        // OBTENER MES
                        int Mes = MesSeleccionado + 1;

                        if (Mes < 10) {
                            mesFormateado = "8" + String.valueOf(Mes);
                            // Antes: 05/3/2023 - Ahora: 05/03/2023
                        } else {
                            mesFormateado = String.valueOf(Mes);
                            // Ejemplo: 12/10/2023 - 12/11/2023 - 12/12/2023
                        }

                        // Setear fecha en TextView
                        Fecha.setText(diaFormateado + "/" + mesFormateado + "/" + AnhoSeleccionado);

                    }
                }
                , anho, mes, dia);
                datePickerDialog.show();

            }
        });
    }

    private void InicializarVariables() {
        Uid_Usuario = findViewById(R.id.Uid_Usuario);
        Correo_usuario = findViewById(R.id.Correo_usuario);
        Fecha_hora_actual = findViewById(R.id.Fecha_hora_actual);
        Fecha = findViewById(R.id.Fecha);
        Estado = findViewById(R.id.Estado);

        Titulo = findViewById(R.id.Titulo);
        Descripcion = findViewById(R.id.Descripcion);
        Btn_Calendario = findViewById(R.id.Btn_Calendario);
    }

    private void ObtenerDatos() {
    }

    private void Obtener_Fecha_Hora_Actual() {
    }

}