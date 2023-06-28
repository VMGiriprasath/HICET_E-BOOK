package com.giriprasath.vm.hicet.e_bookhicet.Credentials.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.giriprasath.vm.hicet.e_bookhicet.Credentials.Model.Modelpdf;
import com.giriprasath.vm.hicet.e_bookhicet.Credentials.Application.MyApplication;
import com.giriprasath.vm.hicet.e_bookhicet.Credentials.PdfDetailsActivity;
import com.giriprasath.vm.hicet.e_bookhicet.databinding.RowPdfAdminBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterpdfAdmin extends RecyclerView.Adapter<AdapterpdfAdmin.HolderpdfAdmin> {
    private Context context;
    private ArrayList<Modelpdf> pdfArrayList;
    private RowPdfAdminBinding binding;
    private ProgressDialog dialog;


    public AdapterpdfAdmin(Context context, ArrayList<Modelpdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;

        //init progress dialogue
        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait!");
        dialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public HolderpdfAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderpdfAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderpdfAdmin holder, int position) {
        Modelpdf model = pdfArrayList.get(position);
        String title = model.getTitle();
        String description = model.getDescription();
        long timestamp = model.getTimestamp();
        String pdfId = model.getId();
        String date = getDate(timestamp);
        String fac = model.getFacultyname();
        String categoryId = model.getCategoryId();
        holder.titletv.setText(title);
        holder.desctv.setText(description);
        holder.datetv.setText(date);
        holder.sizetv.setText(fac);

        //Fetching code for MyApplication class

        MyApplication.loadcategory("" + categoryId, holder.categoryTv);
        //handle edit and delete btn
        holder.morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreoptiondialogue(model, holder);
            }


            private void moreoptiondialogue(Modelpdf model, HolderpdfAdmin holder) {
                String bookId = model.getId();
                String bookUrl = model.getUrl();
                String bookTitle = model.getTitle();
                String[] option = {"Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose Option")
                        .setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    MyApplication.deletemodel(context,
                                            "" + bookId,
                                            "" + bookUrl,
                                            "" + bookTitle);
                                    // deletemodel(model, holder);

                                }
                            }

                        }).show();
            }
        });
        //handle click,open pdf detail page
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, PdfDetailsActivity.class);
                intent1.putExtra("bookId", pdfId);
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

    class HolderpdfAdmin extends RecyclerView.ViewHolder {
        TextView titletv, desctv, categoryTv, sizetv, datetv;
        ImageButton morebtn;

        public HolderpdfAdmin(@NonNull View itemView) {
            super(itemView);
            titletv = binding.titletv;
            desctv = binding.desctv;
            categoryTv = binding.categoryTv;
            sizetv = binding.sizetv;
            datetv = binding.datetv;
            morebtn = binding.morebtn;

        }
    }
}
