package com.app.sociorichapp.activities;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.fragments.SearchPostFragment;
import com.app.sociorichapp.fragments.SerchFriendFragment;

public class SearchResultActivity extends BaseActivity {
    public String queryKeywords = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Search Results");
        queryKeywords = getIntent().getStringExtra("search_key");
        ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_srch_rslt;
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SerchFriendFragment();
                case 1:
                    return new SearchPostFragment();
                default:
                    return new SerchFriendFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Peoples";
                case 1:
                    return "Post";
                default:
                    return "Peoples";
            }
        }
    }
}
