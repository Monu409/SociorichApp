package com.app.sociorichapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.app.sociorichapp.R;
import com.app.sociorichapp.adapters.ImageAdapter;
import com.app.sociorichapp.app_utils.ConstantMethods;

import java.util.ArrayList;

public class FullImageActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Gallery Images");
        ViewPager viewPager = findViewById(R.id.view_pager);
        Intent intent = getIntent();
        ArrayList allImages = intent.getStringArrayListExtra("all_images");
        ImageAdapter adapter = new ImageAdapter(this,allImages);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_full_image;
    }
}
