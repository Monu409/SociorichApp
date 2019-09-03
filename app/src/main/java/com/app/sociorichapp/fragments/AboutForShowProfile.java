package com.app.sociorichapp.fragments;

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
import com.app.sociorichapp.R;
import com.app.sociorichapp.activities.AboutUpdateActivity;
import com.app.sociorichapp.activities.ProfileUpActivity;
import com.app.sociorichapp.activities.ShowProfileActivity;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.app_utils.OnAboutDataReceivedListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.sociorichapp.app_utils.AppApis.SAVE_PROFILE_INTRO;

public class AboutForShowProfile extends Fragment implements OnAboutDataReceivedListener {
    private ImageView introPen,aboutPen;
    private EditText introEdt;
    private Button saveBtn,cancelBtn;
    private TextView introTxt,workTxt,locationTxt,dobTxt;
    LinearLayout introLay;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_for_show,container,false);
        introPen = view.findViewById(R.id.pen_intro);
        aboutPen = view.findViewById(R.id.about_pen);
        introEdt = view.findViewById(R.id.intro_edt);
        saveBtn = view.findViewById(R.id.save_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        introTxt = view.findViewById(R.id.intro_txt);
        introLay = view.findViewById(R.id.intro_lay);
        workTxt = view.findViewById(R.id.work);
        locationTxt = view.findViewById(R.id.location);
        dobTxt = view.findViewById(R.id.dob);

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

        aboutPen.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), AboutUpdateActivity.class);
            intent.putExtra("dobTxt",dobTxt.getText().toString());
            intent.putExtra("workTxt",workTxt.getText().toString());
            intent.putExtra("locationTxt",locationTxt.getText().toString());
            startActivity(intent);
        });
        ShowProfileActivity profileUpActivity = (ShowProfileActivity) getActivity();
        profileUpActivity.setAboutDataListener(this);
        return view;
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
                .post(SAVE_PROFILE_INTRO)
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

    @Override
    public void onResume() {
        super.onResume();
        String workSpc = ConstantMethods.getStringPreference("location_prif",getActivity());
        String location = ConstantMethods.getStringPreference("work_prif",getActivity());
        String dob = ConstantMethods.getStringPreference("dob_prif",getActivity());
        workTxt.setText(workSpc);
        locationTxt.setText(location);
        dobTxt.setText(dob);
    }

    @Override
    public void onDataReceived(String intro, String workSpc, String location, String dob) {
        introTxt.setText(intro);
        workTxt.setText(workSpc);
        locationTxt.setText(location);
        dobTxt.setText(dob);
    }
}
