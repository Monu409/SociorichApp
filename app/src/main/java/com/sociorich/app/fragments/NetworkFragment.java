package com.sociorich.app.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.adapters.MyNetworkAdapter;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.modals.NetworkModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sociorich.app.app_utils.AppApis.MY_NETWORK;

public class NetworkFragment extends Fragment {
    private RecyclerView networkRcylr;
    private Button mNetrkBtn,mFolowrsBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network,container,false);
        networkRcylr = view.findViewById(R.id.netwrk_rcylr);
        networkRcylr.setLayoutManager(new LinearLayoutManager(getActivity()));
        myNetworkData();
        mNetrkBtn = view.findViewById(R.id.my_network);
        mFolowrsBtn = view.findViewById(R.id.my_follower);
        mNetrkBtn.setOnClickListener(v->networkRcylr.setVisibility(View.VISIBLE));
        mFolowrsBtn.setOnClickListener(v->{
            networkRcylr.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "No Followers found", Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    private void myNetworkData(){
        List<NetworkModal> networkModals = new ArrayList<>();
        ConstantMethods.showProgressbar(getActivity());
        String userToken = ConstantMethods.getStringPreference("user_token",getActivity());
        AndroidNetworking
                .get(MY_NETWORK)
                .addHeaders("authorization","Bearer "+userToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        try {
                            JSONArray jsonArray = response.getJSONArray("content");
                            for (int i=0;i<jsonArray.length();i++){
                                NetworkModal networkModal = new NetworkModal();
                                JSONObject childObj = jsonArray.getJSONObject(i);
                                String displayName = childObj.getString("displayName");
                                String socioMoneyBalance = childObj.getString("socioMoneyBalance");
                                String identity = childObj.getString("identity");
                                networkModal.setNameStr(displayName);
                                networkModal.setSocioStr(socioMoneyBalance);
                                networkModal.setIdentity(identity);
                                JSONObject profilePicObj = null;
                                try{
                                    profilePicObj = childObj.getJSONObject("profilePic");
                                    String urlProfile = profilePicObj.getString("url");
                                    networkModal.setProfileStr(urlProfile);
                                }catch(JSONException je){
                                    //json object not found
                                }
                                networkModals.add(networkModal);
                            }
                            MyNetworkAdapter myNetworkAdapter = new MyNetworkAdapter(getActivity(),networkModals);
                            networkRcylr.setAdapter(myNetworkAdapter);

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
