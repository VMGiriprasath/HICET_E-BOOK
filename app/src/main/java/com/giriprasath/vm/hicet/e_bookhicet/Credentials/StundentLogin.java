package com.giriprasath.vm.hicet.e_bookhicet.Credentials;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.giriprasath.vm.hicet.e_bookhicet.Credentials.Adapter.AdapterCategory;
import com.giriprasath.vm.hicet.e_bookhicet.Credentials.Adapter.Adapterpdfuser;
import com.giriprasath.vm.hicet.e_bookhicet.Credentials.Model.Modelpdf;
import com.giriprasath.vm.hicet.e_bookhicet.R;
import com.giriprasath.vm.hicet.e_bookhicet.databinding.ActivityStundentLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StundentLogin extends AppCompatActivity {
    public ActivityStundentLoginBinding binding;
    FirebaseAuth firebaseAuth;
    Button btn;
    Dialog dialog1;
    //Firebase
    //Array list

    private ArrayList<Modelpdf> pdfArrayList;
    private AdapterCategory adapterCategory;

    private Adapterpdfuser adapterpdfuser;
    private String categoryId, categoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStundentLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.progressbar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryTitle = intent.getStringExtra("categoryTitle");

        settextname();
        loaddata();


        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                settextname();
                loaddata();
                binding.swipe.setRefreshing(false);
            }
        });
        binding.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StundentLogin.this);
                String[] option = {"Profile", "Logout", "About", "Report Bugs"};

                builder.setTitle("Settings").setIcon(R.drawable.ic_baseline_settings_24).setCancelable(true).
                        setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if we click profile option
                                if (which == 0) {
                                    Intent intent12 = new Intent(StundentLogin.this, StudentProfile.class);
                                    startActivity(intent12);


                                }//if we click Logout option
                                else if (which == 1) {
                                    firebaseAuth.signOut();
                                    checkuser();

                                }//if we click Developer option
                               /* else if (which == 2) {
                                    dialog1 = new Dialog(StundentLogin.this);
                                    dialog1.setContentView(R.layout.developer_details);
                                    dialog1.setCancelable(false);
                                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    btn = dialog1.findViewById(R.id.ok_btn);
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog1.dismiss();
                                        }
                                    });
                                    dialog1.show();

                                }*/
                                else if (which == 3) {
                                    //Report for bug
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(Uri.parse("mailto:giriprasath.project2002@gmail.com")); // only email apps should handle this
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Report a Bug (HICET E-Book)");
                                    startActivity(intent);

                                }
                            }
                        }).show();


            }


        });


    }

    private void checkuser() {

    }

    private void loaddata() {
        pdfArrayList = new ArrayList<>();
        DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        ref0.keepSynced(true);
        ref0.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String condition1 = "" + snapshot.child("Condition").getValue();


                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Books");
                ref1.orderByChild("Condition").equalTo(condition1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        {
                            binding.progressbar.setVisibility(View.GONE);

                            pdfArrayList.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Modelpdf model = ds.getValue(Modelpdf.class);
                                pdfArrayList.add(model);


                            }
                            adapterpdfuser = new Adapterpdfuser(StundentLogin.this, pdfArrayList);

                            if (pdfArrayList.size() == 0) {
                                binding.nonotes.setVisibility(View.VISIBLE);
                                binding.reporticon.setVisibility(View.VISIBLE);

                            } else {
                                binding.bookrv.setAdapter(adapterpdfuser);
                                binding.nonotes.setVisibility(View.INVISIBLE);
                                binding.reporticon.setVisibility(View.INVISIBLE);


                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.progressbar.setVisibility(View.GONE);
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void settextname() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = "" + snapshot.child("Name").getValue();
                String year = "" + snapshot.child("CurrentYear").getValue();
                String depart = "" + snapshot.child("Department").getValue();
                String email = "" + snapshot.child("E-mail").getValue();


                binding.cardname.setText(name);
                binding.emailtv.setText(email);
                binding.currentyeartv.setText(year);
                binding.currentdepartmenttv.setText(depart);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}