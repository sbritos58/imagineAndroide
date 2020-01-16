package com.santi.imagine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.santi.imagine.R;
import com.santi.imagine.models.Usuarios;
import com.squareup.picasso.Picasso;

public class SolicitudProductos extends AppCompatActivity {

    TextView tv2,tv3,tv4,tv5;
    Button button;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    Usuarios usuario;
    Usuarios usuarioagra;
    ImageView im;
    String producto,tokenUsuario1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_productos);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        Bundle extras = getIntent().getExtras() ;
        tokenUsuario1 = extras.getString("Usuario");
        final String cantidad = extras.getString("Cantidad");
        final String imagen = extras.getString("Imagen");
        producto = extras.getString("Titulo");

        im = (ImageView)findViewById(R.id.iv);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);
        tv5 = (TextView)findViewById(R.id.tv5);

        button = (Button)findViewById(R.id.button);










        FirebaseUser datos_usuario = firebaseAuth.getCurrentUser();


        DocumentReference useragra = db.collection("Usuarios").document(datos_usuario.getUid());

        useragra.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuarioagra = documentSnapshot.toObject(Usuarios.class);

            }
        });


        DocumentReference docref = db.collection("Usuarios").document(tokenUsuario1);

        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario = documentSnapshot.toObject(Usuarios.class);
                tv2.setText("Gracias " +  usuarioagra.getNombreCompleto());
                tv3.setText("Le has solicitado a " + usuario.getNombreCompleto());
                tv4.setText(producto);
                tv5.setText(cantidad + "unidades");
                Picasso.get().load(imagen).into(im);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SolicitudProductos.this, Principal.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("Productos").document(producto+"_"+tokenUsuario1)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        Log.i("PRODUCTO BORRADO CON EXITO","se ha borrado el producto " + producto);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,Principal.class);
        startActivity(i);
    }


}
