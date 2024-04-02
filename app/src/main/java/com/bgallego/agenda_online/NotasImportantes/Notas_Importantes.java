package com.bgallego.agenda_online.NotasImportantes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bgallego.agenda_online.Detalle.Detalle_Nota;
import com.bgallego.agenda_online.ListarNotas.Listar_Notas;
import com.bgallego.agenda_online.Objetos.Nota;
import com.bgallego.agenda_online.R;
import com.bgallego.agenda_online.ViewHolder.ViewHolder_Nota_Importante;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Notas_Importantes extends AppCompatActivity {

    RecyclerView RecyclerViewNotasImportantes;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference Mis_Usuarios;
    DatabaseReference Notas_Importantes;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    FirebaseRecyclerAdapter<Nota, ViewHolder_Nota_Importante> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Nota> firebaseRecyclerOptions;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas_archivadas);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Notas importantes");
            // Flecha hacia atrás
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        RecyclerViewNotasImportantes = findViewById(R.id.RecyclerViewNotasImportantes);
        RecyclerViewNotasImportantes.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Mis_Usuarios = firebaseDatabase.getReference("Usuarios");
        Notas_Importantes = firebaseDatabase.getReference("Mis notas importantes");

        ComprobarUsuario();

    }

    private void ComprobarUsuario() {
        if (user == null) {
            Toast.makeText(com.bgallego.agenda_online.NotasImportantes.Notas_Importantes.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        } else {
            ListarNotasImportantes();
        }
    }

    private void ListarNotasImportantes() {
        /* Hacemos la lectura de la BD "Usuarios", los identificamos por el "Uid" y
        posteriormente hacemos la lectura a la BD "Mis notas importantes". */
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Nota>().setQuery(Mis_Usuarios.child(user.getUid()).child("Mis notas importantes"), Nota.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nota, ViewHolder_Nota_Importante>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Nota_Importante viewHolder_nota_importante, int position, @NonNull Nota nota) {
                Log.d("Notas", "Contenido de la nota: " + nota);
                // Establece los datos de la nota en la vista correspondiente.
                viewHolder_nota_importante.SetearDatos(
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

            @Override
            public ViewHolder_Nota_Importante onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota_importante, parent, false);
                ViewHolder_Nota_Importante viewHolder_nota_importante = new ViewHolder_Nota_Importante(view);
                viewHolder_nota_importante.setOnClickListener(new ViewHolder_Nota_Importante.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        // Obtener los datos de la nota seleccionada
                        String id_nota = getItem(position).getId_nota();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_nota = getItem(position).getFecha_nota();
                        String estado = getItem(position).getEstado();

                        // Enviamos los datos a la siguiente actividad "Notas_Importantes"
                        Intent intent = new Intent(Notas_Importantes.this, Detalle_Nota.class);
                        intent.putExtra("id_nota", id_nota);
                        intent.putExtra("uid_usuario", uid_usuario);
                        intent.putExtra("correo_usuario", correo_usuario);
                        intent.putExtra("fecha_registro", fecha_registro);
                        intent.putExtra("titulo", titulo);
                        intent.putExtra("descripcion", descripcion);
                        intent.putExtra("fecha_nota", fecha_nota);
                        intent.putExtra("estado", estado);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewHolder_nota_importante;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Notas_Importantes.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true); // Listar desde el último registro al primero.
        linearLayoutManager.setStackFromEnd(true); // Al momento de listar empiece por la parte superior.

        RecyclerViewNotasImportantes.setLayoutManager(linearLayoutManager);
        RecyclerViewNotasImportantes.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onStart() {
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}