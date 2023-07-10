package com.giriprasath.vm.hicet.e_bookhicet.Credentials.credential;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.giriprasath.vm.hicet.e_bookhicet.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    public ActivityRegisterBinding binding;
    //Firebase Authentication
    private FirebaseAuth firebaseAuth;
    //Progress Dialogue
    private ProgressDialog progressDialog;
    String[] depatments = {"IT", "CSE", "AI & ML", "ECE", "EIE", "EEE", "MCT", "CIVIL", "MECH", "AERO", "BME", "AUTOMOBILE", "FOOD TECH", "CHEMICAL"};
    String[] year = {"I YEAR", "II YEAR", "III YEAR", "IV YEAR"};
    ArrayAdapter<String> arrayAdapter_year;
    ArrayAdapter<String> arrayAdapter_department;
    String spinnerdepartmentglobal, spinneryearglobal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();

        //Setup progress Dialogue
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(false);

        //Link Setup (From register page to Login page)
        binding.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //spinner department
        arrayAdapter_department = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, depatments);
        binding.spinnerdepartment.setAdapter(arrayAdapter_department);
        binding.spinnerdepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                spinnerdepartmentglobal = (String) item;

            }
        });

        //Spinner year
        arrayAdapter_year = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, year);
        binding.spinneryear.setAdapter(arrayAdapter_year);
        binding.spinneryear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemyear = parent.getItemAtPosition(position);
                spinneryearglobal = (String) itemyear;
            }
        });
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            public String name = "", email = "", password = "";

            @Override
            public void onClick(View view) {
                validatedata();

            }

            private void validatedata() {
                //getting data through Edittext
                name = binding.regname.getText().toString();
                //spinner details wants to get
                String spinnerdepart = spinnerdepartmentglobal;
                String Spinneryear = spinneryearglobal;
                email = binding.regEmail.getText().toString();
                password = binding.regPass.getText().toString();
                String Cpassword = binding.regCpass.getText().toString();

                //Validating data using loop
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterActivity.this, "Enter you name", Toast.LENGTH_SHORT).show();
                } else if (!email.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "Enter valid College E-mail", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Enter Password....!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Cpassword)) {
                    Toast.makeText(RegisterActivity.this, "Confirm Password....!", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(Cpassword)) {
                    Toast.makeText(RegisterActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();

                } else {
                    //method call
                    createUserAccount(spinnerdepart, Spinneryear);
                }
            }

            private void createUserAccount(String a, String b) {
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();
                //Create user in Firebase Auth
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //method call to feed data in realtime database
                        update();
                    }

                    private void update() {
                        progressDialog.setMessage("Saving Info...");
                        long timestamp = System.currentTimeMillis();
                        String uid = firebaseAuth.getUid();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("UID", uid);
                        hashMap.put("E-mail", email);
                        hashMap.put("Name", name);
                        hashMap.put("Department", a);
                        hashMap.put("CurrentYear", b);
                        hashMap.put("Timestamp", timestamp);
                        hashMap.put("Condition", b + a);
                        FirebaseDatabase firebaseDatabase;
                        DatabaseReference ref;
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        // below line is used to get reference for our database.
                        ref = firebaseDatabase.getReference("Users");

                        ref.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Account Created Successfully...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //account Created Failed
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });

    }
}