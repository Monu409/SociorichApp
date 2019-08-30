package com.app.sociorichapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.sociorichapp.app_utils.AppApis.SAVE_ABOUT_UPDATE;

public class AboutUpdateActivity extends BaseActivity {
    private EditText locationEdt,workEdt,dobEdt;
    private Button saveBtn,cnclBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Update");
        locationEdt = findViewById(R.id.location_edt);
        workEdt = findViewById(R.id.work_edt);
        dobEdt = findViewById(R.id.dob_edt);
        Intent intent = getIntent();
        String dobTxt = intent.getStringExtra("dobTxt");
        String workTxt = intent.getStringExtra("workTxt");
        String locationTxt = intent.getStringExtra("locationTxt");
        locationEdt.setText(locationTxt);
        workEdt.setText(workTxt);
        dobEdt.setText(dobTxt);
        saveBtn = findViewById(R.id.save_btn);
        cnclBtn = findViewById(R.id.cancel_btn);
        saveBtn.setOnClickListener(v->{
            String locationStr = locationEdt.getText().toString();
            String workStr = workEdt.getText().toString();
            String dobStr = dobEdt.getText().toString();
            if(locationStr.isEmpty()||workStr.isEmpty()||dobStr.isEmpty()){
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            }
            else{
                updateAboutData(locationStr,workStr,dobStr);
            }
        });
        cnclBtn.setOnClickListener(c->onBackPressed());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_about_update;
    }

    private void updateAboutData(String location, String work, String dob){
        ConstantMethods.setStringPreference("location_prif",location,this);
        ConstantMethods.setStringPreference("work_prif",work,this);
        ConstantMethods.setStringPreference("dob_prif",dob,this);
        ConstantMethods.showProgressbar(this);
        String userToken = ConstantMethods.getStringPreference("user_token",this);
        JSONObject jsonObject = new JSONObject();
        JSONObject locationObj = new JSONObject();
        try {
            locationObj.put("desc",location);
            locationObj.put("lat","0");
            locationObj.put("long","0");
            jsonObject.put("workplace",work);
            jsonObject.put("dob",dob);
            jsonObject.put("location",locationObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(SAVE_ABOUT_UPDATE)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer "+userToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        Log.e("response",""+response);
                        Toast.makeText(AboutUpdateActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("response",""+anError);
                        ConstantMethods.dismissProgressBar();
                        Toast.makeText(AboutUpdateActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
    }
}
