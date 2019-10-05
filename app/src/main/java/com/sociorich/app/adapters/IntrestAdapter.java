package com.sociorich.app.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sociorich.app.R;

import java.util.List;

public class IntrestAdapter extends RecyclerView.Adapter<IntrestAdapter.IntHolder> {
    private List<String> intrstList;
    private Context context;
    private LayoutInflater layoutInflater;

    public IntrestAdapter(List<String> intrstList, Context context){
        this.intrstList = intrstList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public IntHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_intrest,viewGroup);
        return new IntHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntHolder intHolder, int i) {
        intHolder.intrTxt.setText(intrstList.get(i));
    }

    @Override
    public int getItemCount() {
        return intrstList.size();
    }

    class IntHolder extends RecyclerView.ViewHolder {
        public TextView intrTxt;
        public CheckBox checkBox;
        public IntHolder(@NonNull View itemView) {
            super(itemView);
            intrTxt = itemView.findViewById(R.id.intr_txt);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
