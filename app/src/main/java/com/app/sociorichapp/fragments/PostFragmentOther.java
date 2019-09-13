package com.app.sociorichapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.adapters.DashbordAdapter;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.modals.DashModal;
import com.app.sociorichapp.modals.UserCommentModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.app.sociorichapp.app_utils.AppApis.HOMEPAGE_URL_2;
import static com.app.sociorichapp.app_utils.AppApis.MY_POST_URL;

public class PostFragmentOther extends Fragment {
    private RecyclerView postList;
    Map<String,String> catMap = new HashMap<>();
    List<DashModal> dashModals = new ArrayList<>();


    int visibleItemCount, totalItemCount = 1;
    int firstVisiblesItems = 0;
    int totalPages = 1; // get your total pages from web service first response
    int current_page = 0;
    boolean canLoadMoreData = true; // make this variable false while your web service call is going on.
    LinearLayoutManager linearLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post,container,false);
        postList = view.findViewById(R.id.post_list);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        postList.setLayoutManager(linearLayoutManager);
        getHomePageData1("0");
        getCatName();
        return view;
    }

    void getHomePageData1(String pageNo){
        String token = ConstantMethods.getStringPreference("user_token",getActivity());
        String userIdentity = getActivity().getIntent().getStringExtra("user_identity");
//        5cf480c5-d834-425a-9fbe-ea7c18e5a8ed?pageno=
//        String userIdentity = ConstantMethods.getUserID(getActivity());
        ConstantMethods.showProgressbar(getActivity());
        AndroidNetworking
                .get(MY_POST_URL+userIdentity+"?pageno="+pageNo)
                .setPriority(Priority.MEDIUM)
                .addHeaders("authorization","Bearer "+token)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ConstantMethods.dismissProgressBar();
                        List<UserCommentModal> userCommentModals = new ArrayList<>();
                        UserCommentModal userCommentModal = null;
                        canLoadMoreData  = true;

                        for (int i=0;i<response.length();i++){
                            DashModal dashModal = new DashModal();
                            try {
                                JSONObject allObjs = response.getJSONObject(i);
                                JSONObject postJsonObj = allObjs.getJSONObject("post");
                                JSONObject uProfileJsonObj = allObjs.getJSONObject("userProfile");
                                String displayName = uProfileJsonObj.getString("displayName");
                                String postId = postJsonObj.getString("identity");
                                dashModal.setPostIdStr(postId);
                                String createdAt = uProfileJsonObj.getString("createdAt");
                                Long longTime = Long.parseLong(createdAt);
                                String theDate = ConstantMethods.getDate(longTime);
                                String title = postJsonObj.getString("title");
                                String socioCrdt = postJsonObj.getString("socioMoneyDonated");
                                String catId = postJsonObj.getString("categoryId");
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
//                                JSONObject mediaListObj = postJsonObj.getJSONObject("mediaList");
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

                        DashbordAdapter dashbordAdapter = new DashbordAdapter(dashModals,getActivity());
                        postList.setAdapter(dashbordAdapter);

                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getCatName(){
        AndroidNetworking
                .get(HOMEPAGE_URL_2)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
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
                        Toast.makeText(getActivity(), "Some data is not there", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
