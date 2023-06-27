package com.giriprasath.vm.hicet.adminhicet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.giriprasath.vm.hicet.adminhicet.databinding.ActivityCategoryAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {
    public ActivityCategoryAddBinding binding;
    //Firebase Auth
    FirebaseAuth firebaseAuth;

    //Progress Dialogue
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Toolbar
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Init Firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        //Configure Progress Dialogue
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        //handling submit button
        binding.sumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatedata();

            }

            private String category = "";

            private void validatedata() {
                category = binding.catergoryTitle.getText().toString();
                if (TextUtils.isEmpty(category)) {
                    Toast.makeText(CategoryAddActivity.this, "Please enter Subject name...", Toast.LENGTH_SHORT).show();
                } else {
                    addCategoryFirebase();
                }
            }

            private void addCategoryFirebase() {
                progressDialog.setMessage("Adding Subject...");
                progressDialog.show();
                long timestamp = System.currentTimeMillis();
                //Hashmap
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", "" + timestamp);
                hashMap.put("category", "" + category);
                hashMap.put("timestamp", timestamp);
                hashMap.put("uid", "" + firebaseAuth.getUid());
                //Firebase Database
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Category");
                ref.child("" + timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "Subject Added Successfully....", Toast.LENGTH_SHORT).show();
                        binding.catergoryTitle.setText("");
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.catergoryTitle.setText("");
                    }
                });
            }
        });
    }
}