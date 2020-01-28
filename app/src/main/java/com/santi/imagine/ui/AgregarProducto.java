package com.santi.imagine.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santi.imagine.models.Productos;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.santi.imagine.R.anim;
import static com.santi.imagine.R.id;
import static com.santi.imagine.R.layout;


public class AgregarProducto extends AppCompatActivity {


    private static final int IMAGE_CAPTURE_CODE = 1001;
    Button btnFoto,btnDonar,btnSeleccionarUbicacion;
    EditText etProducto,etCantidad,etUbicacion,etDescripcion;
    private StorageReference storageReference;
    EditText etPais;
    private static final int GALLERY_INTENT = 1;
    String producto,cantidad,pais,ubicacion,descripcion;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String urlFotos,tokenUsuario;
    static final int REQUEST_IMAGE_CAPTURE = 10;
    ProgressBar progressBar;
    String latitude,longitude,country,ciudad,direccion,estado,codigoPostal;
    private int CODIGO_BTN_SELECCIONAR = 58;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_agregar_producto);

        btnFoto = (Button)findViewById(id.btnFoto);
        btnDonar = (Button)findViewById(id.btnDonar);
        etProducto = (EditText)findViewById(id.etProducto);
        etCantidad = (EditText)findViewById(id.etCantidad);
        etPais = (EditText) findViewById(id.etPais);
        etUbicacion = (EditText)findViewById(id.etUbicacion);
        progressBar = (ProgressBar)findViewById(id.progressBar);
        etDescripcion = (EditText)findViewById(id.etDescripcion);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        btnSeleccionarUbicacion =(Button)findViewById(id.btnSeleccionarUbicacion);


        storageReference = FirebaseStorage.getInstance().getReference();

        AgregarFormVisibility(true);




        btnSeleccionarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AgregarProducto.this, MapaRegistro.class);
                etUbicacion.setVisibility(View.VISIBLE);
                etPais.setVisibility(View.VISIBLE);
                startActivityForResult(i,CODIGO_BTN_SELECCIONAR);
            }
        });


        btnDonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Animation animation = AnimationUtils.loadAnimation(AgregarProducto.this, anim.bounce);
                btnDonar.startAnimation(animation);

                producto = etProducto.getText().toString();
                cantidad = etCantidad.getText().toString();
                pais = etPais.getText().toString();
                ubicacion = etUbicacion.getText().toString();
                descripcion = etDescripcion.getText().toString();

                if (producto.isEmpty()) {
                    etProducto.setError("Rellene este campo por favor");
                } else

                if (descripcion.isEmpty()) {
                    etDescripcion.setError("Rellene este campo por favor");
                } else{
{
                    if (cantidad.isEmpty()) {
                        etCantidad.setError("Rellene este campo por favor");
                    } else {
                        if(urlFotos.isEmpty()){
                            Toast.makeText(AgregarProducto.this, "Debe agregar una foto del producto por favor", Toast.LENGTH_SHORT).show();
                        }else{
                        if (pais.isEmpty()) {
                            Toast.makeText(AgregarProducto.this, "Seleccione una ubicación en el mapa por favor", Toast.LENGTH_SHORT).show();
                        } else {
                            if (ubicacion.isEmpty()) {
                                Toast.makeText(AgregarProducto.this, "Seleccione una ubicación en el mapa por favor", Toast.LENGTH_SHORT).show();
                            } else {



                                AgregarFormVisibility(false);

                                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                                tokenUsuario = usuario.getUid();



                                Productos product = new Productos(producto, descripcion,cantidad, pais, ubicacion, urlFotos, tokenUsuario,latitude,longitude );

                                firestore.collection("Productos").document(producto+"_"+tokenUsuario).set(product)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressBar.setVisibility(View.GONE);

                                                AlertDialog.Builder alerta = new AlertDialog.Builder(AgregarProducto.this);
                                                alerta.setCancelable(false)
                                                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                etProducto.setText("");
                                                                etDescripcion.setText("");
                                                                etCantidad.setText("");
                                                                etUbicacion.setText("");
                                                                etProducto.requestFocus();
                                                                etPais.setText("");

                                                                dialogInterface.cancel();
                                                            }
                                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent isisi = new Intent(AgregarProducto.this, Principal.class);
                                                        startActivity(isisi);
                                                    }
                                                });
                                                AlertDialog titulo = alerta.create();
                                                titulo.setTitle("¿Desea donar otro producto?");
                                                titulo.show();
                                            }
                                        });


                            }
                        }
                    }
                    }
                }
            }

            }
        });



        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation = AnimationUtils.loadAnimation(AgregarProducto.this, anim.bounce);
                btnFoto.startAnimation(animation);



                final CharSequence[] opciones = {"Tomar foto","Cargar imagen","Cancelar"};

                final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(AgregarProducto.this);
                alertOpciones.setTitle("Seleccione una opción");

                alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (opciones[i].equals("Tomar foto")){
                            tomarFotografia();
                            //TODO ACA VA LA APERTURA DE LA CAMARA
                        }else{
                            if(opciones[i].equals("Cargar imagen")){
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent,GALLERY_INTENT);
                            }else{
                                dialogInterface.dismiss();
                            }
                        }
                    }
                });alertOpciones.show();
            }
        });


    }

    private void tomarFotografia() {
            //TODO ACA VA LA APERTURA DE LA CAMARA SUBIR FOTOS ETC.

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        }



    //Esto sirve para obtener imagen desde la galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(resultCode == RESULT_OK && requestCode == CODIGO_BTN_SELECCIONAR){
            direccion = data.getStringExtra("direccion");
            ciudad = data.getExtras().getString("ciudad");
            estado= data.getExtras().getString("estado");
            country= data.getExtras().getString("pais");
            codigoPostal= data.getExtras().getString("codigoPostal");
            latitude= data.getExtras().getString("latitude");
            longitude = data.getExtras().getString("longitude");
            Log.i("troia",direccion);
            etUbicacion.setText(direccion);
            etPais.setText(country);
        }



        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
        uploadImage(imageBitmap);


        }

            else if(resultCode == RESULT_OK && requestCode == GALLERY_INTENT) {

                final Uri uri = data.getData();

                AlertDialog.Builder siono = new AlertDialog.Builder(AgregarProducto.this);
                siono.setTitle("Volver");
                siono.setMessage("¿Estas seguro de subir esta imagen?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AgregarFormVisibility(false);
                        {

                           final StorageReference filepath = storageReference.child("Fotos").child(uri.getLastPathSegment());
                            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            urlFotos = uri.toString();

                                            AgregarFormVisibility(true);
                                            Toast.makeText(AgregarProducto.this, "Imagen recibida satisfactoriamente", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            });
                        }

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        AgregarFormVisibility(true);

                    }
                });
                siono.show();
                AgregarFormVisibility(false);


            }

    }

    private void AgregarFormVisibility(boolean showForm) {
        progressBar.setVisibility(showForm ? View.GONE : View.VISIBLE);
    }

    private void uploadImage(Bitmap bitmap) {
        AgregarFormVisibility(false);

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());

        final StorageReference ref = storageReference.child("Fotos/" +timeStamp + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();





        final UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                AgregarFormVisibility(true);

                Toast.makeText(AgregarProducto.this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show();

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                       ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               urlFotos = uri.toString();
                           }
                       });
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Log.d("Final URL", "onComplete: Url: " + downUri.toString());
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarProducto.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder cerrar = new AlertDialog.Builder(AgregarProducto.this);
        cerrar.setTitle("Volver");
        cerrar.setMessage("¿Estas seguro de querer volver?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(AgregarProducto.this,Principal.class));
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




