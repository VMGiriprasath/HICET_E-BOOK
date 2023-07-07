package com.giriprasath.vm.hicet.e_bookhicet.Credentials;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.giriprasath.vm.hicet.e_bookhicet.R;
import com.giriprasath.vm.hicet.e_bookhicet.databinding.ActivityStudentProfileBinding;

public class StudentProfile extends AppCompatActivity {
    private ActivityStudentProfileBinding binding;

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

        //

    }
}