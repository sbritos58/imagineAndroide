package com.santi.imagine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.santi.imagine.R;

public class Principal extends AppCompatActivity {

    private Button btnDonador, btnVerDon,cerrarSesion;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



        firebaseAuth = FirebaseAuth.getInstance();


        btnDonador = (Button)findViewById(R.id.btnDonador);
        cerrarSesion = (Button)findViewById(R.id.cerrarSesion);
        btnVerDon = (Button)findViewById(R.id.btnVerDon);


        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent i = new Intent(Principal.this,Login.class);
                startActivity(i);
            }
        });

        btnDonador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(Principal.this,R.anim.bounce);

                btnDonador.startAnimation(animation);

                Intent i = new Intent(Principal.this,AgregarProducto.class);
                startActivity(i);
                finish();

            }
        });
        btnVerDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(Principal.this,R.anim.bounce);

                btnVerDon.startAnimation(animation);

                Intent i = new Intent(Principal.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });

    }


}
