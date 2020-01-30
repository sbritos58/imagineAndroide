package com.santi.imagine.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.santi.imagine.R;

public class Principal extends AppCompatActivity {

    private Button btnDonador, btnVerDon,cerrarSesion,btnMapa;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private int cantidad;
    private TextView textView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        firebaseAuth = FirebaseAuth.getInstance();

        btnDonador = (Button)findViewById(R.id.btnDonador);
        cerrarSesion = (Button)findViewById(R.id.cerrarSesion);
        btnVerDon = (Button)findViewById(R.id.btnVerDon);
        btnMapa = (Button)findViewById(R.id.btnMapa);
        textView3 = (TextView)findViewById(R.id.textView3);


        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Productos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    cantidad = task.getResult().size();

                    switch (cantidad){
                        case 0:
                            textView3.setText("En este momento no tenemos donaciones disponibles");
                            break;
                        case 1:
                            textView3.setText(String.valueOf("En este momento tenemos " + cantidad+" donación disponible"));
                            break;
                        default:
                            textView3.setText(String.valueOf("En este momento tenemos " + cantidad+" donaciones disponibles"));
                            break;
                    }

                }
            }
        });



        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(Principal.this,R.anim.bounce);

                btnMapa.startAnimation(animation);

                startActivity(new Intent(Principal.this, MapitaMostrar.class));
            }
        });


        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(Principal.this,R.anim.bounce);

                cerrarSesion.startAnimation(animation);
                AlertDialog.Builder cerrar = new AlertDialog.Builder(Principal.this);
                cerrar.setTitle("Volver");
                cerrar.setMessage("¿Estas seguro de cerrar sesión?").setCancelable(false).setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(Principal.this,Login.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.cancel();
                    }
                });
                cerrar.show();


            }
        });

        btnDonador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(Principal.this,R.anim.bounce);

                btnDonador.startAnimation(animation);

                Intent i = new Intent(Principal.this,AgregarProducto.class);
                startActivity(i);

            }
        });
        btnVerDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(Principal.this,R.anim.bounce);

                btnVerDon.startAnimation(animation);

                Intent i = new Intent(Principal.this,MainActivity.class);
                startActivityForResult(i,10);


            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder cerrar = new AlertDialog.Builder(Principal.this);
        cerrar.setTitle("Volver");
        cerrar.setMessage("¿Estas seguro de querer salir de la aplicación?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();

            }
        });
        cerrar.show();
    }


}



























