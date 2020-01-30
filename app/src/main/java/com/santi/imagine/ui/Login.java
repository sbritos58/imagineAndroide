package com.santi.imagine.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
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
    LottieAnimationView subirImagen;

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
        sv = (ScrollView)findViewById(R.id.sv);
        subirImagen = (LottieAnimationView)findViewById(R.id.subirImagen);

        loginFormVisibility(true);
        firebaseAuth = FirebaseAuth.getInstance();
        linearLayout.setVisibility(View.GONE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            verifyPermission();
        }

        int permissionCheck = ContextCompat.checkSelfPermission(Login.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



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
                    loginFormVisibility(false);

                    loginUsuario();
                }




/*------------------------------------------------------------INICO Este handler me permite demorar el cambio de pagina para ver el efecto del boton ACCEDER-------------------*/


 /* ------------------------------------------------------------FIN DE HANDLER QUE PERMITE DEMORAR EL CAMBIO DE PAGINA PARA VER EFECTO BOTON ACCEDER*/




            }
        });
        /*---------------------------------------------------- FIN Click con efecto en botones de login-------------------------------------------------*/



    }

        private void loginFormVisibility(boolean showForm) {
                subirImagen.setVisibility(showForm ? View.GONE : View.VISIBLE);
                subirImagen.playAnimation();
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
        loginFormVisibility(true);
    }

    private void updateUI(FirebaseUser user) {

        if(user != null){

            //Almacenar la informacion del usuario en firestore y navegar hacia el siguiente pantallaso
            new Handler().postDelayed(new Runnable(){
                public void run(){
                    loginFormVisibility(true);
                    // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                    Intent intent = new Intent(Login.this,Principal.class);
                    startActivity(intent);
                    finish();
                }
            }, 10);
        }else{
            if(trylogin){


                new Handler().postDelayed(new Runnable(){
                    public void run(){

                        loginFormVisibility(true);
                        etContrasena.setError("Email y/o contraseña incorrectos");
                        etEmail.setText("");
                        etContrasena.setText("");
                        etEmail.requestFocus();

                    }
                }, 200);

                }
            }
        }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);


    }

    private void verifyPermission() {
        int permsRequestCode = 100;
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        int accessFinePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int aceso = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int aceso2= checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int accessCoarsePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);



        if (accessFinePermission == PackageManager.PERMISSION_GRANTED &&
                accessCoarsePermission == PackageManager.PERMISSION_GRANTED
            && aceso==PackageManager.PERMISSION_GRANTED &&
                aceso2 == PackageManager.PERMISSION_GRANTED) {
            //se realiza metodo si es necesario...
        } else {
            requestPermissions(perms, permsRequestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                Toast.makeText(this, "Permisos Aceptados", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder cerrar = new AlertDialog.Builder(Login.this);
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
