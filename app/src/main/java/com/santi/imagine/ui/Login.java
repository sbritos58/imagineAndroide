package com.santi.imagine.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.santi.imagine.R;

public class Login extends AppCompatActivity {

    Button btnRegistro;
    Button btnAcceder;
    ImageView imageView;
    EditText etEmail;
    EditText etContrasena;
    TextView tvContrasenaolvidada;
    LinearLayout linearLayout;
    private FirebaseAuth firebaseAuth;
    private String email,contra;
    boolean trylogin = false;
    private ProgressBar pb;
    private ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegistro = (Button)findViewById(R.id.BtnRegistro);
        btnAcceder = (Button)findViewById(R.id.BtnAcceder);
        imageView = (ImageView)findViewById(R.id.imageView);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etContrasena = (EditText)findViewById(R.id.etContrasena);
        tvContrasenaolvidada = (TextView)findViewById(R.id.tvContrasenaolvidada);
        linearLayout = (LinearLayout)findViewById(R.id.LinearLayout);
        pb = (ProgressBar)findViewById(R.id.pb);
        sv = (ScrollView)findViewById(R.id.sv);

        loginFormVisibility(true);
        firebaseAuth = FirebaseAuth.getInstance();
        linearLayout.setVisibility(View.GONE);



        /*----------------------------------------------INICIO HANDLER INGRESO DE PANTALLA--------------------------------------------*/
       new Handler().postDelayed(new Runnable(){
            public void run(){
                // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                Animation anim = AnimationUtils.loadAnimation(Login.this,R.anim.downtoup);
                if(linearLayout.getVisibility() == View.GONE){
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout.startAnimation(anim);
                }


            };
        },100);
/*----------------------------------------------FINAL HANDLER INGRESO DE PANTALLA--------------------------------------------*/



        /*---------------------------------------------------- INICIO Click con efecto en botones de login-------------------------------------------------*/
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation =AnimationUtils.loadAnimation(Login.this,R.anim.bounce);


                btnRegistro.startAnimation(animation);

/*------------------------------------------------------------INICO Este handler me permite demorar el cambio de pagina para ver el efecto del boton REGISTRO-------------------*/
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                        Intent intent = new Intent(Login.this,Registro.class);
                        startActivity(intent);
                    };
                }, 500);
/* ------------------------------------------------------------FIN DE HANDLER QUE PERMITE DEMORAR EL CAMBIO DE PAGINA PARA VER EFECTO BOTON REGISTRO*/


            }
        });



        tvContrasenaolvidada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation =AnimationUtils.loadAnimation(Login.this,R.anim.bounce);


                tvContrasenaolvidada.startAnimation(animation);

                /*------------------------------------------------------------INICO Este handler me permite demorar el cambio de pagina para ver el efecto del boton recuperar contraseña-------------------*/
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                        Intent intent = new Intent(Login.this,RecuperarContrasena.class);
                        startActivity(intent);
                    };
                }, 500);
                /* ------------------------------------------------------------FIN DE HANDLER QUE PERMITE DEMORAR EL CAMBIO DE PAGINA PARA VER EFECTO BOTON recueprar contraeña*/


            }
        });



        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Animation animation =AnimationUtils.loadAnimation(Login.this,R.anim.bounce);
                btnAcceder.startAnimation(animation);

                email = etEmail.getText().toString();
                contra = etContrasena.getText().toString();

                if(email.isEmpty()){
                    etEmail.setError("El email es obligatorio");
                }else if(contra.isEmpty()){
                    etContrasena.setError("La contraseña es obligatoria");

                }else {
                    loginUsuario();
                }




/*------------------------------------------------------------INICO Este handler me permite demorar el cambio de pagina para ver el efecto del boton ACCEDER-------------------*/


 /* ------------------------------------------------------------FIN DE HANDLER QUE PERMITE DEMORAR EL CAMBIO DE PAGINA PARA VER EFECTO BOTON ACCEDER*/




            }
        });
        /*---------------------------------------------------- FIN Click con efecto en botones de login-------------------------------------------------*/



    }

    private void loginFormVisibility(boolean showForm) {
            pb.setVisibility(showForm ? View.GONE : View.VISIBLE);
            sv.setVisibility(showForm ? View.VISIBLE : View.GONE);

    }

    private void loginUsuario() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if(email.matches(emailPattern) && email.length() > 0){
        firebaseAuth.signInWithEmailAndPassword(email,contra).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                trylogin = true;
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);

                }else{
                    Log.w("TAG","Error en inicio");    }
                    updateUI(null);
            }
        });
    }else{
            etEmail.setError("Escriba el email correctamente");
        }
    }

    private void updateUI(FirebaseUser user) {

        if(user != null){

            //Almacenar la informacion del usuario en firestore y navegar hacia el siguiente pantallaso
            new Handler().postDelayed(new Runnable(){
                public void run(){
                    // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                    Intent intent = new Intent(Login.this,Principal.class);
                    startActivity(intent);
                    finish();
                }
            }, 500);
        }else{
            if(trylogin){
                loginFormVisibility(true);
                etContrasena.setError("Email y/o contraseña incorrectos");
                etEmail.setText("");
                etContrasena.setText("");
                etEmail.requestFocus();

                }
            }
        }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
