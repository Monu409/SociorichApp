package com.app.sociorichapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.adapters.CommentAdapter;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.modals.CommentModal;
import com.app.sociorichapp.modals.DashModal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.sociorichapp.app_utils.AppApis.MY_COMMENT;
import static com.app.sociorichapp.app_utils.AppApis.MY_INSPIRE_VERIFY;
import static com.app.sociorichapp.app_utils.AppApis.MY_UPDATE_COMMENT;

public class CommentActivity extends BaseActivity {
    private RecyclerView comntList;
    private EditText comntEdt;
    private Button comntBtn;
    private ArrayList<ArrayList<String>> commentUserLists;
    private ArrayList<String> commentLists;
    CommentAdapter commentAdapter;
    int insertIndex = 0;
    String token;
    List<String> userComment;
    List<String> userName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Comments");
        comntList = findViewById(R.id.comment_list);
        comntList.setLayoutManager(new LinearLayoutManager(this));
        comntEdt = findViewById(R.id.commnet_edt);
        comntBtn = findViewById(R.id.comment_btn);
        token = ConstantMethods.getStringPreference("user_token",this);

        Intent intent = getIntent();
        DashModal dashModal = (DashModal) intent.getSerializableExtra("dash_modal");
        userComment = dashModal.getTestComments();
        userName = dashModal.getTestUsers();
        commentAdapter = new CommentAdapter(userName,userComment,CommentActivity.this);
        comntList.setAdapter(commentAdapter);

        comntBtn.setOnClickListener(v->{
            String comntStr = comntEdt.getText().toString();
            if(comntStr.isEmpty()){
                Toast.makeText(this, "Please enter comment", Toast.LENGTH_SHORT).show();
            }
            else{
                commnetOnPost(comntStr);
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_comment;
    }

    private void commnetOnPost(String userCommnet){
        CommentModal commentModal = new CommentModal();
        JSONObject jsonObject = new JSONObject();
        String postId = getIntent().getStringExtra("post_id");
        String token = ConstantMethods.getStringPreference("user_token",this);
        String userId = ConstantMethods.getUserID(this);
        try {
            jsonObject.put("comment",userCommnet);
            jsonObject.put("postId",postId);
            jsonObject.put("postedBy",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(MY_COMMENT)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization","Bearer "+token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ghg",""+response);
                        try {
                            String comment = response.getString("comment");
                            commentAdapter = new CommentAdapter(userName,userComment,CommentActivity.this);
                            comntList.setAdapter(commentAdapter);
                            userComment.add(insertIndex, comment);
                            userName.add(insertIndex, "Test Developer");
                            commentAdapter.notifyItemInserted(insertIndex);
                            insertIndex = insertIndex+1;
                            comntEdt.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ghg",""+anError);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        incrementCommentLabels();
    }

    private void incrementCommentLabels(){
        AndroidNetworking
                .get(MY_UPDATE_COMMENT)
                .addHeaders("authorization","Bearer "+token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void checkVerifyAndInspire(String changeValue){
        String postId = getIntent().getStringExtra("post_id");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("postId",postId);
            jsonObject.put("type","LIKE");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(MY_INSPIRE_VERIFY)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization","Bearer "+token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("result");
                            if(result.equals("success")){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
