package com.santi.imagine.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.santi.imagine.R;

public class PrimeraActivity extends AppCompatActivity {
    private ImageView tv;
    TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.primera_activity);
        textView= (TextView)findViewById(R.id.TextView);






        Animation anim = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        textView.startAnimation(anim);

        tv = (ImageView) findViewById(R.id.tv);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        tv.startAnimation(myanim);


        final Intent i = new Intent(this, Login.class);
        Thread timer = new Thread(){
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }



}