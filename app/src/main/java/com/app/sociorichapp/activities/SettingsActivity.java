package com.app.sociorichapp.activities;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.fragments.AccountFragment;
import com.app.sociorichapp.fragments.ChangePassFragment;

public class SettingsActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Settings");
        ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_settings;
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AccountFragment();
                case 1:
                    return new ChangePassFragment();
                default:
                    return new AccountFragment();
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
                    return "Account";
                case 1:
                    return "Change Password";
                default:
                    return "Account";
            }
        }
    }
}
