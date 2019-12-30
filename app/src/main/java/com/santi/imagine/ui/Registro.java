package com.santi.imagine.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.santi.imagine.R;
import com.santi.imagine.models.Usuarios;


public class Registro extends AppCompatActivity {

    private Button btnRegistro;
    private EditText etNombreCompleto;
    private EditText etNombreOrg;
    private EditText etEmail;
    private EditText etTelefono;
    private EditText etFecha;
    private EditText etContra;
    private EditText etRepetContra;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private String nombreCompleto,nombreOrg,email,telefono,fecha,contrasena,repetirContra;
    private ProgressBar pb;
    private ScrollView sv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //instanciar la conexion a firestore
        db = FirebaseFirestore.getInstance();


        btnRegistro = (Button)findViewById(R.id.btnRegistro);
        etNombreCompleto= (EditText)findViewById(R.id.etNombreCompleto);
        etNombreOrg= (EditText)findViewById(R.id.etNombreOrg);
        etEmail= (EditText)findViewById(R.id.etEmail);
        etTelefono= (EditText)findViewById(R.id.etTelefono);
        etFecha= (EditText)findViewById(R.id.etFecha);
        etContra= (EditText)findViewById(R.id.etContra);
        etRepetContra= (EditText)findViewById(R.id.etRepetContra);
        firebaseAuth = FirebaseAuth.getInstance();
        pb = (ProgressBar)findViewById(R.id.pb);
        sv = (ScrollView)findViewById(R.id.sv);

        db = FirebaseFirestore.getInstance();
        registroFormVisibility(true);


        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation = AnimationUtils.loadAnimation(Registro.this,R.anim.bounce);


                btnRegistro.startAnimation(animation);


                nombreCompleto = etNombreCompleto.getText().toString();
                nombreOrg= etNombreOrg.getText().toString();
                email= etEmail.getText().toString();
                telefono= etTelefono.getText().toString();
                fecha = etFecha.getText().toString();
                contrasena = etContra.getText().toString();
                repetirContra= etRepetContra.getText().toString();


                if(nombreCompleto.isEmpty() || nombreOrg.isEmpty() || email.isEmpty() || telefono.isEmpty() || fecha.isEmpty()
                        || contrasena.isEmpty() || repetirContra.isEmpty()){
                    Toast.makeText(Registro.this, "Rellene todos los datos del formulario", Toast.LENGTH_SHORT).show();

                }else{
                    if (contrasena.equals(repetirContra)){
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                        if(email.matches(emailPattern) && email.length() > 0) {

                            if(Integer.parseInt(fecha)> 17){
                                crearUsuario();
                            }else{
                                etFecha.setError("Debes ser mayor de edad");
                                etFecha.requestFocus();
                                etFecha.selectAll();
                            }



                        }else{
                            etEmail.setError("El email es incorrecto");
                        }

                    }else{
                        etContra.setError("Las contraseñas no coinciden");
                        etRepetContra.setText("");
                        etContra.setText("");
                        etContra.requestFocus();
                    }



                }

            }

        });

    }

    private void crearUsuario() {
        registroFormVisibility(false);
        firebaseAuth.createUserWithEmailAndPassword(email,contrasena).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);

                }else{
                    Toast.makeText(Registro.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){

            Usuarios nuevoUsuario = new Usuarios(nombreOrg,nombreCompleto,email,telefono,fecha,contrasena);

            db.collection("Usuarios").document(user.getUid()).set(nuevoUsuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent i = new Intent(Registro.this,Login.class);
                    startActivity(i);
                    finish();
                }
            });

            //Almacenar la informacion del usuario en firestore y navegar hacia el siguiente pantallaso

        }else{
            registroFormVisibility(true);
            Toast.makeText(this, "Error, registre sus datos de nuevo", Toast.LENGTH_SHORT).show();
            etRepetContra.setText("");
            etContra.setText("");
            etNombreCompleto.setText("");
            etNombreOrg.setText("");
            etEmail.setText("");
            etTelefono.setText("");
            etNombreCompleto.requestFocus();
        }


    }
    private void registroFormVisibility(boolean showForm) {
        pb.setVisibility(showForm ? View.GONE : View.VISIBLE);
        sv.setVisibility(showForm ? View.VISIBLE : View.GONE);

    }
}