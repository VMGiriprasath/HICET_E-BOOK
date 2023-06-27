package com.giriprasath.vm.hicet.adminhicet.Credentials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.giriprasath.vm.hicet.adminhicet.AdminHomeActivity;
import com.giriprasath.vm.hicet.adminhicet.R;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    private EditText loginid, password;
    private MaterialButton login_btn;
    private final static String id_val = "admin@hicet.ac.in";
    private final static String id_pass = "admin@123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginid = findViewById(R.id.mailid);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.btn_login);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = loginid.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (mail.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                } else if (pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter a valid password", Toast.LENGTH_SHORT).show();
                } else {
                    if (mail.equals(id_val) && pass.equals(id_pass)) {
                        Intent homeintent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        startActivity(homeintent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Email or Password do not match.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }
}