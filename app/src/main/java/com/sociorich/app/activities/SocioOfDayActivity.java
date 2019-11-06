package com.sociorich.app.activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.adapters.SlidingImage_Adapter;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.lib_cls.CirclePageIndicator;
import com.sociorich.app.modals.SocoDayModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import static com.sociorich.app.app_utils.AppApis.GET_BANNER_DATA;

public class SocioOfDayActivity extends BaseActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Socio of the Week");
        getBannerData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main1;
    }

    private void init(List<SocoDayModal> socoDayModals) {

        mPager = findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(this, socoDayModals));


        CirclePageIndicator indicator = findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private void getBannerData() {
        AndroidNetworking
                .get(GET_BANNER_DATA)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<SocoDayModal> socoDayModals = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                SocoDayModal socoDayModal = new SocoDayModal();
                                JSONObject bannerObject = response.getJSONObject(i);
                                JSONObject postObj = bannerObject.getJSONObject("post");
                                JSONArray mediaList = postObj.getJSONArray("mediaList");
                                JSONObject imgObj = mediaList.getJSONObject(0);
                                String urlImg = imgObj.getString("url");
                                String title = postObj.getString("title");
                                JSONObject profileObj = bannerObject.getJSONObject("userProfile");
                                String displayName = profileObj.getString("displayName");
//                                String socioMoneyBalance = profileObj.getString("socioMoneyBalance");
                                JSONObject socioMoneyObj = bannerObject.getJSONObject("sotwPost");
                                String socioMonetStr = socioMoneyObj.getString("socioMoney");
                                String socioMoneyBalance = socioMonetStr.substring(0, socioMonetStr.length() - 2);
                                String categoryId = postObj.getString("categoryId");
                                String catValue = DashboardActivity.catMap.get(categoryId);
                                socoDayModal.setCatTxt(catValue);
                                socoDayModal.setDesStr(title);
                                socoDayModal.setImgUrl(urlImg);
                                socoDayModal.setSocoCrdt(socioMoneyBalance);
                                socoDayModal.setuName(displayName);
                                socoDayModals.add(socoDayModal);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        init(socoDayModals);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

}