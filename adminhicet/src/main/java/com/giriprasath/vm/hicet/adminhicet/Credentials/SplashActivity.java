package com.giriprasath.vm.hicet.adminhicet.Credentials;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.giriprasath.vm.hicet.adminhicet.R;

public class SplashActivity extends AppCompatActivity {
    public static final int MILLI =3000;

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