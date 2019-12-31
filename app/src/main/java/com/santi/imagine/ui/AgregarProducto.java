package com.santi.imagine.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


    Button btnFoto,btnDonar;
    EditText etProducto,etCantidad,etPais,etCiudad;
    private StorageReference storageReference;
    private static final int GALLERY_INTENT = 1;
    String producto,cantidad,pais,ciudad;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String urlFotos,tokenUsuario;


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

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        btnDonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String producto = etProducto.getText().toString();
                cantidad = etCantidad.getText().toString();
                pais = etPais.getText().toString();
                ciudad = etCiudad.getText().toString();



                if (producto.isEmpty()){
                    etProducto.setError("Rellene este campo por favor");
                }else{
                    if (cantidad.isEmpty()){
                        etCantidad.setError("Rellene este campo por favor");
                    }else{
                        if (pais.isEmpty()){
                            etPais.setError("Rellene este campo por favor");
                        }else{
                            if (ciudad.isEmpty()){
                                etCiudad.setError("Rellene este campo por favor");
                            }else{

                                FirebaseUser usuario =firebaseAuth.getCurrentUser();
                                tokenUsuario = usuario.getUid();
                                Productos product = new Productos(producto,cantidad,pais,ciudad,urlFotos,tokenUsuario);

                                firestore.collection("Productos").document().set(product)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                AlertDialog.Builder alerta = new AlertDialog.Builder(AgregarProducto.this);
                                                alerta.setMessage("¿Desea volver a donar otro producto?").setCancelable(false)
                                                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.cancel();
                                                            }
                                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent isisi = new Intent(AgregarProducto.this,Principal.class);
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
        });



        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);

            }
        });


    }



    //Esto sirve para obtener imagen desde la galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){


            Uri uri = data.getData();


            final StorageReference filepath = storageReference.child("Fotos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Uri downloadUrl = uri;
                            urlFotos = uri.toString();

                            Toast.makeText(AgregarProducto.this, "Imagen recibida satisfactoriamente", Toast.LENGTH_SHORT).show();
                        }

                    });

                }
            });

        }
    }
}
