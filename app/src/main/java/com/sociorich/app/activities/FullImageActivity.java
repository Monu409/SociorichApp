package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.sociorich.app.R;
import com.sociorich.app.adapters.ImageAdapter;
import com.sociorich.app.app_utils.ConstantMethods;

import java.util.ArrayList;

public class FullImageActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String headerName = getIntent().getStringExtra("header_name");
        ConstantMethods.setTitleAndBack(this,headerName);
        ViewPager viewPager = findViewById(R.id.view_pager);
        Intent intent = getIntent();
        ArrayList allImages = intent.getStringArrayListExtra("all_images");
        int imgPosition = intent.getIntExtra("image_position",0);
        ImageAdapter adapter = new ImageAdapter(this,allImages,imgPosition);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_full_image;
    }
}
