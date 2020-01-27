package com.santi.imagine.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.santi.imagine.R;

public class RecuperarContrasena extends AppCompatActivity {

    private EditText etEmail;
    private Button btnRecuperar;
    FirebaseAuth firebaseAuth;
    String email;
    ProgressBar pb2;
    ScrollView sv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        etEmail = (EditText)findViewById(R.id.etEmail);
        btnRecuperar = (Button)findViewById(R.id.btnRecuperar);
        pb2 = (ProgressBar)findViewById(R.id.pb2);
        sv = (ScrollView)findViewById(R.id.sv);

        recuperarFormVisibility(true);



        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recuperarFormVisibility(false);
                Animation animation = AnimationUtils.loadAnimation(RecuperarContrasena.this, R.anim.bounce);
                btnRecuperar.startAnimation(animation);

                email = etEmail.getText().toString();

                if (!email.isEmpty()){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RecuperarContrasena.this, "Email enviado correctamente", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RecuperarContrasena.this, Login.class);
                            startActivity(i);
                            finish();
                        } else {
                            recuperarFormVisibility(true);
                            etEmail.setError("Email no existe");
                            etEmail.setText("");
                        }
                    }
                });
            }else{
                    recuperarFormVisibility(true);
                    etEmail.setError("Ingrese algun email Válido");
                    etEmail.requestFocus();
                }

            }
        });

    }
    private void recuperarFormVisibility(boolean showForm) {
        pb2.setVisibility(showForm ? View.GONE : View.VISIBLE);
        sv.setVisibility(showForm ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder cerrar = new AlertDialog.Builder(RecuperarContrasena.this);
        cerrar.setTitle("Volver");
        cerrar.setMessage("¿Estas seguro de querer volver?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(RecuperarContrasena.this,Principal.class));
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
