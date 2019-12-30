package com.santi.imagine.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.santi.imagine.R;

public class MainActivity extends AppCompatActivity {
    private ImageView tv;
    TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    sleep(2000);
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