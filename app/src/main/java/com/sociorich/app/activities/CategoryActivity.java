package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.adapters.CustomExpandableListAdapter;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sociorich.app.app_utils.AppApis.POST_CATEGORY;

public class CategoryActivity extends BaseActivity{
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private Map<String, String> nameIdentityMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expandableListView = findViewById(R.id.expandableListView);
//        expandableListDetail = ExpandableListDataPump.getData();
        ConstantMethods.setTitleAndBack(this,"Select Category");

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent i = new Intent();
                String catName = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                i.putExtra("cat_name",catName);
                i.putExtra("cat_id", nameIdentityMap.get(catName));
                setResult(RESULT_OK, i);
                finish();
                return false;
            }
        });
        getCatData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_category;
    }

    private void getCatData(){
        String userToken = ConstantMethods.getStringPreference("user_token", this);
        AndroidNetworking
                .get(POST_CATEGORY)
                .addHeaders("authorization","Bearer "+userToken)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> campList = new ArrayList<>();
                        List<String> causList = new ArrayList<>();
                        List<String> activityList = new ArrayList<>();
                        List<String> donationList = new ArrayList<>();
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject childObj = response.getJSONObject(i);
                                String type = childObj.getString("type");
                                String name = childObj.getString("name");
                                String identity = childObj.getString("identity");
                                nameIdentityMap.put(name,identity);
                                String displayOrder = childObj.getString("displayOrder");
                                if(displayOrder.equals("3")){
                                    donationList.add(name);
                                }
                                else if(type.equals("ACTIVITY")&&!displayOrder.equals("3")){
                                    activityList.add(name);
                                }
                                else if(type.equals("CAUSE")){
                                    causList.add(name);
                                }
                                else if(displayOrder.equals("1")){
                                    campList.add(name);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        expandableListDetail = getData(campList,causList,activityList,donationList);
                        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                        expandableListAdapter = new CustomExpandableListAdapter(CategoryActivity.this, expandableListTitle, expandableListDetail);
                        expandableListView.setAdapter(expandableListAdapter);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    public static HashMap<String, List<String>> getData(List<String> campList,List<String> causList,List<String> activityList,List<String> donationList) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        expandableListDetail.put("CAMPAIGN", campList);
        expandableListDetail.put("CAUSES", causList);
        expandableListDetail.put("ACTIVITIES", activityList);
        expandableListDetail.put("DONATION IN KIND", donationList);
        return expandableListDetail;
    }

}
