package com.sociorich.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sociorich.app.R;
import com.sociorich.app.testpac.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

public class PostImageFragment extends Fragment {
    private RecyclerView imgList;
    String u1 = "https://s3.ap-south-1.amazonaws.com/srch-dev-media/fe6b9f8f-c36e-4127-a3a3-9bd3f2f42f78/fbf48d5b-7941-415c-8ea9-326fa593e0ff.jpg";
    List<String> uList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_image,container,false);
        imgList = rootView.findViewById(R.id.img_list);
        imgList.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL));
        uList.add(u1);
        uList.add(u1);
        uList.add(u1);
        CustomAdapter customAdapter = new CustomAdapter(getActivity(),uList);
        imgList.setAdapter(customAdapter);
        return rootView;
    }
}
