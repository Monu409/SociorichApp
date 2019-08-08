package com.app.sociorichapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.adapters.GalleryAdapter;
import com.app.sociorichapp.app_utils.ConstantMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.sociorichapp.app_utils.AppApis.GALLERY_DATA;

public class GalleryFragment extends Fragment {
    private RecyclerView imageRcylr;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery,container,false);
        imageRcylr = view.findViewById(R.id.imag_rcylr);
        imageRcylr.setLayoutManager(new GridLayoutManager(getActivity(),2));
        getAllTheImages();
        return view;
    }

    private void getAllTheImages(){
        ConstantMethods.showProgressbar(getActivity());
        String userIdentity = ConstantMethods.getUserID(getActivity());
        String userToken = ConstantMethods.getStringPreference("user_token",getActivity());
        AndroidNetworking
                .get(GALLERY_DATA+userIdentity+"?pageno=0")
                .addHeaders("authorization", "Bearer "+userToken)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ConstantMethods.dismissProgressBar();
                        List<String> imageListUrl = new ArrayList<>();
                        for(int i=0;i<response.length();i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String imageUrl = jsonObject.getString("url");
                                imageListUrl.add(imageUrl);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        GalleryAdapter galleryAdapter = new GalleryAdapter(imageListUrl,getActivity());
                        imageRcylr.setAdapter(galleryAdapter);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("theData",""+anError);
                        ConstantMethods.dismissProgressBar();
                    }
                });
    }
}
