package com.sociorich.app.activities;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.fragments.SearchPostFragment;
import com.sociorich.app.fragments.SerchFriendFragment;

public class SearchResultActivity extends BaseActivity {
    public String queryKeywords = "";
    private Button pplBt,postBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Search Results");
        queryKeywords = getIntent().getStringExtra("search_key");
        ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        pplBt = findViewById(R.id.people_btn);
        postBtn = findViewById(R.id.post_btn);
        loadFragment(new SerchFriendFragment());
        pplBt.setOnClickListener(v->{
            loadFragment(new SerchFriendFragment());
            pplBt.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            postBtn.setBackgroundColor(getResources().getColor(R.color.black_trans80));
        });
        postBtn.setOnClickListener(v->{
            loadFragment(new SearchPostFragment());
            pplBt.setBackgroundColor(getResources().getColor(R.color.black_trans80));
            postBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_srch_rslt;
    }


    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.load_fragment,fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

}
