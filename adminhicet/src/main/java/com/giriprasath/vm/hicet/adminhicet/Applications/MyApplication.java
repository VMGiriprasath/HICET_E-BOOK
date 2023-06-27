package com.giriprasath.vm.hicet.adminhicet.Applications;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.FileOutputStream;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static void deletemodel(Context context,String bookId ,String bookUrl ,String bookTitle) {

        ProgressDialog dialog=new ProgressDialog(context);
        dialog.setTitle("Please Wait!...");
        dialog.setMessage("Deleting " + bookTitle + " " + "...");
        dialog.show();

        //Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Books");
                databaseReference.child(bookId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(context, "Notes Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void loadcategory(String categoryId, TextView categoryTv) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Category");
        ref.child(categoryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String category = "" + snapshot.child("category").getValue();
                        categoryTv.setText(category);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public static void downloadbook(Context context,String bookId,String bookTitle,String bookUrl){

        String namewithExtension=bookTitle+".pdf";

         //progress dialogu
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Downloading"+" "+namewithExtension);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //download from firebase url
        StorageReference reference= FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
        reference.getBytes(Constants.MAX_BYTES_PDF).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
             savedownloadbook(context,progressDialog,bytes,namewithExtension,bookId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void savedownloadbook(Context context, ProgressDialog progressDialog, byte[] bytes, String namewithExtension, String bookId) {
   try {

           File downloaderEnvironment= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
           downloaderEnvironment.mkdirs();
           String filepath=downloaderEnvironment.getPath()+"/"+namewithExtension;
           FileOutputStream out=new FileOutputStream(filepath);

               out.write(bytes);
               out.close();
               Toast.makeText(context, "Saved to Downloaded Folder", Toast.LENGTH_SHORT).show();
               progressDialog.dismiss();





   }catch (Exception e){
       Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
   }
    }
}
