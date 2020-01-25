package com.santi.imagine.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class Solicitud2 extends AppCompatActivity {

    TextView tv2,tv3,tv4,tv5;
    Button button,solicitar,volver;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    Usuarios usuario;
    Usuarios usuarioagra;
    ImageView im;
    String producto,tokenUsuario1;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitud2);

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
        pb = (ProgressBar) findViewById(R.id.pb);
        solicitar = (Button)findViewById(R.id.btnSolicitar);
        volver = (Button)findViewById(R.id.btnVolver);

        progressbar(true);

        FirebaseUser datos_usuario = firebaseAuth.getCurrentUser();

        DocumentReference useragra = db.collection("Usuarios").document(datos_usuario.getUid());

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbar(true);
                final Intent email = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                email.putExtra(Intent.EXTRA_EMAIL,new String[]{usuario.getEmail()});
                email.putExtra(Intent.EXTRA_TEXT,"Contacte a su donador");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email,"Elige un cliente de email"));
                db.collection("Productos").document(producto+"_"+tokenUsuario1)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.i("PRODUCTO BORRADO CON EXITO","se ha borrado el producto " + producto);
                            }
                        });
                finish();
            }
        });

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
                tv2.setText("Gracias por usar nuestro sevicio " +  usuarioagra.getNombreCompleto());
                tv3.setText("Estas por solicitarle a " + usuario.getNombreCompleto());
                tv4.setText(producto);
                tv5.setText(cantidad + " unidades");
                Picasso.get().load(imagen).into(im);

            }
        });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,Principal.class);
        startActivity(i);
    }

    private void progressbar(boolean showForm) {
        pb.setVisibility(showForm ? View.GONE : View.VISIBLE);
    }
}

