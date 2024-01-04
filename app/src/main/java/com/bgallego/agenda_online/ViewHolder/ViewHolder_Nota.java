package com.bgallego.agenda_online.ViewHolder;

import android.content.Context;
import android.view.View;
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

    /**
     * Interfaz que define los métodos a ser implementados por los escuchadores de clics en la vista de nota.
     */
    public interface ClickListener {
        /**
         * Se ejecuta al hacer clic en el elemento.
         *
         * @param view     La vista que recibió el clic.
         * @param position La posición del elemento en el adaptador.
         */
        void onItemClick(View view, int position);

        /**
         * Se ejecuta al mantener presionado el elemento.
         *
         * @param view     La vista que recibió el clic largo.
         * @param position La posición del elemento en el adaptador.
         */
        void onItemLongClick(View view, int position);
    }

    /**
     * Establece el escuchador de clics para la vista de nota.
     *
     * @param clickListener El objeto que implementa la interfaz ClickListener.
     */
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

        // ESTABLECER CONEXIÓN CON EL ITEM
        Id_nota_Item = mView.findViewById(R.id.Id_nota_Item);
        Uid_Usuario_Item = mView.findViewById(R.id.Uid_Usuario_Item);
        Correo_usuario_Item = mView.findViewById(R.id.Correo_usuario_Item);
        Fecha_hora_registro_Item = mView.findViewById(R.id.Fecha_hora_registro_Item);
        Titulo_Item = mView.findViewById(R.id.Titulo_Item);
        Descripcion_Item = mView.findViewById(R.id.Descripcion_Item);
        Fecha_Item = mView.findViewById(R.id.Fecha_Item);
        Estado_Item = mView.findViewById(R.id.Estado_Item);

        // SETEAR LA INFORMACIÓN DENTRO DEL ITEM
        Id_nota_Item.setText(id_nota);
        Uid_Usuario_Item.setText(uid_usuario);
        Correo_usuario_Item.setText(correo_usuario);
        Fecha_hora_registro_Item.setText(fecha_hora_registro);
        Titulo_Item.setText(titulo);
        Descripcion_Item.setText(descripcion);
        Fecha_Item.setText(fecha_nota);
        Estado_Item.setText(estado);

    }
}
