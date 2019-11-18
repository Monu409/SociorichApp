package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.fragments.AboutForOrgAcc;
import com.sociorich.app.fragments.AboutForShowProfile;
import com.sociorich.app.fragments.AboutFragment;
import com.sociorich.app.fragments.AwardFragment;
import com.sociorich.app.fragments.GalleryFragment;
import com.sociorich.app.fragments.GalleryFragmentOther;
import com.sociorich.app.fragments.NetworkFragment;
import com.sociorich.app.fragments.PostFragment;
import com.sociorich.app.fragments.PostFragmentOther;
import com.sociorich.app.fragments.UserNetworkOther;

public class ProfileDataFragActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String frName = intent.getStringExtra("fr_name");
        String crntUsr = intent.getStringExtra("crnt_usr");
        String accountType = intent.getStringExtra("account_type");
        ConstantMethods.setTitleAndBack(this,frName);
        switch (frName){
            case "About us":
                if(accountType.equals("ORG")){
                    loadFragment(new AboutForOrgAcc());
                }
                else {
                    if (crntUsr.equals("me")) {
                        loadFragment(new AboutFragment());
                    } else {
                        loadFragment(new AboutForShowProfile());
                    }
                }
                break;
            case "Post":
                if(crntUsr.equals("me")){
                    loadFragment(new PostFragment());
                }
                else {
                    loadFragment(new PostFragmentOther());
                }
                break;
            case "Gallery":
                if(crntUsr.equals("me")){
                    loadFragment(new GalleryFragment());
                }
                else {
                    loadFragment(new GalleryFragmentOther());
                }
                break;
            case "Network":
                if(crntUsr.equals("me")){
                    loadFragment(new NetworkFragment());
                }
                else {
                    loadFragment(new UserNetworkOther());
                }
                break;
            case "More Info":
                if(crntUsr.equals("me")){
                    loadFragment(new AwardFragment());
                }
                else {
                    loadFragment(new AwardFragment());
                }
                break;
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profiledatafrag;
    }
    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.pro_frag,fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
