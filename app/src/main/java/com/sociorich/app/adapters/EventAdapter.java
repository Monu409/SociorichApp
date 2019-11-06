package com.sociorich.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sociorich.app.R;
import com.sociorich.app.modals.EventModel;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    private List<EventModel> eventModels;
    private Context context;
    private LayoutInflater layoutInflater;

    public EventAdapter(List<EventModel> eventModels, Context context){
        this.eventModels = eventModels;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_event,null);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        holder.titlTxt.setText(eventModels.get(position).getTitle());
        holder.desTxt.setText(eventModels.get(position).getDesc());
        holder.dateTxt.setText(eventModels.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return eventModels.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public ImageView evImg;
        public TextView titlTxt,desTxt,dateTxt;
        public EventHolder(@NonNull View itemView) {
            super(itemView);
            evImg = itemView.findViewById(R.id.img_event);
            titlTxt = itemView.findViewById(R.id.title_txt);
            desTxt = itemView.findViewById(R.id.des_txt);
            dateTxt = itemView.findViewById(R.id.date_txt);
        }
    }
}
