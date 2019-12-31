package com.santi.imagine.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.santi.imagine.R;

public class Principal extends AppCompatActivity {

    private Button btnDonador, btnVerDon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);





        btnDonador = (Button)findViewById(R.id.btnDonador);
        btnVerDon = (Button)findViewById(R.id.btnVerDon);

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

                Intent i = new Intent(Principal.this,Login.class);
                startActivity(i);
                finish();

            }
        });

    }


}
