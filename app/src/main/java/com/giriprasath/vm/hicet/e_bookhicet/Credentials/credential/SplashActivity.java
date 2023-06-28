package com.giriprasath.vm.hicet.e_bookhicet.Credentials.credential;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.giriprasath.vm.hicet.e_bookhicet.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    public static final int MILLI = 2000;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Handler for handling threads
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginintent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(loginintent);
                finish();
            }
        }, MILLI);


    }
}