package com.app.sociorichapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.CircleImageView;
import com.app.sociorichapp.modals.NotifModal;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifHolder> {
    private List<NotifModal> notifModals;
    private Context context;
    private LayoutInflater layoutInflater;

    public NotificationAdapter(List<NotifModal> notifModals, Context context){
        this.notifModals = notifModals;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public NotifHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_notification,null);
        return new NotifHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifHolder notifHolder, int i) {
        String imgPath = notifModals.get(i).getImgUrl();
        String nameStr = notifModals.get(i).getNameStr();
        String timeStr = notifModals.get(i).getTimeStr();
        String descStr = notifModals.get(i).getDetailsStr();
        String type = notifModals.get(i).getType();
        String postTitle = notifModals.get(i).getPostTitle();
        String amount = notifModals.get(i).getAmount();
        String desStr = "";
        if(type.equals("DONATION_RECEIVED")){
            desStr = "Donated you "+amount+" Equa Credits on your post "+postTitle;
        }
        else if(type.equals("COMMENT_ON_UR_POST")){
            desStr = "Commented on your post "+postTitle;
        }
        else if(type.equals("INSPIRED_BY_UR_POST")){
            desStr = "has inspired from your post "+postTitle;
        }
        else if(type.equals("VERIFIED_UR_POST")){
            desStr = "has verified your post "+postTitle;
        }
        Glide.with(context)
                .load(imgPath)
                .placeholder(R.drawable.user_profile)
                .error(R.drawable.user_profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(notifHolder.profileImg);
        notifHolder.nameTxt.setText(nameStr+" ("+timeStr+")");
        notifHolder.descTxt.setText(desStr);
    }

    @Override
    public int getItemCount() {
        return notifModals.size();
    }

    public class NotifHolder extends RecyclerView.ViewHolder {
        public CircleImageView profileImg;
        public TextView nameTxt,descTxt;
        public NotifHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profile_img);
            nameTxt = itemView.findViewById(R.id.name_txt);
            descTxt = itemView.findViewById(R.id.notif_txt);
        }
    }
}
