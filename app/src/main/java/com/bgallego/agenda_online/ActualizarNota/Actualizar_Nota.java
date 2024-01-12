package com.bgallego.agenda_online.ActualizarNota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bgallego.agenda_online.AgregarNota.Agregar_Nota;
import com.bgallego.agenda_online.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Actualizar_Nota extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView Id_nota_A, Uid_Usuario_A, Correo_usuario_A, Fecha_registro_A, Fecha_A, Estado_A, Estado_nuevo;
    EditText Titulo_A, Descripcion_A;
    Button Btn_Calendario_A;

    // DECLARAR LOS STRING PARA ALMACENAR LOS DATOS RECUPERADOS DE LA ACTIVIDAD LISTAR NOTA
    String id_nota_R , uid_usuario_R , correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

    ImageView Tarea_Finalizada, Tarea_No_Finalizada;

    Spinner Spinner_estado;
    int dia, mes, anho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_nota);

        // Creación de la flecha para atras (ActionBar).
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Actualizar nota");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InicializarVistas();
        RecuperarDatos();
        SetearDatos();
        ComprobarEstadoNota();
        Spinner_Estado();

        Btn_Calendario_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeleccionarFecha();
            }
        });
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

        Tarea_Finalizada = findViewById(R.id.Tarea_Finalizada);
        Tarea_No_Finalizada = findViewById(R.id.Tarea_No_Finalizada);

        Spinner_estado = findViewById(R.id.Spinner_estado);
        Estado_nuevo = findViewById(R.id.Estado_nuevo);

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

    private void ComprobarEstadoNota() {
        // Obtener el estado de la nota desde el elemento de interfaz de usuario (TextView)
        String estado_nota = Estado_A.getText().toString();

        // Comprobar si la nota está marcada como "No finalizado" y mostrar la vista correspondiente
        if (estado_nota.equals("No finalizado")) {
            Tarea_No_Finalizada.setVisibility(View.VISIBLE);
        }

        // Comprobar si la nota está marcada como "Finalizado" y mostrar la vista correspondiente
        if (estado_nota.equals("Finalizado")) {
            Tarea_Finalizada.setVisibility(View.VISIBLE);
        }
    }

    private void SeleccionarFecha(){
        final Calendar calendario = Calendar.getInstance();

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        anho = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Actualizar_Nota.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int AnioSeleccionado, int MesSeleccionado, int DiaSeleccionado) {

                String diaFormateado, mesFormateado;

                //OBTENER DIA
                if (DiaSeleccionado < 10){
                    diaFormateado = "0"+String.valueOf(DiaSeleccionado);
                    // Antes: 9/11/2022 -  Ahora 09/11/2022
                }else {
                    diaFormateado = String.valueOf(DiaSeleccionado);
                    //Ejemplo 13/08/2022
                }

                //OBTENER EL MES
                int Mes = MesSeleccionado + 1;

                if (Mes < 10){
                    mesFormateado = "0"+String.valueOf(Mes);
                    // Antes: 09/8/2022 -  Ahora 09/08/2022
                }else {
                    mesFormateado = String.valueOf(Mes);
                    //Ejemplo 13/10/2022 - 13/11/2022 - 13/12/2022

                }

                //Setear fecha en TextView
                Fecha_A.setText(diaFormateado + "/" + mesFormateado + "/"+ AnioSeleccionado);

            }
        }
                ,anho,mes,dia);
        datePickerDialog.show();
    }

    /**
     * Método para configurar un Spinner con opciones predefinidas de estados de nota.
     * Utiliza un ArrayAdapter para enlazar el conjunto de datos de los estados con el Spinner.
     */
    private void Spinner_Estado() {
        // Crear un ArrayAdapter a partir de un recurso de cadena de arrays que contiene los estados de nota
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Estados_nota, android.R.layout.simple_spinner_item);

        // Especificar el diseño del elemento desplegable
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Establecer el adaptador creado para el Spinner de estados
        Spinner_estado.setAdapter(adapter);
        Spinner_estado.setOnItemSelectedListener(this);  // Al seleccionar en un valor se debe setear en Estado_nuevo
    }

    private void ActualizarNotaBD() {
        // Capturar los datos
        String tituloActualizar = Titulo_A.getText().toString();
        String descripcionActualizar = Descripcion_A.getText().toString();
        String fechaActualizar = Fecha_A.getText().toString();
        String estadoActualizar = Estado_nuevo.getText().toString();

        // Llamar a Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Notas_Publicadas");

        // Consulta
        Query query = databaseReference.orderByChild("id_nota").equalTo(id_nota_R);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().child("titulo").setValue(tituloActualizar);
                    ds.getRef().child("descripcion").setValue(descripcionActualizar);
                    ds.getRef().child("fecha_nota").setValue(fechaActualizar);
                    ds.getRef().child("estado").setValue(estadoActualizar);
                }
                Toast.makeText(Actualizar_Nota.this, "Nota actualizada con éxito", Toast.LENGTH_SHORT).show();
                onBackPressed(); // Nos dirige a la actividad anterior al actualizar la nota
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String ESTADO_ACTUAL = Estado_A.getText().toString();

        String Posicion_1 = adapterView.getItemAtPosition(1).toString();

        String estado_seleccionado = adapterView.getItemAtPosition(i).toString(); // Obtener el estado seleccionado en el AdapterView
        Estado_nuevo.setText(estado_seleccionado); // Establecer el estado seleccionado en un elemento de interfaz de usuario (TextView, supongamos)

        if (ESTADO_ACTUAL.equals("Finalizado")){
            Estado_nuevo.setText(Posicion_1);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Método de devolución de llamada para crear el menú de opciones cuando se crea la actividad.
     * Infla el diseño del menú definido en 'menu_actualizar.xml'.
     *
     * @param menu El menú de opciones en el que colocas tus elementos.
     * @return true para mostrar el menú; false para evitar que se muestre.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Obtener un objeto MenuInflater para inflar el menú desde 'menu_actualizar.xml'
        MenuInflater menuInflater = getMenuInflater();

        // Inflar el menú en el objeto Menu proporcionado como parámetro
        menuInflater.inflate(R.menu.menu_actualizar, menu);

        // Llamar al método en la superclase para realizar cualquier trabajo adicional
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Método de devolución de llamada invocado cuando se selecciona un elemento del menú.
     * En este caso, escucha la selección del elemento de menú 'Actualizar_Nota_BD',
     * y muestra un mensaje Toast indicando que la nota ha sido actualizada.
     *
     * @param item El elemento de menú que se seleccionó.
     * @return true para consumir el evento del menú aquí; false para permitir que la procesamiento normal del menú continúe.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Actualizar_Nota_BD) {
            ActualizarNotaBD();
            // Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);   // Llamar al método en la superclase para realizar cualquier trabajo adicional
    }

    // Acción que nos permite regresar a la actividad anterior
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}