package com.app.sociorichapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;

import java.util.List;

public class DeleteIntrestAdapter extends RecyclerView.Adapter<DeleteIntrestAdapter.QtyHolder> {
    private List<String> qltyList;
    private Context context;
    private LayoutInflater layoutInflater;

    public DeleteIntrestAdapter(List<String> qltyList, Context context){
        this.qltyList = qltyList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public QtyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_dlt_intrst,null);
        return new QtyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QtyHolder qtyHolder, int i) {
        qtyHolder.qltyTxt.setText(qltyList.get(i));
//        List<String> qltyList = ConstantMethods.getArrayListShared(context,"quality_key");
        qtyHolder.deleteImg.setOnClickListener(d->{
            alertDialogForDelete(i);
        });
    }

    @Override
    public int getItemCount() {
        return qltyList.size();
    }

    public class QtyHolder extends RecyclerView.ViewHolder {
        public TextView qltyTxt;
        public ImageView deleteImg;
        public QtyHolder(@NonNull View itemView) {
            super(itemView);
            qltyTxt = itemView.findViewById(R.id.qlty_txt);
            deleteImg = itemView.findViewById(R.id.delete_img);
        }
    }
    private void alertDialogForDelete(int i){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete Interest");
        builder1.setMessage("Do you want to delete this interest?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        qltyList.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, qltyList.size());
                        ConstantMethods.saveArrayListShared(qltyList, context,"interest_save");
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
