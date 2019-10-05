package com.sociorich.app.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.activities.SearchResultActivity;
import com.sociorich.app.adapters.DashbordAdapter;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.modals.DashModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sociorich.app.app_utils.AppApis.HOMEPAGE_URL_2;
import static com.sociorich.app.app_utils.AppApis.SEARCH_POST;

public class SearchPostFragment extends Fragment {
    Map<String,String> catMap = new HashMap<>();
    private RecyclerView searchPostList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_srch_post,container,false);
        searchPostList = view.findViewById(R.id.srch_post_list);
        searchPostList.setLayoutManager(new LinearLayoutManager(getActivity()));
        SearchResultActivity searchResultActivity = (SearchResultActivity)getActivity();
        String searchKeyword = searchResultActivity.queryKeywords;
        getHomePageData(SEARCH_POST+searchKeyword+"&pageno=0");
        getCatName(HOMEPAGE_URL_2);
        return view;
    }

    private void getHomePageData(String url){
        List<DashModal> dashModals = new ArrayList<>();
        ConstantMethods.showProgressbar(getActivity());
        String userToken = ConstantMethods.getStringPreference("user_token",getActivity());
        AndroidNetworking
                .get(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("authorization","Bearer "+userToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        ConstantMethods.dismissProgressBar();
                        try {
                            JSONArray response = jsonObject.getJSONArray("content");
                            dashModals.clear();
                            for (int i=0;i<response.length();i++){
                                DashModal dashModal = new DashModal();
                                try {
                                    JSONObject allObjs = response.getJSONObject(i);
                                    JSONObject postJsonObj = allObjs.getJSONObject("post");
                                    JSONObject uProfileJsonObj = allObjs.getJSONObject("userProfile");
                                    String displayName = uProfileJsonObj.getString("displayName");
                                    String postId = postJsonObj.getString("identity");
                                    dashModal.setPostIdStr(postId);
                                    String createdAt = uProfileJsonObj.getString("modifiedAt");
                                    Long longTime = Long.parseLong(createdAt);
                                    String theDate = ConstantMethods.getDate(longTime);
                                    String title = postJsonObj.getString("title");
                                    String socioCrdt = postJsonObj.getString("socioMoneyDonated");
                                    String catId = postJsonObj.getString("categoryId");
                                    dashModal.setCategoryId(catId);
                                    JSONObject profilePicObj = null;
                                    try{
                                        profilePicObj = uProfileJsonObj.getJSONObject("profilePic");
                                    }catch(JSONException je){
                                        //json object not found
                                    }

                                    JSONArray mediaArr = postJsonObj.getJSONArray("mediaList");
                                    List<String> mediaList = new ArrayList<>();
                                    for(int j=0;j<mediaArr.length();j++){
                                        JSONObject mediaObj = mediaArr.getJSONObject(j);
                                        String mediaStr = mediaObj.getString("url");
                                        mediaList.add(mediaStr);
                                    }
                                    dashModal.setMediaList(mediaList);



                                    JSONObject commentObj = allObjs.getJSONObject("comments");
                                    String totalComntStr = commentObj.getString("numberOfElements");
                                    JSONArray contentArr = commentObj.getJSONArray("content");
                                    List<String> userList = new ArrayList<>();
                                    List<String> comentList = new ArrayList<>();
                                    for(int k=0;k<contentArr.length();k++){
                                        JSONObject contentObj = contentArr.getJSONObject(k);
                                        JSONObject userObject = contentObj.getJSONObject("user");
                                        JSONObject comentObject = contentObj.getJSONObject("comment");
                                        String userStr = userObject.getString("displayName");
                                        String commentStr = comentObject.getString("comment");
                                        userList.add(userStr);
                                        comentList.add(commentStr);
                                    }

                                    dashModal.setTestComments(comentList);
                                    dashModal.setTestUsers(userList);

                                    String createdBy = postJsonObj.getString("createdBy");
                                    dashModal.setUserIdentity(createdBy);

                                    JSONArray userExprsnArr = allObjs.getJSONArray("userExpressions");
                                    Log.e("value",""+userExprsnArr);
                                    if(userExprsnArr.length()==0){
                                        dashModal.setmVeryfyStr("");
                                        dashModal.setmLikeStr("");
                                    }
                                    else{
                                        dashModal.setmVeryfyStr("VERIFY");
                                        dashModal.setmLikeStr("LIKE");
                                    }
                                    String exprsnStr = postJsonObj.getString("expressions");
                                    if (exprsnStr.equals("null")) {
                                        dashModal.setLikeStr("0");
                                        dashModal.setVerifyStr("0");

                                    } else {
                                        JSONObject exprsnObj = postJsonObj.getJSONObject("expressions");
                                        JSONObject obj4 = exprsnObj.getJSONObject("countByType");

                                        if (obj4.isNull("VERIFY") == true) {
                                            //   profilepic = ;
                                            dashModal.setVerifyStr("0");
                                        } else {
                                            String verfy = obj4.getString("VERIFY");
                                            dashModal.setVerifyStr(verfy);
                                            // actor.setSt_imgfile(cname1);
                                        }
                                        String like = obj4.getString("LIKE");
                                        dashModal.setLikeStr(like);

                                    }
                                    String profilePicUrl = "";
                                    if (profilePicObj != null) {
                                        profilePicUrl = profilePicObj.getString("url");
                                    }
                                    String catName = catMap.get(catId);
                                    dashModal.setCatNameStr(catName);
                                    dashModal.setUserNameStr(displayName);
                                    dashModal.setDateStr(theDate);
                                    dashModal.setPostDataStr(title);
                                    dashModal.setProfilePicStr(profilePicUrl);
                                    dashModal.setSocioCreStr(socioCrdt);
                                    dashModal.setCommentStr(totalComntStr);
                                    dashModals.add(dashModal);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            DashbordAdapter dashbordAdapter = new DashbordAdapter(dashModals, getActivity());
                            searchPostList.setAdapter(dashbordAdapter);
                            if(dashModals.size()==0){
                                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                            ConstantMethods.dismissProgressBar();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                    }
                });
    }


    private void getCatName(String url){
        AndroidNetworking
                .get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ConstantMethods.dismissProgressBar();
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String createdAt = jsonObject.getString("identity");
                                String name = jsonObject.getString("name");
                                catMap.put(createdAt,name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                        Toast.makeText(getActivity(), "Some data is not there", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
