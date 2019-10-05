package com.sociorich.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sociorich.app.R;
import com.sociorich.app.activities.ShowProfileActivity;
import com.sociorich.app.modals.NetworkModal;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class MyNetworkAdapter extends RecyclerView.Adapter<MyNetworkAdapter.MNetHolder> {
    private Context context;
    private List<NetworkModal> networkModals;
    private LayoutInflater layoutInflater;

    public MyNetworkAdapter(Context context, List<NetworkModal> networkModals){
        this.context = context;
        this.networkModals = networkModals;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public MNetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_my_network,null);
        return new MNetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MNetHolder holder, int position) {
        Glide.with(context)
                .load(networkModals.get(position).getProfileStr())
                .placeholder(R.drawable.user_profile)
                .error(R.drawable.user_profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(holder.profileImg);
        holder.socioTxt.setText(networkModals.get(position).getSocioStr());
        holder.proNameTxt.setText(networkModals.get(position).getNameStr());
        holder.profileImg.setOnClickListener(v->{
            String identity = networkModals.get(position).getIdentity();
            Intent intent = new Intent(context, ShowProfileActivity.class);
            intent.putExtra("user_identity", identity);
            context.startActivity(intent);
        });
        holder.proNameTxt.setOnClickListener(v->{
            String identity = networkModals.get(position).getIdentity();
            Intent intent = new Intent(context, ShowProfileActivity.class);
            intent.putExtra("user_identity", identity);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return networkModals.size();
    }

    class MNetHolder extends RecyclerView.ViewHolder {
        ImageView profileImg;
        TextView proNameTxt,socioTxt;
        public MNetHolder(View view) {
            super(view);
            profileImg = view.findViewById(R.id.fr_img);
            proNameTxt = view.findViewById(R.id.fr_name_txt);
            socioTxt = view.findViewById(R.id.soco_credt);
        }
    }
}
