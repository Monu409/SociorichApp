package com.app.sociorichapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.CircleImageView;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.app_utils.OnAboutDataReceivedListener;
import com.app.sociorichapp.fragments.AboutForShowProfile;
import com.app.sociorichapp.fragments.AboutFragment;
import com.app.sociorichapp.fragments.GalleryFragment;
import com.app.sociorichapp.fragments.NetworkFragment;
import com.app.sociorichapp.fragments.PostFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;
import static com.app.sociorichapp.app_utils.AppApis.SHOW_PROFILE;

public class ShowProfileActivity extends BaseActivity{
    private TextView intrextTxt, intrestAllTxt, soCrTxt, eqCrTxt, qualityTxt;
    private ImageView deleteBnrImg,edtBnrImg,bannerImg;
    private CircleImageView profileImg;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private boolean appBarExpanded = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Show Profile");
        toolbar = findViewById(R.id.anim_toolbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        appBarLayout = findViewById(R.id.appbar);
        soCrTxt = findViewById(R.id.so_cr_txt);
        eqCrTxt = findViewById(R.id.eq_cr_txt);
        bannerImg = findViewById(R.id.header);
        profileImg = findViewById(R.id.profile_img);
        getProfileData();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.landscape);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {
                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                collapsingToolbar.setContentScrimColor(vibrantColor);
                collapsingToolbar.setStatusBarScrimColor(R.color.black_trans80);
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                } else {
                    appBarExpanded = true;
                }
                invalidateOptionsMenu();
            }
        });

    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AboutForShowProfile();
                case 1:
                    return new PostFragment();
                case 2:
                    return new GalleryFragment();
                case 3:
                    return new NetworkFragment();
                default:
                    return new AboutForShowProfile();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "About";
                case 1:
                    return "Post";
                case 2:
                    return "Gallery";
                case 3:
                    return "Network";
                default:
                    return "Second Tab";
            }
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_show_profile;
    }

    private void getProfileData() {
        String userToken = ConstantMethods.getStringPreference("user_token", this);
        String userIdentity = getIntent().getStringExtra("user_identity");
        AndroidNetworking
                .get(SHOW_PROFILE+userIdentity+"/")
                .addHeaders("authorization", "Bearer " + userToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("test", "" + response);
                        try {
                            String profileDes = response.getString("profileDesc");
                            String dob = response.getString("dob");
                            String wrkPlace = response.getString("workplace");
                            String firstName = response.getString("firstName");
                            ConstantMethods.setTitleAndBack(ShowProfileActivity.this,firstName);
                            JSONObject locationObj = null;
                            String location = "";
                            try {
                                locationObj = response.getJSONObject("location");
                                location = locationObj.getString("desc");
                            } catch (Exception e) {
                                e.getStackTrace();
                            }
                            JSONObject bannerObj = null;
                            String bannerStr = "";
                            try {
                                bannerObj = response.getJSONObject("coverPhoto");
                                bannerStr = bannerObj.getString("url");
                                Glide.with(ShowProfileActivity.this)
                                        .load(bannerStr)
                                        .placeholder(R.drawable.user_profile)
                                        .error(R.drawable.user_profile)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .priority(com.bumptech.glide.Priority.HIGH)
                                        .into(bannerImg);
                            } catch (Exception e) {
                                e.getStackTrace();
                            }
                            mAboutDataListener.onDataReceived(profileDes, wrkPlace, location, dob);
                            JSONObject profileObj = response.getJSONObject("profilePic");
                            String profileUrl = profileObj.getString("url");
                            Glide.with(ShowProfileActivity.this)
                                    .load(profileUrl)
                                    .placeholder(R.drawable.user_profile)
                                    .error(R.drawable.user_profile)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .priority(com.bumptech.glide.Priority.HIGH)

                                    .into(profileImg);
                            String socioMoneyBalance = response.getString("socioMoneyBalance");
                            String equaMoneyBalance = response.getString("equaMoneyBalance");
                            socioMoneyBalance = socioMoneyBalance.substring(0, socioMoneyBalance.length() - 2);
                            equaMoneyBalance = equaMoneyBalance.substring(0, equaMoneyBalance.length() - 2);
                            soCrTxt.setText(socioMoneyBalance);
                            eqCrTxt.setText(equaMoneyBalance);
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("test", "" + anError);
                    }
                });
    }
    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }
    private OnAboutDataReceivedListener mAboutDataListener;
}
