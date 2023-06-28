package com.giriprasath.vm.hicet.adminhicet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.giriprasath.vm.hicet.adminhicet.databinding.ActivityPdfAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class PdfAddActivity extends AppCompatActivity {
    private ActivityPdfAddBinding binding;

    //progress dialogue
    private ProgressDialog progressDialog;
    private static final int PDF_PICK_CODE = 1000;
    //array list for pdf category
    private ArrayList<String> categoryTitleArrayList, categoryidArrayList;
    //uri of picked pdf
    private Uri pdfuri = null;
    private FirebaseAuth firebaseAuth;
    String spinnerdepartmentglobal, spinneryearglobal;
    String[] depatments = {"IT", "CSE", "AI & ML", "ECE", "EEE", "MCT", "CIVIL", "MECH", "AERO", "BME","AUTOMOBILE","FOOD TECH","CHEMICAL"};
    String[] year = {"I YEAR", "II YEAR", "III YEAR", "IV YEAR"};
    ArrayAdapter<String> arrayAdapter_year;
    ArrayAdapter<String> arrayAdapter_department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Toolbar
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        loadpdfcategories();
        //setup progress dialogue
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
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

        //handle attach btn
        binding.attachpdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfpickintent();
            }


        });
        //handle pick category
        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryPickDialogue();
            }
        });
        //handle sumit button
        binding.sumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatedata();
            }
        });

    }

    private String title = "", description = "", facultyname = "";

    private void validatedata() {
        //step 1 : validate data
        Log.d(TAG, "Validating data: Valadating data...");
        //get data
        title = binding.titleet.getText().toString().trim();
        description = binding.descriptionet.getText().toString().trim();
        facultyname = binding.facultyname.getText().toString().trim();


        //validate data
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getApplicationContext(), "Enter title...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(facultyname)) {
            Toast.makeText(this, "Enter Faculty Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(getApplicationContext(), "Enter Description...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(selecetedCategoryTitle)) {
            Toast.makeText(getApplicationContext(), "Pick Category...", Toast.LENGTH_SHORT).show();
        } else if (pdfuri == null) {
            Toast.makeText(getApplicationContext(), "Pick pdf...", Toast.LENGTH_SHORT).show();
        } else {
            uplodepdftosttorage();
        }

    }

    private void uplodepdftosttorage() {
        //step 2:Uplode pdf to firebase
        Log.d(TAG, "Uploadingpdftostorage: Uploading to storage...");
        //show progress
        progressDialog.setMessage("Uploading pdf...");
        progressDialog.show();
        //timestamp
        long timestamp = System.currentTimeMillis();
        //

        String filepathandName = "Books/" + timestamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filepathandName);
        storageReference.putFile(pdfuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Onsuccess:PDF Uploaded to storage");
                        Log.d(TAG, "Onsuccess:getting pdf url");

                        //get pdf url
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        String uploadedpdfurl = "" + uriTask.getResult();
                        //upload to firebase db
                        UploadInfoindb(uploadedpdfurl, timestamp);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "ONFailure:PDF upload failed due to" + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Pdf Upload failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void UploadInfoindb(String uploadedpdfurl, long timestamp) {
        //step 3: Upload pdf into firebase db
        Log.d(TAG, "Uploadpdfto stroage:Uploading pdf into db..");
        progressDialog.setMessage("Uploading pdf info...");
        String uid = firebaseAuth.getUid();
        String appen = spinneryearglobal + spinnerdepartmentglobal;



        //setup data to upload
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("title", "" + title);
        hashMap.put("description", "" + description);
        hashMap.put("categoryId", "" + selectedCategoryId);
        hashMap.put("Url", "" + uploadedpdfurl);
        hashMap.put("timestamp", timestamp);
        hashMap.put("facultyname", facultyname);
        hashMap.put("CurrentYear", spinneryearglobal);
        hashMap.put("Department", spinnerdepartmentglobal);
        hashMap.put("Condition", appen);
        //db ref::DB>BOOKS

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child("" + timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failed to uplaod in db" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void loadpdfcategories() {
        Log.d(TAG, "Loadcategories:Loading pdf Categories...");
        categoryTitleArrayList = new ArrayList<>();
        categoryidArrayList = new ArrayList<>();
        //db reference to load category
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Category");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitleArrayList.clear();
                categoryidArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {//get id and title of category
                    String categoryid = "" + ds.child("id").getValue();
                    String categorytitle = "" + ds.child("category").getValue();

                    //add to respective array list
                    categoryTitleArrayList.add(categorytitle);
                    categoryidArrayList.add(categoryid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private String selectedCategoryId, selecetedCategoryTitle;

    private void categoryPickDialogue() {
        Log.d(TAG, "categorypick dialogue:show category pick dialgue");
        //get string array of categories  for arraylist
        String[] categoryArray = new String[categoryTitleArrayList.size()];
        for (int i = 0; i < categoryTitleArrayList.size(); i++) {
            categoryArray[i] = categoryTitleArrayList.get(i);
        }
        //alert dailogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(categoryArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        selecetedCategoryTitle = categoryTitleArrayList.get(i);
                        selectedCategoryId = categoryidArrayList.get(i);

                        binding.categoryTv.setText(selecetedCategoryTitle);
                        Log.d(TAG, "On click:Selected CategoryiD" + "" + selectedCategoryId + "" + selecetedCategoryTitle);
                    }
                }).show();

    }

    private void pdfpickintent() {
        Log.d(TAG, "pdfPickIntent: starting pdf pick intent");
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select pdf"), PDF_PICK_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PDF_PICK_CODE) {
                pdfuri = data.getData();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Canelled Picking pdf", Toast.LENGTH_SHORT).show();

        }
    }
}