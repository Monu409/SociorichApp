package com.app.sociorichapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;

import java.util.List;

public class QualityAdapter extends RecyclerView.Adapter<QualityAdapter.QtyHolder> {
    private List<String> qltyList;
    private Context context;
    private LayoutInflater layoutInflater;

    public QualityAdapter(List<String> qltyList, Context context){
        this.qltyList = qltyList;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public QtyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_quality,null);
        return new QtyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QtyHolder qtyHolder, int i) {
        qtyHolder.qltyTxt.setText(qltyList.get(i));
        qtyHolder.deleteImg.setOnClickListener(d->{
            List<String> qltyList = ConstantMethods.getArrayListShared(context,"quality_key");
            qltyList.remove(i);
            notifyItemRemoved(i);

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
}
