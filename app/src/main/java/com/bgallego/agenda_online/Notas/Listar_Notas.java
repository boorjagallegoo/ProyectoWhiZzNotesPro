package com.bgallego.agenda_online.Notas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bgallego.agenda_online.Objetos.Nota;
import com.bgallego.agenda_online.R;
import com.bgallego.agenda_online.ViewHolder.ViewHolder_Nota;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Listar_Notas extends AppCompatActivity {

    RecyclerView recyclerviewNotas;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BD_Usuarios;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Nota, ViewHolder_Nota> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Nota> options;

    Dialog dialog, dialog_filtrar;

    FirebaseAuth auth;
    FirebaseUser user;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_notas);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Mis notas");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        recyclerviewNotas = findViewById(R.id.recyclerviewNotas);
        recyclerviewNotas.setHasFixedSize(true); // El RV se adaptara a los cambios que pueda tener la lista.

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser(); // Obtenemos al usuario que inicio sesión actualmente.

        dialog_filtrar = new Dialog(Listar_Notas.this);

        firebaseDatabase = FirebaseDatabase.getInstance(); // Obtener la instancia de FirebaseDatabase (Firebase en tiempo real).
        BD_Usuarios = firebaseDatabase.getReference("Usuarios");
        Estado_Filtro();

    }

    private void ListarTodasNotas() {
        // Consulta
        Query query = BD_Usuarios.child(user.getUid()).child("Notas_Publicadas").orderByChild("fecha_nota");
        options = new FirebaseRecyclerOptions.Builder<Nota>().setQuery(query, Nota.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nota, ViewHolder_Nota>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Nota viewHolder_nota, int position, @NonNull Nota nota) {
                // Imprime en la consola el contenido de la nota para propósitos de depuración.
                Log.d("Notas", "Contenido de la nota: " + nota.toString());
                // Establece los datos de la nota en la vista correspondiente.
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

            @Override
            public ViewHolder_Nota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflar la vista del elemento de la lista desde el diseño definido en R.layout.item_nota
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
                // Crear una instancia de ViewHolder_Nota, que representa y almacena las vistas dentro del elemento de la lista
                ViewHolder_Nota viewHolder_nota = new ViewHolder_Nota(view);

                viewHolder_nota.setOnClickListener(new ViewHolder_Nota.ClickListener() {
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

                        // Enviamos los datos a la siguiente actividad
                        Intent intent = new Intent(Listar_Notas.this, Detalle_Nota.class);
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
        linearLayoutManager.setReverseLayout(true); // Listar desde el último registro al primero.
        linearLayoutManager.setStackFromEnd(true); // Al momento de listar empiece por la parte superior.

        recyclerviewNotas.setLayoutManager(linearLayoutManager);
        recyclerviewNotas.setAdapter(firebaseRecyclerAdapter);

    }

    private void ListarNotasFinalizadas() {
        // Consulta
        String estado_nota = "Finalizado";
        Query query = BD_Usuarios.child(user.getUid()).child("Notas_Publicadas").orderByChild("estado").equalTo(estado_nota);
        options = new FirebaseRecyclerOptions.Builder<Nota>().setQuery(query, Nota.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nota, ViewHolder_Nota>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Nota viewHolder_nota, int position, @NonNull Nota nota) {
                // Imprime en la consola el contenido de la nota para propósitos de depuración.
                Log.d("Notas", "Contenido de la nota: " + nota.toString());
                // Establece los datos de la nota en la vista correspondiente.
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

            @Override
            public ViewHolder_Nota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflar la vista del elemento de la lista desde el diseño definido en R.layout.item_nota
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
                // Crear una instancia de ViewHolder_Nota, que representa y almacena las vistas dentro del elemento de la lista
                ViewHolder_Nota viewHolder_nota = new ViewHolder_Nota(view);

                viewHolder_nota.setOnClickListener(new ViewHolder_Nota.ClickListener() {
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

                        // Enviamos los datos a la siguiente actividad
                        Intent intent = new Intent(Listar_Notas.this, Detalle_Nota.class);
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
        linearLayoutManager.setReverseLayout(true); // Listar desde el último registro al primero.
        linearLayoutManager.setStackFromEnd(true); // Al momento de listar empiece por la parte superior.

        recyclerviewNotas.setLayoutManager(linearLayoutManager);
        recyclerviewNotas.setAdapter(firebaseRecyclerAdapter);

    }

    private void ListarNotasNoFinalizadas() {
        // Consulta
        String estado_nota = "No finalizado";
        Query query = BD_Usuarios.child(user.getUid()).child("Notas_Publicadas").orderByChild("estado").equalTo(estado_nota);
        options = new FirebaseRecyclerOptions.Builder<Nota>().setQuery(query, Nota.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nota, ViewHolder_Nota>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Nota viewHolder_nota, int position, @NonNull Nota nota) {
                // Imprime en la consola el contenido de la nota para propósitos de depuración.
                Log.d("Notas", "Contenido de la nota: " + nota.toString());
                // Establece los datos de la nota en la vista correspondiente.
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

            @Override
            public ViewHolder_Nota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflar la vista del elemento de la lista desde el diseño definido en R.layout.item_nota
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
                // Crear una instancia de ViewHolder_Nota, que representa y almacena las vistas dentro del elemento de la lista
                ViewHolder_Nota viewHolder_nota = new ViewHolder_Nota(view);

                viewHolder_nota.setOnClickListener(new ViewHolder_Nota.ClickListener() {
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

                        // Enviamos los datos a la siguiente actividad
                        Intent intent = new Intent(Listar_Notas.this, Detalle_Nota.class);
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
        linearLayoutManager.setReverseLayout(true); // Listar desde el último registro al primero.
        linearLayoutManager.setStackFromEnd(true); // Al momento de listar empiece por la parte superior.

        recyclerviewNotas.setLayoutManager(linearLayoutManager);
        recyclerviewNotas.setAdapter(firebaseRecyclerAdapter);

    }

    /**
     * Método para eliminar una nota mediante un cuadro de diálogo de confirmación.
     *
     * @param id_nota El identificador único de la nota que se desea eliminar.
     */
    private void EliminarNota(String id_nota) {

        // Construir el cuadro de diálogo de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Notas.this);
        builder.setTitle("Eliminar nota");
        builder.setMessage("¿Desea eliminar la nota?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ELIMINAR NOTA EN BD
                Query query = BD_Usuarios.child(user.getUid()).child("Notas_Publicadas").orderByChild("id_nota").equalTo(id_nota);

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

    private void Vaciar_Registro_De_Notas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Notas.this);
        builder.setTitle("Vaciar todos los registros");
        builder.setMessage("¿Estás seguro(a) de eliminar todas las notas?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Eliminación de todas las notas
                Query query = BD_Usuarios.child(user.getUid()).child("Notas_Publicadas");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Listar_Notas.this, "Todas las notas se han eliminado correctamente", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejo de error en la eliminación de notas
                    }
                });
            }
        });

        // Configuración del botón negativo (No)
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Listar_Notas.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        // Mostrar el diálogo
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notas, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Vaciar_Todas_Las_Notas) {
            Vaciar_Registro_De_Notas();
        }
        if (item.getItemId() == R.id.Filtrar_Notas) {
            FiltrarNotas();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening(); // Va ver si las notas son leídas correctamente.
        }
    }

    private void FiltrarNotas() {
        Button Todas_notas, Notas_Finalizadas, Notas_No_Finalizadas;

        dialog_filtrar.setContentView(R.layout.cuadro_dialogo_filtrar_notas);

        Todas_notas = dialog_filtrar.findViewById(R.id.Todas_notas);
        Notas_Finalizadas = dialog_filtrar.findViewById(R.id.Notas_Finalizadas);
        Notas_No_Finalizadas = dialog_filtrar.findViewById(R.id.Notas_No_Finalizadas);

        Todas_notas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Listar", "Todas");
                editor.apply();
                recreate(); // Guardamos la configuración
                Toast.makeText(Listar_Notas.this, "Todas las notas", Toast.LENGTH_SHORT).show();
                dialog_filtrar.dismiss();
            }
        });

        Notas_Finalizadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Listar", "Finalizados");
                editor.apply();
                recreate();
                Toast.makeText(Listar_Notas.this, "Notas finalizadas", Toast.LENGTH_SHORT).show();
                dialog_filtrar.dismiss();
            }
        });

        Notas_No_Finalizadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Listar", "No finalizados");
                editor.apply();
                recreate();
                Toast.makeText(Listar_Notas.this, "Notas no finalizadas", Toast.LENGTH_SHORT).show();
                dialog_filtrar.dismiss();
            }
        });

        dialog_filtrar.show();
    }

    private void Estado_Filtro() {
        sharedPreferences = Listar_Notas.this.getSharedPreferences("Notas", MODE_PRIVATE);

        String estado_filtro = sharedPreferences.getString("Listar", "Todas");

        switch (estado_filtro) {
            case "Todas":
                ListarTodasNotas();
                break;
            case "Finalizados":
                ListarNotasFinalizadas();
                break;
            case "No finalizados":
                ListarNotasNoFinalizadas();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
