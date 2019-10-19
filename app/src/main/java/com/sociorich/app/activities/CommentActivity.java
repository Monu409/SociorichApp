package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.adapters.CommentAdapter;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.modals.CommentModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sociorich.app.app_utils.AppApis.MY_COMMENT;
import static com.sociorich.app.app_utils.AppApis.MY_INSPIRE_VERIFY;
import static com.sociorich.app.app_utils.AppApis.MY_LOAD_COMMENT;

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
    private TextView loadMoreTxt;
    List<CommentModal> commentModals;
    int pageNo = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Comments");
        comntList = findViewById(R.id.comment_list);
        comntList.setLayoutManager(new LinearLayoutManager(this));
        comntEdt = findViewById(R.id.commnet_edt);
        comntBtn = findViewById(R.id.comment_btn);
        loadMoreTxt = findViewById(R.id.load_more_txt);
        loadMoreTxt.setOnClickListener(v->{
            loadMoreComents(String.valueOf(pageNo));
            pageNo = pageNo+1;
            commentAdapter.notifyDataSetChanged();
            comntList.scrollToPosition(commentModals.size() - 1);
        });
        token = ConstantMethods.getStringPreference("user_token",this);

        Intent intent = getIntent();
        commentModals = (List<CommentModal>) intent.getSerializableExtra("dash_modal");
        if(commentModals==null){
            commentModals = new ArrayList<>();
        }
        commentAdapter = new CommentAdapter(commentModals,CommentActivity.this);
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
                            String firstName = ConstantMethods.getStringPreference("first_name",CommentActivity.this);
                            commentAdapter = new CommentAdapter(commentModals,CommentActivity.this);
                            insertIndex = commentModals.size();
                            comntList.setAdapter(commentAdapter);
                            CommentModal commentModal1 = new CommentModal();
                            commentModal1.setComntStr(comment);
                            commentModal1.setUserStr(firstName);
                            commentModal1.setTimeDateStr(ConstantMethods.currentDate());
                            commentModals.add(insertIndex,commentModal1);
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



    private void loadMoreComents(String pageNo){
        String identity = getIntent().getStringExtra("post_id");
//        7c836a7c-ff7c-41cc-a2c8-8afe032d59d7/comments?pageno=
        AndroidNetworking
                .get(MY_LOAD_COMMENT+identity+"/comments?pageno="+pageNo)
                .addHeaders("authorization","Bearer "+token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("content");
                            List<CommentModal> commentModals = new ArrayList<>();
                            for(int i=0;i<jsonArray.length();i++){
                                CommentModal commentModal = new CommentModal();
                                JSONObject childObj = jsonArray.getJSONObject(i);
                                JSONObject user = childObj.getJSONObject("user");
                                JSONObject comment = childObj.getJSONObject("comment");
                                String commentStr = comment.getString("comment");
                                String displayName = user.getString("displayName");
                                String dateTime = comment.getString("createdAt");
                                String comntDate = ConstantMethods.getDateForComment(dateTime);
                                String identity = comment.getString("identity");

                                JSONObject profilePicObj = null;

                                try{
                                    profilePicObj = user.getJSONObject("profilePic");
                                    String profileImgUrl = profilePicObj.getString("url");
                                    commentModal.setImgUrl(profileImgUrl);
                                }catch(JSONException je){
                                    //json object not found
                                }
                                commentModal.setComntStr(commentStr);
                                commentModal.setUserStr(displayName);
                                commentModal.setTimeDateStr(comntDate);
                                commentModal.setIdentity(identity);

                                commentModals.add(commentModal);

                            }
                            List<CommentModal> finalData = commentModals;
//                            finalData.addAll(commentModals);
//                            finalData.addAll(CommentActivity.this.commentModals);
                            CommentActivity.this.commentModals.addAll(finalData);
                            commentAdapter = new CommentAdapter(CommentActivity.this.commentModals,CommentActivity.this);
                            comntList.setAdapter(commentAdapter);
                            commentAdapter.notifyDataSetChanged();
//                            commentAdapter.addMoreData(commentModals);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CommentActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
