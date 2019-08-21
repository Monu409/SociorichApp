package com.app.sociorichapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.adapters.NotificationAdapter;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.modals.NotifModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.sociorichapp.app_utils.AppApis.ALL_NOTIFICATION;

public class NotificationActivity extends BaseActivity {
    private RecyclerView notifList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"All Notification");
        notifList = findViewById(R.id.notif_list);
        notifList.setLayoutManager(new LinearLayoutManager(this));
        getAllNotifications();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_notification;
    }

    private void getAllNotifications(){
        ConstantMethods.showProgressbar(this);
        String userToken = ConstantMethods.getStringPreference("user_token",this);
        AndroidNetworking
                .get(ALL_NOTIFICATION)
                .setPriority(Priority.MEDIUM)
                .addHeaders("authorization", "Bearer " + userToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        if(response == null){
                            Toast.makeText(NotificationActivity.this, "No Notification is found", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            List<NotifModal> notifModals = new ArrayList<>();
                            try {
                                JSONArray jsonArray = response.getJSONArray("content");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    NotifModal notifModal = new NotifModal();
                                    JSONObject childOBJ = jsonArray.getJSONObject(i);
                                    JSONObject lastActorObj = childOBJ.getJSONObject("lastActor");
                                    String displayName = lastActorObj.getString("displayName");
                                    JSONObject profilePicObj = null;
                                    try {
                                        profilePicObj = lastActorObj.getJSONObject("profilePic");
                                    } catch (JSONException je) {
                                        //json object not found
                                    }
                                    String profilePicUrl = "";
                                    if (profilePicObj != null) {
                                        profilePicUrl = profilePicObj.getString("url");
                                    }
//                                String profilePic = profilePicObj.getString("url");
                                    JSONObject notificationObj = childOBJ.getJSONObject("notification");
                                    String type = notificationObj.getString("type");
                                    String timeStamp = notificationObj.getString("modifiedAt");
                                    Long longTime = Long.parseLong(timeStamp);
                                    String theDate = ConstantMethods.getDate(longTime);
                                    JSONObject detailObj = notificationObj.getJSONObject("detail");
                                    String postTitle = detailObj.getString("postTitle");
                                    notifModal.setPostTitle(postTitle);
                                    if (detailObj.has("amount")) {
                                        //Checking address Key Present or not
                                        String amount = detailObj.getString("amount"); // Present Key
                                        notifModal.setAmount(amount);
                                    } else {
                                        //Do Your Staff
                                    }
//                                String amount = detailObj.getString("amount");
                                    String whoGive = detailObj.getString("postTitle");
                                    String showDes = "Donated you" + "3" + " Equa Credits on your post " + whoGive;
                                    notifModal.setNameStr(displayName);
                                    notifModal.setImgUrl(profilePicUrl);
                                    notifModal.setTimeStr(theDate);
                                    notifModal.setDetailsStr(showDes);
                                    notifModal.setType(type);
                                    notifModals.add(notifModal);
                                }
                                NotificationAdapter notificationAdapter = new NotificationAdapter(notifModals, NotificationActivity.this);
                                notifList.setAdapter(notificationAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                        Toast.makeText(NotificationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
