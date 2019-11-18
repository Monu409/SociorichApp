package com.sociorich.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.activities.EditAboutOrgActivity;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sociorich.app.app_utils.AppApis.ORG_ABOUT_US;
import static com.sociorich.app.app_utils.AppApis.ORG_PROFILE_INTRO;
import static com.sociorich.app.app_utils.AppApis.SAVE_PROFILE_INTRO;

public class AboutForOrgAcc extends Fragment {
    private TextView introTxt,webTxt,addessTxt,yearTxt,tmStrngthTxt;
    private ImageView introPen,aboutPen;
    LinearLayout introLay;
    private Button saveBtn,cancelBtn;
    private EditText introEdt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_org,container,false);
        introTxt = view.findViewById(R.id.intro_txt);
        webTxt = view.findViewById(R.id.website);
        addessTxt = view.findViewById(R.id.location);
        yearTxt = view.findViewById(R.id.date_year_co);
        tmStrngthTxt = view.findViewById(R.id.tm_strngth);
        introPen = view.findViewById(R.id.pen_intro);
        saveBtn = view.findViewById(R.id.save_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        aboutPen = view.findViewById(R.id.about_pen);
        introLay = view.findViewById(R.id.intro_lay);
        introEdt = view.findViewById(R.id.intro_edt);
        introPen.setOnClickListener(v->{
            introLay.setVisibility(View.VISIBLE);
        });
        cancelBtn.setOnClickListener(v->{
            introLay.setVisibility(View.GONE);
        });
        saveBtn.setOnClickListener(v->{
            String introStr = introEdt.getText().toString();
            if(introStr.isEmpty()){
                Toast.makeText(getActivity(), "Please write something first", Toast.LENGTH_SHORT).show();
            }
            else{
                saveIntro(introStr);
            }
        });
        aboutPen.setOnClickListener(v-> {
            Intent intent = new Intent(getActivity(), EditAboutOrgActivity.class);
            intent.putExtra("web_key",webTxt.getText().toString());
            intent.putExtra("year_key",yearTxt.getText().toString());
            intent.putExtra("office_key",addessTxt.getText().toString());
//            intent.putExtra("web_key",webTxt.getText().toString());
            startActivity(intent);
        });
        getAboutUs();
        return view;
    }

    private void getAboutUs(){
        ConstantMethods.showProgressbar(getActivity());
        String userToken = ConstantMethods.getStringPreference("user_token", getActivity());
        AndroidNetworking
                .get(ORG_ABOUT_US)
                .addHeaders("authorization", "Bearer " + userToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        try {
                            String profileDesc = response.getString("profileDesc");
                            String website = response.getString("website");
                            String employeeSize = response.getString("employeeSize");
                            String foundationYear = response.getString("foundationYear");
                            JSONObject locationObject = response.getJSONObject("location");
                            String locationStr = locationObject.getString("desc");
                            
                            introTxt.setText(profileDesc);
                            webTxt.setText(website);
                            addessTxt.setText(locationStr);
                            yearTxt.setText(foundationYear);
                            tmStrngthTxt.setText(employeeSize);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                        Toast.makeText(getActivity(), "Some data is not available", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveIntro(String intro){
        ConstantMethods.showProgressbar(getActivity());
        String userToken = ConstantMethods.getStringPreference("user_token",getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("profileDesc",intro);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(ORG_PROFILE_INTRO)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer "+userToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        try {
                            String desStr = response.getString("profileDesc");
                            if(desStr.equals(intro)) {
                                Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                introTxt.setText(desStr);
                                introLay.setVisibility(View.GONE);
                            }
                            else {
                                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
