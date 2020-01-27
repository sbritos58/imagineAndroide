package com.santi.imagine.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.santi.imagine.R;

public class Principal extends AppCompatActivity {

    private Button btnDonador, btnVerDon,cerrarSesion,btnMapa;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        firebaseAuth = FirebaseAuth.getInstance();

        btnDonador = (Button)findViewById(R.id.btnDonador);
        cerrarSesion = (Button)findViewById(R.id.cerrarSesion);
        btnVerDon = (Button)findViewById(R.id.btnVerDon);
        btnMapa = (Button)findViewById(R.id.btnMapa);


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
