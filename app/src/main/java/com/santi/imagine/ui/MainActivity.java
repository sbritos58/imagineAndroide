package com.santi.imagine.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.santi.imagine.R;
import com.santi.imagine.models.AdapterProductos;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore mFirestore;
    RecyclerView recyclerView;
    MyAdapter mAdapter ;
    LinearLayout linearLayout;
    ProgressBar progressBar2;
    private int hola = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        progressBar2 = (ProgressBar)findViewById(R.id.progressBar2) ;

        AgregarFormVisibility(false);

        mFirestore = FirebaseFirestore.getInstance();

        Query query = mFirestore.collection("Productos");

        FirestoreRecyclerOptions<AdapterProductos> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<AdapterProductos>()
                .setQuery(query,AdapterProductos.class).build();

        mAdapter = new MyAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        AgregarFormVisibility(true);

        Thread timer = new Thread(){
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(recyclerView.getAdapter()!=null){
                        if(recyclerView.getAdapter().getItemCount()== 0 ){
                            hola = recyclerView.getAdapter().getItemCount();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    //Toast.makeText(this, cadena, Toast.LENGTH_LONG).show();// Aqui me sale el error
                                    Toast.makeText(getApplicationContext(),"En este momento no hay donaciones disponibles", Toast.LENGTH_LONG).show();
                                }
                            });
                            Intent i = new Intent(MainActivity.this,Principal.class);

                            startActivity(i);

                        }
                    }
                }
            }
        };
        timer.start();



    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder cerrar = new AlertDialog.Builder(MainActivity.this);
        cerrar.setTitle("Volver");

        cerrar.setMessage("¿Estas seguro de querer volver?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(MainActivity.this, Principal.class));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();

            }
        });
        cerrar.show();
    }

    private void AgregarFormVisibility(boolean showForm) {
        progressBar2.setVisibility(showForm ? View.GONE : View.VISIBLE);
        linearLayout.setVisibility(showForm ? View.VISIBLE : View.GONE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();

    }


    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}