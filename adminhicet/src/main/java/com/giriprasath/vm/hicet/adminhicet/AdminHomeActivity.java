package com.giriprasath.vm.hicet.adminhicet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.giriprasath.vm.hicet.adminhicet.Adapter.AdapterCategory;
import com.giriprasath.vm.hicet.adminhicet.Model.ModelCategory;
import com.giriprasath.vm.hicet.adminhicet.databinding.ActivityAdminHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity {

    private ActivityAdminHomeBinding binding;
    FirebaseAuth firebaseAuth;

    //Array list to store Subject Category
    private ArrayList<ModelCategory> categoryArrayList;
    private AdapterCategory adapterCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //method call...
        loadCategory();
        //add pdf fab click
        binding.addpdffab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, PdfAddActivity.class));

            }
        });
        binding.addcategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, CategoryAddActivity.class);
                startActivity(intent);
            }
        });

        //when search button is clicked
        binding.searchet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterCategory.getFilter().filter(s);

                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void loadCategory() {
        categoryArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Category");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelCategory model = ds.getValue(ModelCategory.class);
                    categoryArrayList.add(model);
                }
                adapterCategory = new AdapterCategory(AdminHomeActivity.this, categoryArrayList);
                binding.CategoriesRv.setAdapter(adapterCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}