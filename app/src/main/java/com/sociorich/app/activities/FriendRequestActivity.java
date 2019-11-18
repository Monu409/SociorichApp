package com.sociorich.app.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.adapters.FriendRequestAdapter;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.modals.FRModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sociorich.app.app_utils.AppApis.FRIEND_REQUEST;

public class FriendRequestActivity extends BaseActivity {
    private RecyclerView friendReqList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Pending Invitations");
        friendReqList = findViewById(R.id.friend_req_list);
        friendReqList.setLayoutManager(new LinearLayoutManager(this));
        getAllFriendReqData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_friend_request;
    }

    private void getAllFriendReqData(){
        ConstantMethods.showProgressbar(this);
        String userToken = ConstantMethods.getStringPreference("user_token",this);
        List<FRModel> frModels = new ArrayList<>();
        AndroidNetworking
                .get(FRIEND_REQUEST)
                .setPriority(Priority.MEDIUM)
                .addHeaders("authorization","Bearer "+userToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        try {
                            JSONArray jsonArray = response.getJSONArray("content");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject userObj = jsonArray.getJSONObject(i);
                                String displayName = userObj.getString("displayName");
                                String identity = userObj.getString("identity");
                                String mutualConnCount = userObj.getString("mutualConnCount");
                                JSONObject imgObject = userObj.getJSONObject("profilePic");
                                String url = imgObject.getString("url");
                                FRModel frModel = new FRModel();
                                frModel.setUserName(displayName);
                                frModel.setMotualCon(mutualConnCount+" Mutual Connections");
                                frModel.setProImg(url);
                                frModel.setIdentity(identity);
                                frModels.add(frModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        FriendRequestAdapter friendRequestAdapter = new FriendRequestAdapter(frModels,FriendRequestActivity.this);
                        friendReqList.setAdapter(friendRequestAdapter);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error",""+anError);
                    }
                });
    }
}
