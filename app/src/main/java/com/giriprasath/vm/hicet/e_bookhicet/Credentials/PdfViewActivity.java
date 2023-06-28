package com.giriprasath.vm.hicet.e_bookhicet.Credentials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.giriprasath.vm.hicet.e_bookhicet.Credentials.Application.Constants;
import com.giriprasath.vm.hicet.e_bookhicet.databinding.ActivityPdfViewBinding;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PdfViewActivity extends AppCompatActivity {
   private ActivityPdfViewBinding binding;
   private String bookId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get Intent
        Intent intent=getIntent();
        bookId=intent.getStringExtra("bookId");

        //toolbar
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadbooksDetails();
    }

    private void loadbooksDetails() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pdfurl=""+snapshot.child("Url").getValue();
                String title=""+snapshot.child("title").getValue();

                binding.toolbar.setTitle(title);

                loadbookfromurl(pdfurl);

            }

            private void loadbookfromurl(String pdfurl) {
                StorageReference storeref= FirebaseStorage.getInstance().getReferenceFromUrl(pdfurl);
                storeref.getBytes(Constants.MAX_BYTES_PDF).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        binding.progressbar.setVisibility(View.GONE);
                        binding.pdfview.fromBytes(bytes).
                                swipeHorizontal(false).onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {

                                    }
                                }).onError(new OnErrorListener() {
                                    @Override
                                    public void onError(Throwable t) {
                                        Toast.makeText(PdfViewActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }).onPageError(new OnPageErrorListener() {
                                    @Override
                                    public void onPageError(int page, Throwable t) {
                                        Toast.makeText(PdfViewActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }).load();
                        binding.progressbar.setVisibility(View.GONE);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                      binding.progressbar.setVisibility(View.GONE);
                        Toast.makeText(PdfViewActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}