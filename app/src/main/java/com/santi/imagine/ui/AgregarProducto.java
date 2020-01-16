package com.santi.imagine.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santi.imagine.R;
import com.santi.imagine.models.Productos;


public class AgregarProducto extends AppCompatActivity {


    private static final int IMAGE_CAPTURE_CODE = 1001;
    Button btnFoto,btnDonar;
    EditText etProducto,etCantidad,etPais,etCiudad,etDescripcion;
    private StorageReference storageReference;
    private static final int GALLERY_INTENT = 1;
    String producto,cantidad,pais,ciudad,descripcion;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String urlFotos,tokenUsuario;
    static final int REQUEST_TAKE_PHOTO = 1;
    ProgressBar progressBar;
    Uri image_uri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        btnFoto = (Button)findViewById(R.id.btnFoto);
        btnDonar = (Button)findViewById(R.id.btnDonar);
        etProducto = (EditText)findViewById(R.id.etProducto);
        etCantidad = (EditText)findViewById(R.id.etCantidad);
        etPais = (EditText)findViewById(R.id.etPais);
        etCiudad = (EditText)findViewById(R.id.etCiudad);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        etDescripcion = (EditText)findViewById(R.id.etDescripcion);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        AgregarFormVisibility(true);


        btnDonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(AgregarProducto.this, R.anim.bounce);
                btnDonar.startAnimation(animation);

                producto = etProducto.getText().toString();
                cantidad = etCantidad.getText().toString();
                pais = etPais.getText().toString();
                ciudad = etCiudad.getText().toString();
                descripcion = etDescripcion.getText().toString();

                if (descripcion.isEmpty()) {
                    etDescripcion.setError("Rellene este campo por favor");
                } else{
                    if (producto.isEmpty()) {
                    etProducto.setError("Rellene este campo por favor");
                } else {
                    if (cantidad.isEmpty()) {
                        etCantidad.setError("Rellene este campo por favor");
                    } else {
                        if (pais.isEmpty()) {
                            etPais.setError("Rellene este campo por favor");
                        } else {
                            if (ciudad.isEmpty()) {
                                etCiudad.setError("Rellene este campo por favor");
                            } else {


                                AgregarFormVisibility(false);

                                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                                tokenUsuario = usuario.getUid();



                                Productos product = new Productos(producto, descripcion,cantidad, pais, ciudad, urlFotos, tokenUsuario);

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
                                                                etCiudad.setText("");
                                                                etPais.setText("");
                                                                etProducto.requestFocus();

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
                                                titulo.setTitle("Volver");
                                                titulo.show();
                                            }
                                        });


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

                Animation animation = AnimationUtils.loadAnimation(AgregarProducto.this,R.anim.bounce);
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

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"from the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
        }



    //Esto sirve para obtener imagen desde la galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {

            AlertDialog.Builder siono = new AlertDialog.Builder(AgregarProducto.this);
            siono.setTitle("Volver");
            siono.setMessage("¿Estas seguro de subir esta imagen?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AgregarFormVisibility(false);
                    {

                        final StorageReference filepath = storageReference.child("Fotos").child(image_uri.getLastPathSegment());
                        filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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




            if(resultCode == RESULT_OK && requestCode == GALLERY_INTENT) {

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


}




