package com.bgallego.agenda_online.ActualizarNota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bgallego.agenda_online.R;

public class Actualizar_Nota extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView Id_nota_A, Uid_Usuario_A, Correo_usuario_A, Fecha_registro_A, Fecha_A, Estado_A, Estado_nuevo;
    EditText Titulo_A, Descripcion_A;
    Button Btn_Calendario_A;

    // DECLARAR LOS STRING PARA ALMACENAR LOS DATOS RECUPERADOS DE LA ACTIVIDAD LISTAR NOTA
    String id_nota_R , uid_usuario_R , correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

    ImageView Tarea_Finalizada, Tarea_No_Finalizada;

    Spinner Spinner_estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_nota);
        InicializarVistas();
        RecuperarDatos();
        SetearDatos();
        ComprobarEstadoNota();
        Spinner_Estado();
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

    /**
     * Callback method invoked when an item in the AdapterView has been selected.
     * This callback is triggered only when the newly selected position is different
     * from the previously selected position or if there was no selected item.
     * Implementers can use getItemAtPosition(position) to access the data associated
     * with the selected item.
     *
     * @param adapterView The AdapterView where the selection occurred.
     * @param view        The view within the AdapterView that was clicked.
     * @param i           The position of the view in the adapter.
     * @param l           The row ID of the item that is selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Obtener el estado seleccionado en el AdapterView
        String estado_seleccionado = adapterView.getItemAtPosition(i).toString();

        // Establecer el estado seleccionado en un elemento de interfaz de usuario (TextView, supongamos)
        Estado_nuevo.setText(estado_seleccionado);
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
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
            Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show();
        }

        // Llamar al método en la superclase para realizar cualquier trabajo adicional
        return super.onOptionsItemSelected(item);
    }
}