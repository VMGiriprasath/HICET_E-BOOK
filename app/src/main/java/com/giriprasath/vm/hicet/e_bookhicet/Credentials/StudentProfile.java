package com.giriprasath.vm.hicet.e_bookhicet.Credentials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.giriprasath.vm.hicet.e_bookhicet.R;
import com.giriprasath.vm.hicet.e_bookhicet.databinding.ActivityStudentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StudentProfile extends AppCompatActivity {
    private ActivityStudentProfileBinding binding;
    String[] year = {"I YEAR", "II YEAR", "III YEAR", "IV YEAR"};
    ArrayAdapter<String> arrayAdapter_year;
    String spinneryearglobal;

    String con, yea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //toolbar
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //load data in spinner department

        arrayAdapter_year = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, year);
        binding.spinneryear.setAdapter(arrayAdapter_year);
        binding.spinneryear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object itemyear = adapterView.getItemAtPosition(i);
                spinneryearglobal = (String) itemyear;


            }
        });
        //load data in profile
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = "" + snapshot.child("Name").getValue();
                yea = "" + snapshot.child("CurrentYear").getValue();
                con = "" + snapshot.child("Department").getValue();
                String email = "" + snapshot.child("E-mail").getValue();


                binding.editEmailName.setText(email);
                binding.editTextName.setText(name.substring(0, 1).toUpperCase() + name.substring(1));
                binding.editDepartment.setText(con);
                binding.editYear.setText(yea);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //on click save changes button

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editTextName.getText().toString();

                HashMap<String, Object> hashMap = new HashMap<>();

                if (spinneryearglobal != null && name != null) {
                    hashMap.put("Name", "" + name);
                    hashMap.put("CurrentYear", "" + spinneryearglobal);
                    hashMap.put("Condition", "" + spinneryearglobal + con);
                }


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.child(firebaseAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(StudentProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentProfile.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}