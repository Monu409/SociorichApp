package com.sociorich.app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.adapters.GalleryAdapter;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sociorich.app.app_utils.AppApis.GALLERY_DATA;

public class GalleryFragmentOther extends Fragment {
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
        String userIdentity = getActivity().getIntent().getStringExtra("user_identity");
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
