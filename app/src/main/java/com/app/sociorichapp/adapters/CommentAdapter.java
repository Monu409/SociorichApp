package com.app.sociorichapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.modals.CommentModal;
import com.app.sociorichapp.modals.UserCommentModal;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ComntHolder> {
    private List<CommentModal> commentModals;
    private List<String> commentUsers;
    private Context context;
    private LayoutInflater layoutInflater;
    List<UserCommentModal> userCommentModals;
    List<String> testUsers;
    List<String> testComments;

    public CommentAdapter(List<CommentModal> commentModals, Context context,String s){
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
    @NonNull
    @Override
    public ComntHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_comment,viewGroup,false);
        return new ComntHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComntHolder comntHolder, int i) {
        comntHolder.comntTxt.setText(testComments.get(i));
        comntHolder.userTxt.setText(testUsers.get(i));
        comntHolder.dateTxt.setText(ConstantMethods.currentDate());
    }

    @Override
    public int getItemCount() {
        return testComments.size();
    }

    class ComntHolder extends RecyclerView.ViewHolder {
        public TextView comntTxt,dateTxt,userTxt;
        public ComntHolder(@NonNull View itemView) {
            super(itemView);
            comntTxt = itemView.findViewById(R.id.comment_txt);
            dateTxt = itemView.findViewById(R.id.date_txt);
            userTxt = itemView.findViewById(R.id.user_txt);
        }
    }
}
