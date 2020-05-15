package com.santi.imagine.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.santi.imagine.R;
import com.santi.imagine.models.AdapterProductos;
import com.squareup.picasso.Picasso;

public class MyAdapter extends FirestoreRecyclerAdapter<AdapterProductos, MyAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyAdapter(@NonNull FirestoreRecyclerOptions<AdapterProductos> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull AdapterProductos articulo) {

        holder.titulo.setText(articulo.getProducto());
        holder.descripcion.setText("("+articulo.getCantidad() + ") " + articulo.getDescripcion());
        Picasso.get().load(articulo.getUrl()).into(holder.profilePic);
        holder.tokenUsuario.setText(articulo.getTokenUsuario());
        holder.cantidad.setText(articulo.getCantidad());
        holder.foto.setText(articulo.getUrl());
        holder.setOnClickListeners();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        ImageView profilePic;
        TextView titulo;
        TextView descripcion;
        Button solicitar;
        TextView tokenUsuario;
        TextView cantidad;
        TextView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            profilePic = itemView.findViewById(R.id.profilePic);
            titulo = itemView.findViewById(R.id.titulo);
            descripcion = itemView.findViewById(R.id.descripcion);
            solicitar = itemView.findViewById(R.id.solicitar);
            tokenUsuario = itemView.findViewById(R.id.tokenUsuario);
            foto = itemView.findViewById(R.id.foto);
            cantidad = itemView.findViewById(R.id.cantidad);
        }

        public void setOnClickListeners() {
            solicitar.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.solicitar:
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    solicitar.startAnimation(animation);

                    Intent intent = new Intent(context, SolicitudProductos.class);
                    intent.putExtra("Usuario", tokenUsuario.getText());
                    intent.putExtra("Cantidad", cantidad.getText());
                    intent.putExtra("Imagen", foto.getText());
                    intent.putExtra("Titulo", titulo.getText());
                    context.startActivity(intent);
                    /*Para capturar eventos de la vista lo realizamos aca*/


            }
        }
    }

}
