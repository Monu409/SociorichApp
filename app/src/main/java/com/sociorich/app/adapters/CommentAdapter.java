package com.sociorich.app.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.CircleImageView;
import com.sociorich.app.modals.CommentModal;
import com.sociorich.app.modals.UserCommentModal;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ComntHolder> {
    private List<CommentModal> commentModals;
    private List<String> commentUsers;
    private Context context;
    private LayoutInflater layoutInflater;
    List<UserCommentModal> userCommentModals;
    List<String> testUsers;
    List<String> testComments;

    public CommentAdapter(List<CommentModal> commentModals, Context context){
        this.commentModals = commentModals;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    public CommentAdapter(List<String> testUsers, List<String> testComments, Context context){
        this.testUsers = testUsers;
        this.testComments = testComments;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

//    public CommentAdapter(List<CommentModal> commentModals, Context context){
//        this.commentModals = commentModals;
//        this.context = context;
//        layoutInflater = LayoutInflater.from(context);
//    }


    @NonNull
    @Override
    public ComntHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_comment,viewGroup,false);
        return new ComntHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComntHolder comntHolder, int i) {
        comntHolder.comntTxt.setText(commentModals.get(i).getComntStr());
        comntHolder.userTxt.setText(commentModals.get(i).getUserStr());
        comntHolder.dateTxt.setText(commentModals.get(i).getTimeDateStr());
        Glide.with(context)
                .load(commentModals.get(i).getImgUrl())
                .placeholder(R.drawable.user_profile)
                .error(R.drawable.user_profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(com.bumptech.glide.Priority.HIGH)
                .into(comntHolder.userImg);
    }

    @Override
    public int getItemCount() {
        return commentModals.size();
    }

    class ComntHolder extends RecyclerView.ViewHolder {
        public TextView comntTxt,dateTxt,userTxt;
        public CircleImageView userImg;
        public ComntHolder(@NonNull View itemView) {
            super(itemView);
            comntTxt = itemView.findViewById(R.id.comment_txt);
            dateTxt = itemView.findViewById(R.id.date_txt);
            userTxt = itemView.findViewById(R.id.user_txt);
            userImg = itemView.findViewById(R.id.user_img);
        }
    }
    public void addMoreData(List<CommentModal> commentModals){
        commentModals.addAll(commentModals);
        notifyDataSetChanged();
    }
}
