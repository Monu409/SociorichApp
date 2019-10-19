package com.sociorich.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.sociorich.app.R;
import com.sociorich.app.app_utils.CircleImageView;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.modals.CommentModal;
import com.sociorich.app.modals.UserCommentModal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.sociorich.app.app_utils.AppApis.DELETE_COMMENT;
import static com.sociorich.app.app_utils.AppApis.UPDATE_COMMENT;

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
        String comment = commentModals.get(i).getComntStr();
        comntHolder.comntTxt.setText(comment);
        comntHolder.userTxt.setText(commentModals.get(i).getUserStr());
        comntHolder.dateTxt.setText(commentModals.get(i).getTimeDateStr());
        Glide.with(context)
                .load(commentModals.get(i).getImgUrl())
                .into(comntHolder.userImg);
        String comntIdnetity = commentModals.get(i).getIdentity();
        String userIdentity = ConstantMethods.getUserID(context);
        String currentUsrIdntty = commentModals.get(i).getCreatedBy();
        if(userIdentity.equals(currentUsrIdntty)){
            comntHolder.menuImg.setVisibility(View.VISIBLE);
            comntHolder.menuImg.setOnClickListener(v->deleteEditDialog(comntIdnetity,comment,i));
        }

    }

    @Override
    public int getItemCount() {
        return commentModals.size();
    }

    class ComntHolder extends RecyclerView.ViewHolder {
        public TextView comntTxt,dateTxt,userTxt;
        public CircleImageView userImg;
        public ImageView menuImg;
        public ComntHolder(@NonNull View itemView) {
            super(itemView);
            comntTxt = itemView.findViewById(R.id.comment_txt);
            dateTxt = itemView.findViewById(R.id.date_txt);
            userTxt = itemView.findViewById(R.id.user_txt);
            userImg = itemView.findViewById(R.id.user_img);
            menuImg = itemView.findViewById(R.id.menu_img);
        }
    }
    public void addMoreData(List<CommentModal> commentModals){
        commentModals.addAll(commentModals);
        notifyDataSetChanged();
    }

    private void deleteEditDialog(String identity,String previousComment,int position) {
        final CharSequence[] options = {"Edit Comment", "Delete Comment"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Delete Comment")) {
                    dialog.dismiss();
                    alertDialogForLogout(identity,position);
                } else if (options[item].equals("Edit Comment")) {
                    editCommentDialog(identity,previousComment,position);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void alertDialogForLogout(String identity, int position){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete Post");
        builder1.setMessage("Do you want to delete this post?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePost(identity,position);
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

    private void deletePost(String identity, int position){
        String token = ConstantMethods.getStringPreference("user_token", context);
        AndroidNetworking
                .delete(DELETE_COMMENT+identity)
                .addHeaders("authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Comment deleted", Toast.LENGTH_SHORT).show();
//                        notifyItemRemoved(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,commentModals.size());
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void editComment(String identity, String editedComment, int position){
        String token = ConstantMethods.getStringPreference("user_token", context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("identity",identity);
            jsonObject.put("comment",editedComment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(UPDATE_COMMENT)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization","Bearer "+token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Comment Edited", Toast.LENGTH_SHORT).show();
//                        ((AppCompatActivity)context).finish();
//                        Intent intent = new Intent(context, CommentActivity.class);
//                        context.startActivity(intent);
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void editCommentDialog(String identity, String previousComment,int position){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        alert.setTitle("Edit Comment");
        alert.setView(edittext);
        edittext.setText(previousComment);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String editedCommnt = edittext.getText().toString();
                editComment(identity,editedCommnt,position);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }
}
