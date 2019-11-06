package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.adapters.EventAdapter;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.modals.EventModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sociorich.app.app_utils.AppApis.ALL_EVENT;

public class EventsAvtivity extends BaseActivity {
    private RelativeLayout createEventView;
    private RecyclerView eventList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Events");
        createEventView = findViewById(R.id.create_event_view);
        eventList = findViewById(R.id.evnt_list);
        eventList.setLayoutManager(new LinearLayoutManager(this));
        createEventView.setOnClickListener(v->startActivity(new Intent(this,CreateEventActivity.class)));
        getEventData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_events;
    }

    private void getEventData() {
        String userToken = ConstantMethods.getStringPreference("user_token", this);
        String userIdentity = ConstantMethods.getUserID(this);
        AndroidNetworking
                .get(ALL_EVENT+userIdentity+"/")
                .addHeaders("authorization", "Bearer " + userToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("test", "" + response);
                        List<EventModel> eventModels = new ArrayList<>();
                        for(int i=0;i<response.length();i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                JSONObject childObj = jsonObject.getJSONObject("post");
                                String title = childObj.getString("title");
                                String desc = childObj.getString("desc");
                                String startDate = childObj.getString("startDate");
                                String formatedDate = ConstantMethods.getDateAsWeb(startDate);
                                EventModel eventModel = new EventModel();
                                eventModel.setDate(formatedDate);
                                eventModel.setDesc(desc);
                                eventModel.setTitle(title);
                                eventModels.add(eventModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        EventAdapter eventAdapter = new EventAdapter(eventModels,EventsAvtivity.this);
                        eventList.setAdapter(eventAdapter);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("test", "" + anError);
                    }
                });
    }
}
