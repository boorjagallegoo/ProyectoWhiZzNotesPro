package com.bgallego.agenda_online.AgregarNota;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bgallego.agenda_online.Objetos.Nota;
import com.bgallego.agenda_online.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Agregar_Nota extends AppCompatActivity {

    TextView Uid_Usuario, Correo_usuario, Fecha_hora_actual, Fecha, Estado;
    EditText Titulo, Descripcion;
    Button Btn_Calendario;

    int dia, mes, anho;

    DatabaseReference BD_Firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nota);

        // Creación de la flecha para atras (ActionBar).
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Agregar nota");
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

                        // Formatear DIA con cero a la izquierda si es menor a 10
                        String diaFormateado = (DiaSeleccionado < 10) ? "0" + DiaSeleccionado : String.valueOf(DiaSeleccionado);

                        // Formatear MES con cero a la izquierda si es menor a 10
                        int Mes = MesSeleccionado + 1;  // El mes está indexado desde 0
                        String mesFormateado = (Mes < 10) ? "0" + Mes : String.valueOf(Mes);

                        // Setear fecha en TextView
                        Fecha.setText(diaFormateado + "/" + mesFormateado + "/" + AnhoSeleccionado);

                    }
                }, anho, mes, dia);
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

        BD_Firebase = FirebaseDatabase.getInstance().getReference();
    }

    private void ObtenerDatos() {
        String uid_recuperado = getIntent().getStringExtra("Uid");
        String correo_recuperado = getIntent().getStringExtra("Correo");

        Uid_Usuario.setText(uid_recuperado);
        Correo_usuario.setText(correo_recuperado);
    }

    private void Obtener_Fecha_Hora_Actual() {
        String Fecha_hora_registro = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a",
                Locale.getDefault()).format(System.currentTimeMillis());
        // EJEMPLO: 30-12-2023/12:18:21 am
        Fecha_hora_actual.setText(Fecha_hora_registro);
    }

    public void AgregarNota() { // Lo cambiamos de private a public para el test

        // Obtener los datos
        String uid_usuario = Uid_Usuario.getText().toString();
        String correo_usuario = Correo_usuario.getText().toString();
        String fecha_hora_actual = Fecha_hora_actual.getText().toString();
        String titulo = Titulo.getText().toString();
        String descripcion = Descripcion.getText().toString();
        String fecha = Fecha.getText().toString();
        String estado = Estado.getText().toString();

        // Validar datos
        if (!uid_usuario.equals("") && !correo_usuario.equals("") && !fecha_hora_actual.equals("") &&
                !titulo.equals("") && !descripcion.equals("") && !fecha.equals("") && !estado.equals("")) {

            Nota nota = new Nota(correo_usuario + "/" + fecha_hora_actual,
                    uid_usuario,
                    correo_usuario,
                    fecha_hora_actual,
                    titulo,
                    descripcion,
                    fecha,
                    estado);

            String Nota_usuario = BD_Firebase.push().getKey();
            // Establecer el nombre de la BD
            String Nombre_BD = "Notas_Publicadas";

            BD_Firebase.child(Nombre_BD).child(Nota_usuario).setValue(nota);

            Toast.makeText(this, "Se ha agregado la nota exitosamente", Toast.LENGTH_SHORT).show();
            onBackPressed();

        } else {
            Toast.makeText(this, "Llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método llamado para crear el menú de opciones en la barra de acción.
     *
     * @param menu El menú en el que se inflarán los elementos de la barra de acción.
     * @return Devuelve true para que el menú se muestre; si se devuelve false, no se mostrará.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agregar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Se llama cuando se selecciona un elemento del menú de opciones.
     *
     * @param item El elemento de menú seleccionado.
     * @return Devuelve true si el evento se gestionó correctamente, de lo contrario, devuelve false.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Agregar_Nota_BD) {
            AgregarNota();
        }
        return super.onOptionsItemSelected(item);
    }

    // Acción que nos permite regresar a la actividad anterior
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}