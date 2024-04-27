package com.bgallego.agenda_online;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.bgallego.agenda_online.Configuracion.Configuracion;
import com.bgallego.agenda_online.Contactos.Listar_Contactos;
import com.bgallego.agenda_online.Notas.Agregar_Nota;
import com.bgallego.agenda_online.Notas.Listar_Notas;
import com.bgallego.agenda_online.Notas.Notas_Importantes;
import com.bgallego.agenda_online.Perfil.Perfil_Usuario;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuPrincipal extends AppCompatActivity {

    Button AgregarNotas, ListarNotas, Importantes, Contactos, AcercaDe, CerrarSesion;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ImageView Imagen_usuario;
    TextView UidPrincipal, NombresPrincipal, CorreoPrincipal;
    Button EstadoCuentaPrincipal;
    ProgressBar progressBarDatos;
    ProgressDialog progressDialog;
    LinearLayoutCompat Linear_Nombres, Linear_Correo, Linear_Verificacion;

    DatabaseReference Usuarios; // Llamamos a nuestra Base de datos (Usuarios).

    Dialog dialog_cuenta_verificada, dialog_informacion, dialog_fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        Imagen_usuario = findViewById(R.id.Imagen_usuario);
        UidPrincipal = findViewById(R.id.UidPrincipal);
        NombresPrincipal = findViewById(R.id.NombresPrincipal);
        CorreoPrincipal = findViewById(R.id.CorreoPrincipal);
        EstadoCuentaPrincipal = findViewById(R.id.EstadoCuentaPrincipal);
        progressBarDatos = findViewById(R.id.progressBarDatos);

        dialog_cuenta_verificada = new Dialog(this);
        dialog_informacion = new Dialog(this);
        dialog_fecha = new Dialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor ...");
        progressDialog.setCanceledOnTouchOutside(false);

        Linear_Nombres = findViewById(R.id.Linear_Nombres);
        Linear_Correo = findViewById(R.id.Linear_Correo);
        Linear_Verificacion = findViewById(R.id.Linear_Verificacion);


        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        AgregarNotas = findViewById(R.id.AgregarNotas);
        ListarNotas = findViewById(R.id.ListarNotas);
        Importantes = findViewById(R.id.Importantes);
        Contactos = findViewById(R.id.Contactos);
        AcercaDe = findViewById(R.id.AcercaDe);
        CerrarSesion = findViewById(R.id.CerrarSesion);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Evento para el botón Verificar Usuario.
        EstadoCuentaPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isEmailVerified()) {
                    // Si la cuenta está verificada
                    // Toast.makeText(MenuPrincipal.this, "Cuenta ya verificada", Toast.LENGTH_SHORT);
                    AnimacionCuentaVerificada();
                } else {
                    // Si la cuenta no está verificada
                    VerificarCuentaCorreo();
                }
            }
        });

        // Inicio de la Activitys
        AgregarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Obtenemos la información de los TextView */
                String uid_usuario = UidPrincipal.getText().toString();
                String correo_usuario = CorreoPrincipal.getText().toString();

                /* Pasamos los datos a la siguiente actividad */
                Intent intent = new Intent(MenuPrincipal.this, Agregar_Nota.class);
                intent.putExtra("Uid", uid_usuario);
                intent.putExtra("Correo", correo_usuario);
                startActivity(intent);
            }
        });

        ListarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, Listar_Notas.class));
                Toast.makeText(MenuPrincipal.this, "Listar Notas", Toast.LENGTH_SHORT).show();
            }
        });

        Importantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, Notas_Importantes.class));
                Toast.makeText(MenuPrincipal.this, "Notas Archivadas", Toast.LENGTH_SHORT).show();
            }
        });

        Contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MenuPrincipal.this, "Contactos", Toast.LENGTH_SHORT).show();
                /*Obteniendo el dato uid del usuario*/
                String uid_usuario = UidPrincipal.getText().toString();
                Intent intent = new Intent(MenuPrincipal.this, Listar_Contactos.class);
                /*Enviamos el dato a la siguiente actividad*/
                intent.putExtra("Uid", uid_usuario);
                startActivity(intent);
            }
        });

        AcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Informacion();
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalirAplicacion();
            }
        });
    }

    /**
     * Método que muestra un cuadro de diálogo para confirmar el envío de instrucciones de verificación al correo electrónico del usuario.
     *
     * @distinctiveSection Verificar usuario por correo electrónico
     */
    private void VerificarCuentaCorreo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verificar cuenta")
                .setMessage("¿Estás seguro(a) de enviar instrucciones de verificación a su correo electrónico? " + user.getEmail())
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        EnviarCorreoAVerificacion();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // Agregar el método show() para mostrar el Toast
                        Toast.makeText(MenuPrincipal.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    /**
     * Método que envía instrucciones de verificación al correo electrónico del usuario y muestra mensajes de éxito o error.
     *
     * @distinctiveSection Verificar usuario por correo electrónico
     */
    private void EnviarCorreoAVerificacion() {
        progressDialog.setMessage("Enviando instrucciones de verificación a su correo electrónico " + user.getEmail());
        progressDialog.show();

        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // El envío fue exitoso
                        progressDialog.dismiss();
                        Toast.makeText(MenuPrincipal.this, "Instrucciones enviadas, revise su bandeja de correo " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Falló el envío
                        Toast.makeText(MenuPrincipal.this, "Falló debido a: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Método que verifica el estado de la cuenta del usuario y actualiza la interfaz de usuario en consecuencia.
     *
     * @distinctiveSection Verificar usuario por correo electrónico
     */
    private void VerificarEstadoCuenta() {
        String Verificado = "Verificado";
        String No_Verificado = "No Verificado";
        if (user.isEmailVerified()) {
            EstadoCuentaPrincipal.setText(Verificado);
            EstadoCuentaPrincipal.setBackgroundColor(Color.rgb(114, 220, 41));
        } else {
            EstadoCuentaPrincipal.setText(No_Verificado);
            EstadoCuentaPrincipal.setBackgroundColor(Color.rgb(231, 76, 60));
        }
    }

    private void AnimacionCuentaVerificada() {
        Button EntendidoVerificado;

        dialog_cuenta_verificada.setContentView(R.layout.dialogo_cuenta_verificada);

        EntendidoVerificado = dialog_cuenta_verificada.findViewById(R.id.EntendidoVerificado);

        EntendidoVerificado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_cuenta_verificada.dismiss();
            }
        });
        dialog_cuenta_verificada.show();
        dialog_cuenta_verificada.setCanceledOnTouchOutside(false);
    }

    private void Informacion() {

        // ImageView Ir_facebook, Ir_instagram, Ir_youtube, Ir_twitter;
        Button EntendidoInfo;

        dialog_informacion.setContentView(R.layout.cuadro_dialogo_informacion);

      /*  Ir_facebook = dialog_informacion.findViewById(R.id.Ir_facebook);
        Ir_instagram = dialog_informacion.findViewById(R.id.Ir_instagram);
        Ir_youtube = dialog_informacion.findViewById(R.id.Ir_youtube);
        Ir_twitter = dialog_informacion.findViewById(R.id.Ir_twitter);
        */

        EntendidoInfo = dialog_informacion.findViewById(R.id.EntendidoInfo);

      /*  Ir_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir_p_facebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"));
                startActivity(ir_p_facebook);
            }
        });

        Ir_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir_p_instagram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"));
                startActivity(ir_p_instagram);
            }
        });

        Ir_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir_p_youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"));
                startActivity(ir_p_youtube);
            }
        });

        Ir_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir_p_twitter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com"));
                startActivity(ir_p_twitter);
            }
        });
*/
        EntendidoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_informacion.dismiss();
            }
        });

        dialog_informacion.show();
        dialog_informacion.setCanceledOnTouchOutside(false);
    }

    private void VisualizarFecha() {
        TextView Fecha_hoy;
        Button Btn_cerrar;

        dialog_fecha.setContentView(R.layout.cuadro_dialogo_fecha);

        Fecha_hoy = dialog_fecha.findViewById(R.id.Fecha_hoy);
        Btn_cerrar = dialog_fecha.findViewById(R.id.Btn_cerrar);

        /* Obtener fecha actual */
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d 'de' MMMM 'del' yyyy"); // 27 de abril del 2024
        String fecha = simpleDateFormat.format(date);
        Fecha_hoy.setText(fecha);

        Btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_fecha.dismiss();
            }
        });

        dialog_fecha.show();
        dialog_fecha.setCanceledOnTouchOutside(false);
    }

    /**
     * Método que se llama cuando la actividad está a punto de hacerse visible para el usuario.
     * Realiza la comprobación de inicio de sesión y ejecuta las acciones correspondientes.
     */
    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();
    }

    /**
     * Comprueba si el usuario ha iniciado sesión. Si ha iniciado sesión, carga los datos.
     * Si no ha iniciado sesión, redirige al usuario a la actividad principal.
     */
    private void ComprobarInicioSesion() {
        if (user != null) {
            // El usuario ha iniciado sesión
            CargaDeDatos();
        } else {
            // Lo dirigirá al MainActivity
            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }

    /**
     * Carga los datos del usuario desde la base de datos y actualiza la interfaz de usuario.
     */
    private void CargaDeDatos() {

        VerificarEstadoCuenta();
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Si el usuario existe
                if (snapshot.exists()) {
                    // El progressbar se oculta.
                    progressBarDatos.setVisibility(View.GONE);
                    // Los TextViews se muestran
                    // UidPrincipal.setVisibility(View.VISIBLE);
                    // NombresPrincipal.setVisibility(View.VISIBLE);
                    // CorreoPrincipal.setVisibility(View.VISIBLE);
                    Linear_Nombres.setVisibility(View.VISIBLE);
                    Linear_Correo.setVisibility(View.VISIBLE);
                    Linear_Verificacion.setVisibility(View.VISIBLE);

                    // Obtener los datos
                    String uid = "" + snapshot.child("uid").getValue();
                    String nombres = "" + snapshot.child("nombre").getValue();
                    String correo = "" + snapshot.child("correo").getValue();
                    String imagen = "" + snapshot.child("imagen_perfil").getValue();

                    // Setear los datos en los respectivos TextView
                    UidPrincipal.setText(uid);
                    NombresPrincipal.setText(nombres);
                    CorreoPrincipal.setText(correo);

                    // Habilitar los botones del menú
                    AgregarNotas.setEnabled(true);
                    ListarNotas.setEnabled(true);
                    Importantes.setEnabled(true);
                    Contactos.setEnabled(true);
                    AcercaDe.setEnabled(true);
                    CerrarSesion.setEnabled(true);

                    ObtenerImagen(imagen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void ObtenerImagen(String imagen) {
        try {
            // Si la imagen se ha traido con éxito
            Glide.with(getApplicationContext()).load(imagen).placeholder(R.drawable.imagen_usuario).into(Imagen_usuario);
        } catch (Exception e) {
            // Si la imagen no fue traida con éxito
            Glide.with(getApplicationContext()).load(R.drawable.imagen_usuario).into(Imagen_usuario);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Perfil_usuario) {
            startActivity(new Intent(MenuPrincipal.this, Perfil_Usuario.class));
        }
        if (item.getItemId() == R.id.Calendario) {
            VisualizarFecha();
        }
        if (item.getItemId() == R.id.Configuracion) {
            String uid_usuario = UidPrincipal.getText().toString();

            Intent intent = new Intent(MenuPrincipal.this, Configuracion.class);
            intent.putExtra("Uid", uid_usuario);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Cierra la sesión actual del usuario y redirige a la actividad principal.
     */
    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesión exitosamente", Toast.LENGTH_SHORT).show();
    }
}