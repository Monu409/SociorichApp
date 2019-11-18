package com.sociorich.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sociorich.app.R;
import com.sociorich.app.modals.AwardModel;

import java.util.List;

public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.AwardHolder>{
    private List<AwardModel> awardModels;
    private Context context;
    private LayoutInflater layoutInflater;

    public AwardAdapter(List<AwardModel> awardModels, Context context){
        this.awardModels = awardModels;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public AwardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_award,null);
        return new AwardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AwardHolder holder, int position) {
        holder.titleTxt.setText(awardModels.get(position).getTitle());
        holder.dateTxt.setText(awardModels.get(position).getDate());
        holder.descTxt.setText(awardModels.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return awardModels.size();
    }

    class AwardHolder extends RecyclerView.ViewHolder{
        TextView titleTxt,dateTxt,descTxt;
        public AwardHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.awrd_txt);
            dateTxt = itemView.findViewById(R.id.date_txt);
            descTxt = itemView.findViewById(R.id.awrd_des);
        }
    }
}
