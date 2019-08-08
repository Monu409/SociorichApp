package com.app.sociorichapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.activities.SearchResultActivity;
import com.app.sociorichapp.adapters.FriendsAdapter;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.modals.FrndModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.sociorichapp.app_utils.AppApis.BASE_URL;
import static com.app.sociorichapp.app_utils.AppApis.SEARCH_FRIEND_POST;

public class SerchFriendFragment extends Fragment {
    private RecyclerView friendRcylList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_srch_frnd,container,false);
        friendRcylList = view.findViewById(R.id.srch_frnd_list);
        friendRcylList.setLayoutManager(new LinearLayoutManager(getActivity()));
        SearchResultActivity searchResultActivity = (SearchResultActivity)getActivity();
        String searchKeyword = searchResultActivity.queryKeywords;
        getSearchData(searchKeyword);
        return view;
    }

    private void getSearchData(String query){
        ConstantMethods.showProgressbar(getActivity());
        String userToken = ConstantMethods.getStringPreference("user_token",getActivity());
        AndroidNetworking
                .get(SEARCH_FRIEND_POST+query+"&pageno=0")
                .addHeaders("authorization","Bearer "+userToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        try {
                            JSONArray jsonArray = response.getJSONArray("content");
                            List<FrndModal> frndModals = new ArrayList<>();
                            for(int i=0;i<jsonArray.length();i++){
                                FrndModal frndModal = new FrndModal();
                                JSONObject childObj = jsonArray.getJSONObject(i);
                                String displayName = childObj.getString("displayName");
                                String identity = childObj.getString("identity");
                                String friendStatus = childObj.getString("friendStatus");
                                JSONObject profileObj = null;
                                try{
                                    profileObj = childObj.getJSONObject("profilePic");
                                }catch(JSONException je){
                                    //json object not found
                                }
                                String imageUrl = "";
                                if(profileObj!=null) {
                                    imageUrl = profileObj.getString("url");
                                }
                                frndModal.setfNameStr(displayName);
                                frndModal.setImgUrl(BASE_URL+imageUrl);
                                frndModal.setIdentity(identity);
                                frndModal.setFriendStatus(friendStatus);
                                frndModals.add(frndModal);
                            }
                            FriendsAdapter friendsAdapter = new FriendsAdapter(getActivity(),frndModals);
                            friendRcylList.setAdapter(friendsAdapter);
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
}
