package com.sociorich.app.app_utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        try {
            int numChildren = getChildCount();
            for (int i = 0; i < numChildren; i++) {
                View child = getChildAt(i);
                if (child != null) {
                    child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int h = child.getMeasuredHeight();
                    heightMeasureSpec = Math.max(heightMeasureSpec, MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}