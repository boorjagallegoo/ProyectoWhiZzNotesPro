package com.bgallego.agenda_online.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bgallego.agenda_online.R;

/**
 * ViewHolder_Nota es una clase que extiende RecyclerView.ViewHolder y se utiliza para representar
 * y manejar la vista de un elemento de nota en un RecyclerView.
 */
public class ViewHolder_Nota extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolder_Nota.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position); /* SE EJECUTA AL PRESIONAR EN EL ITEM */
        void onItemLongClick(View view, int position); /* SE EJECUTA AL MANTENER PRESIONADO EL ITEM */
    }

    public void setOnClickListener(ViewHolder_Nota.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    /**
     * Constructor que inicializa la vista de nota y establece los escuchadores de clics.
     *
     * @param itemView La vista de nota inflada.
     */
    public ViewHolder_Nota(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        // Establecemos los eventos de los métodos
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getBindingAdapterPosition());
                return false;
            }
        });
    }

    /**
     * Método que establece los datos en la vista de nota.
     *
     * @param context              El contexto de la aplicación.
     * @param id_nota              El ID de la nota.
     * @param uid_usuario          El ID del usuario.
     * @param correo_usuario       El correo electrónico del usuario.
     * @param fecha_hora_registro  La fecha y hora de registro de la nota.
     * @param titulo               El título de la nota.
     * @param descripcion          La descripción de la nota.
     * @param fecha_nota           La fecha de la nota.
     * @param estado               El estado de la nota.
     */
    public void SetearDatos(Context context, String id_nota, String uid_usuario, String correo_usuario, String fecha_hora_registro,
                            String titulo, String descripcion, String fecha_nota, String estado) {

        // DECLARAR LAS VISTAS
        TextView Id_nota_Item, Uid_Usuario_Item, Correo_usuario_Item, Fecha_hora_registro_Item, Titulo_Item,
                Descripcion_Item, Fecha_Item, Estado_Item;

        ImageView Tarea_Finalizada_Item, Tarea_No_Finalizada_Item;

        // ESTABLECER CONEXIÓN CON EL ITEM
        Id_nota_Item = mView.findViewById(R.id.Id_nota_Item);
        Uid_Usuario_Item = mView.findViewById(R.id.Uid_Usuario_Item);
        Correo_usuario_Item = mView.findViewById(R.id.Correo_usuario_Item);
        Fecha_hora_registro_Item = mView.findViewById(R.id.Fecha_hora_registro_Item);
        Titulo_Item = mView.findViewById(R.id.Titulo_Item);
        Descripcion_Item = mView.findViewById(R.id.Descripcion_Item);
        Fecha_Item = mView.findViewById(R.id.Fecha_Item);
        Estado_Item = mView.findViewById(R.id.Estado_Item);

        Tarea_Finalizada_Item = mView.findViewById(R.id.Tarea_Finalizada_Item);
        Tarea_No_Finalizada_Item = mView.findViewById(R.id.Tarea_No_Finalizada_Item);

        // SETEAR LA INFORMACIÓN DENTRO DEL ITEM
        Id_nota_Item.setText(id_nota);
        Uid_Usuario_Item.setText(uid_usuario);
        Correo_usuario_Item.setText(correo_usuario);
        Fecha_hora_registro_Item.setText(fecha_hora_registro);
        Titulo_Item.setText(titulo);
        Descripcion_Item.setText(descripcion);
        Fecha_Item.setText(fecha_nota);
        Estado_Item.setText(estado);

        // GESTIONAMOS EL COLOR DEL ESTADO
        if (estado.equals("Finalizado")) {
            Tarea_Finalizada_Item.setVisibility(View.VISIBLE);
        } else {
            Tarea_No_Finalizada_Item.setVisibility(View.VISIBLE);
        }

    }
}
