package com.giriprasath.vm.hicet.adminhicet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.giriprasath.vm.hicet.adminhicet.Adapter.AdapterpdfAdmin;
import com.giriprasath.vm.hicet.adminhicet.Model.Modelpdf;
import com.giriprasath.vm.hicet.adminhicet.databinding.ActivityPdfListAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PdfListAdminActivity extends AppCompatActivity {

    private ArrayList<Modelpdf> pdfArrayList;
    private AdapterpdfAdmin adapterpdfAdmin;
    private ActivityPdfListAdminBinding binding;
    private String categoryId, categoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfListAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryTitle = intent.getStringExtra("categoryTitle");
        binding.toolbarrl.setTitle(categoryTitle);

        //
        binding.toolbarrl.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //method call for pdflist
        loadpdflist();

    }

    private void loadpdflist() {
        pdfArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.orderByChild("categoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Modelpdf model = ds.getValue(Modelpdf.class);
                    pdfArrayList.add(model);


                }
                adapterpdfAdmin = new AdapterpdfAdmin(PdfListAdminActivity.this, pdfArrayList);
                if (pdfArrayList.size() == 0) {
                    binding.notesfound.setVisibility(View.VISIBLE);

                } else {
                    binding.notesfound.setVisibility(View.INVISIBLE);
                    binding.bookrv.setAdapter(adapterpdfAdmin);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}