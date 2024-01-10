package com.bgallego.agenda_online.ListarNotas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bgallego.agenda_online.ActualizarNota.Actualizar_Nota;
import com.bgallego.agenda_online.Objetos.Nota;
import com.bgallego.agenda_online.R;
import com.bgallego.agenda_online.ViewHolder.ViewHolder_Nota;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Listar_Notas extends AppCompatActivity {

    RecyclerView recyclerviewNotas;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Nota, ViewHolder_Nota> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Nota> options;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_notas);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Mis notas");
            // Flecha hacia atrás
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        recyclerviewNotas = findViewById(R.id.recyclerviewNotas);
        recyclerviewNotas.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("Notas_Publicadas");
        dialog = new Dialog(Listar_Notas.this);
        ListarNotasUsuarios();
    }

    private void ListarNotasUsuarios() {
        options = new FirebaseRecyclerOptions.Builder<Nota>()
                .setQuery(BASE_DE_DATOS.orderByKey(), Nota.class)
                .build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nota, ViewHolder_Nota>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Nota viewHolder_nota, int position, @NonNull Nota nota) {
                Log.d("Notas", "Contenido de la nota: " + nota.toString());

                viewHolder_nota.SetearDatos(
                        getApplicationContext(),
                        nota.getId_nota(),
                        nota.getUid_usuario(),
                        nota.getCorreo_usuario(),
                        nota.getFecha_hora_actual(),
                        nota.getTitulo(),
                        nota.getDescripcion(),
                        nota.getFecha_nota(),
                        nota.getEstado()
                );
            }

            @NonNull
            @Override
            public ViewHolder_Nota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
                ViewHolder_Nota viewHolder_nota = new ViewHolder_Nota(view);
                viewHolder_nota.setOnClickListener(new ViewHolder_Nota.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(Listar_Notas.this, "on item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        // Obtener los datos de la nota seleccionada
                        String id_nota = getItem(position).getId_nota();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_nota = getItem(position).getFecha_nota();
                        String estado = getItem(position).getEstado();

                        // Declarar las vistas
                        Button CD_Eliminar, CD_Actualizar;

                        // Realizar la conexión con el diseño
                        dialog.setContentView(R.layout.dialogo_opciones);

                        // Inicializar las vistas
                        CD_Eliminar = dialog.findViewById(R.id.CD_Eliminar);
                        CD_Actualizar = dialog.findViewById(R.id.CD_Actualizar);

                        CD_Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               EliminarNota(id_nota);
                               dialog.dismiss(); // Se cierra automaticamente el cuadro.

                            }
                        });

                        CD_Actualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Toast.makeText(Listar_Notas.this, "Actualizar nota", Toast.LENGTH_SHORT).show();
                               // startActivity(new Intent(Listar_Notas.this, Actualizar_Nota.class)); // Ir a la actividad Actualizar nota.
                                Intent intent = new Intent(Listar_Notas.this, Actualizar_Nota.class);
                                intent.putExtra("id_nota", id_nota);
                                intent.putExtra("uid_usuario", uid_usuario);
                                intent.putExtra("correo_usuario", correo_usuario);
                                intent.putExtra("fecha_registro", fecha_registro);
                                intent.putExtra("titulo", titulo);
                                intent.putExtra("descripcion", descripcion);
                                intent.putExtra("fecha_nota", fecha_nota);
                                intent.putExtra("estado", estado);
                                startActivity(intent);
                                dialog.dismiss(); // Se cierra automaticamente el cuadro.
                            }
                        });
                        dialog.show();
                    }
                });
                return viewHolder_nota;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Listar_Notas.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerviewNotas.setLayoutManager(linearLayoutManager);
        recyclerviewNotas.setAdapter(firebaseRecyclerAdapter);
    }

    /**
     * Método para eliminar una nota mediante un cuadro de diálogo de confirmación.
     *
     * @param id_nota El identificador único de la nota que se desea eliminar.
     */
    private void EliminarNota(String id_nota) {

        // Construir un cuadro de diálogo de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Notas.this);
        builder.setTitle("Eliminar nota");
        builder.setMessage("¿Desea eliminar la nota?");

        // Configurar el botón positivo (Sí) del cuadro de diálogo
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ELIMINAR NOTA EN BD
                Query query = BASE_DE_DATOS.orderByChild("id_nota").equalTo(id_nota);

                // Escuchar los cambios en la base de datos para eliminar la nota
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Iterar sobre los resultados de la consulta y eliminar la nota
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        // Mostrar mensaje de éxito
                        Toast.makeText(Listar_Notas.this, "Nota eliminada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Mostrar mensaje de error en caso de fallo
                        Toast.makeText(Listar_Notas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Configurar el botón negativo (No) del cuadro de diálogo
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Mostrar mensaje de cancelación por el usuario
                Toast.makeText(Listar_Notas.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        // Crear y mostrar el cuadro de diálogo
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
