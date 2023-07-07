package com.giriprasath.vm.hicet.e_bookhicet.Credentials;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.giriprasath.vm.hicet.e_bookhicet.Credentials.Application.MyApplication;
import com.giriprasath.vm.hicet.e_bookhicet.databinding.ActivityPdfDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class PdfDetailsActivity extends AppCompatActivity {

    String bookid, bookTitle, bookUrl;
    private ActivityPdfDetailsBinding binding;
    //request permision
    private final ActivityResultLauncher<String> requestpermissionauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {
            MyApplication.downloadbook(this, "" + bookid, "" + bookTitle, "" + bookUrl);


        } else {
            Toast.makeText(this, "Permission was Denied!...", Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseDatabase.getInstance().getReference("Books");

        //toolbar
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //get date from book id
        Intent intent = getIntent();
        bookid = intent.getStringExtra("bookId");

        binding.downloadbookbtn.setVisibility(View.GONE);

        loadBookdetails();
//handle download book
        binding.downloadbookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PdfDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    MyApplication.downloadbook(PdfDetailsActivity.this, "" + bookid, "" + bookTitle, "" + bookUrl);

                } else {
                    requestpermissionauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });
        binding.viewbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PdfDetailsActivity.this, PdfViewActivity.class);
                intent1.putExtra("bookId", bookid);
                startActivity(intent1);

            }
        });
    }

    private void loadBookdetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get data
                bookTitle = "" + snapshot.child("title").getValue();
                String description = "" + snapshot.child("description").getValue();
                String categoryid = "" + snapshot.child("categoryId").getValue();
                String Facultyname = "" + snapshot.child("facultyname").getValue();
                String Currentyear = "" + snapshot.child("CurrentYear").getValue();
                String currentdept = "" + snapshot.child("Department").getValue();
                bookUrl = "" + snapshot.child("Url").getValue();
                String timestamp = "" + snapshot.child("timestamp").getValue();
                String date = getDate(timestamp);
                String time = getTime(timestamp);
                MyApplication.loadcategory("" + categoryid, binding.categoryTv);
                binding.toolbar.setTitle("Book");

                //set data
                binding.titleTv.setText(bookTitle.substring(0, 1).toUpperCase() + bookTitle.substring(1));
                binding.downloadbookbtn.setVisibility(View.VISIBLE);
                binding.descriptiontv.setText(description.substring(0, 1).toUpperCase() + description.substring(1));
                binding.DateTv.setText(date);
                binding.FacultynameTv.setText(Facultyname.substring(0, 1).toUpperCase() + Facultyname.substring(1));
                binding.Timetv.setText(time);
                binding.currentyeartv.setText(Currentyear);
                binding.currentdepartmenttv.setText(currentdept);
                binding.downloadbookbtn.setVisibility(View.VISIBLE);


            }

            private String getTime(String timestamp) {
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(Long.parseLong(timestamp));
                String times = DateFormat.format("hh:mm:ss", cal).toString();
                return times;
            }

            private String getDate(String timestamp) {
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(Long.parseLong(timestamp));
                String date = DateFormat.format("dd-MM-yyyy", cal).toString();
                return date;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}