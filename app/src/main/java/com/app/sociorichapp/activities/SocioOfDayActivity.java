package com.app.sociorichapp.activities;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import ahmed.easyslider.EasySlider;
import ahmed.easyslider.SliderItem;

import static com.app.sociorichapp.app_utils.AppApis.GET_BANNER_DATA;

public class SocioOfDayActivity extends BaseActivity {
    private EasySlider easySlider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        easySlider = findViewById(R.id.slider);
        ConstantMethods.setTitleAndBack(this,"Socio of the day");
        getBannerData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    private void getBannerData(){
        List<String> imgList = new ArrayList<>();
        AndroidNetworking
                .get(GET_BANNER_DATA)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<SliderItem> sliderItems = new ArrayList<>();
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject bannerObject = response.getJSONObject(i);
                                JSONObject postObj = bannerObject.getJSONObject("post");
                                JSONArray mediaList = postObj.getJSONArray("mediaList");
                                JSONObject imgObj = mediaList.getJSONObject(0);
                                String urlImg = imgObj.getString("url");
                                String title = postObj.getString("title");
//                                sliderItems.add(new SliderItem(title,urlImg));
                                imgList.add(urlImg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        sliderItems.add(new SliderItem("Cloth distribution by Guwahati unit in the slum area","https://s3.ap-south-1.amazonaws.com/sociorich-prod-media/e497461f-9222-43d4-8602-5a4bd7be857d/f35de591-c830-4692-b698-3eea638392c8.jpg"));
                        sliderItems.add(new SliderItem("सफल शिक्षा सेवा समिति","https://s3.ap-south-1.amazonaws.com/sociorich-prod-media/e20cd776-7bcc-457f-b0a5-44fa01b048ee/65945eb0-beba-4b8c-9515-ae0feb505ace.jpg"));
                        //sliderItems.add(new SliderItem("title3","https://s3.ap-south-1.amazonaws.com/sociorich-prod-media/e20cd776-7bcc-457f-b0a5-44fa01b048ee/f890089f-e474-44f1-a8c1-c3c36762918c.jpg"));
                        sliderItems.add(new SliderItem("Free Art classes","https://s3.ap-south-1.amazonaws.com/sociorich-prod-media/e497461f-9222-43d4-8602-5a4bd7be857d/8dcf241d-ce85-42a1-bacc-4299212ada08.jpg"));
                        easySlider.setPages(sliderItems);
//                        slider = findViewById(R.id.banner_slider);
//                        slider.setAdapter(new MainSliderAdapter(imgList));
//                        slider.setInterval(3000);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

}