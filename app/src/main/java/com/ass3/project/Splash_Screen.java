package com.ass3.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Splash_Screen extends AppCompatActivity {

    androidx.appcompat.widget.AppCompatButton start;
    TextView txtCook,txtApp;
    RelativeLayout relativeLayout;
    Animation txtAnim,layoutAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        txtAnim = AnimationUtils.loadAnimation(Splash_Screen.this,R.anim.fall_down);
        layoutAnim = AnimationUtils.loadAnimation(Splash_Screen.this,R.anim.bottom_top);

        start = findViewById(R.id.getStart);
        txtCook = findViewById(R.id.text1);
        txtApp = findViewById(R.id.text2);
        relativeLayout = findViewById(R.id.relMain);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout.setAnimation(layoutAnim);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtCook.setVisibility(View.VISIBLE);
                        txtApp.setVisibility(View.VISIBLE);

                        txtCook.setAnimation(txtAnim);
                        txtApp.setAnimation(txtAnim);

                    }
                }, 900);
            }
        }, 500);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Splash_Screen.this,Sign_In.class);
                startActivity(intent);
            }
        });
    }
}