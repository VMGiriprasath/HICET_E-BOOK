package com.giriprasath.vm.hicet.e_bookhicet.Credentials.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.giriprasath.vm.hicet.e_bookhicet.Credentials.Model.Modelpdf;
import com.giriprasath.vm.hicet.e_bookhicet.Credentials.Application.MyApplication;
import com.giriprasath.vm.hicet.e_bookhicet.Credentials.PdfDetailsActivity;
import com.giriprasath.vm.hicet.e_bookhicet.databinding.RowPdfUserBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Adapterpdfuser extends RecyclerView.Adapter<Adapterpdfuser.HolderPdfUser>{
    private Context context;
    public ArrayList<Modelpdf>pdfArrayList;
    private RowPdfUserBinding binding;
    private ProgressDialog dialog;


    public Adapterpdfuser(Context context, ArrayList<Modelpdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        //init progress dialogue
        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait!");
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public HolderPdfUser onCreateViewHolder( ViewGroup parent, int viewType) {
        binding=RowPdfUserBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderPdfUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder( HolderPdfUser holder, int position) {
        Modelpdf model=pdfArrayList.get(position);
        String title= model.getTitle();
        String description= model.getDescription();
        long timestamp = model.getTimestamp();
        String pdfId= model.getId();
        String date = getDate(timestamp);
        String fac = model.getFacultyname();
        String categoryId = model.getCategoryId();
        Intent intent = ((Activity) context).getIntent();

        //set datas
        holder.titletv.setText(title);
        holder.desctv.setText(description);
        holder.dateTv.setText(date);
        holder.sizeTv.setText(fac);


        MyApplication.loadcategory("" + categoryId, holder.categoryTv);

        //handle click,open pdf detail page
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(context, PdfDetailsActivity.class);
                intent1.putExtra("bookId",pdfId );
                context.startActivity(intent1);

            }
        });



    }
    private String getDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }


    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    class HolderPdfUser extends RecyclerView.ViewHolder{
   TextView titletv,desctv,categoryTv,sizeTv,dateTv;
        public HolderPdfUser(@NonNull View itemView) {
            super(itemView);
            titletv= binding.titletv;
            desctv= binding.desctv;
            categoryTv= binding.categoryTv;
            sizeTv= binding.sizetv;
            dateTv= binding.datetv;

        }
    }
}
