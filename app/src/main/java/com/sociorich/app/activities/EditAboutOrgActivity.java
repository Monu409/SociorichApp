package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static com.sociorich.app.app_utils.AppApis.ORG_PROFILE_INTRO;

public class EditAboutOrgActivity extends BaseActivity{
    private EditText webEdt,addrsEdt,yearEdt,misinEdt,titlEdt,descEdt,dateEdt;
    private Spinner teamSpnr;
    private Button saveBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Edit Details");
        webEdt = findViewById(R.id.web_edt);
        addrsEdt = findViewById(R.id.adres_edt);
        yearEdt = findViewById(R.id.found_edt);
        teamSpnr = findViewById(R.id.team_spnr);
        misinEdt = findViewById(R.id.mission_edt);
        titlEdt = findViewById(R.id.titl_edt);
        descEdt = findViewById(R.id.desc_edt);
        dateEdt = findViewById(R.id.date_edt);
        saveBtn = findViewById(R.id.save_btn);
        dateEdt.setOnClickListener(v-> ConstantMethods.selectAnyDate(dateEdt,this));
        Intent intent = getIntent();
        webEdt.setText(intent.getStringExtra("web_key"));
        addrsEdt.setText(intent.getStringExtra("office_key"));
        yearEdt.setText(intent.getStringExtra("year_key"));
        misinEdt.setText(ProfileUpActivity.missionStatement);

        saveBtn.setOnClickListener(v->{
            JSONObject jsonObject = getSendJson();
            String token = ConstantMethods.getStringPreference("user_token", this);
            AndroidNetworking
                    .post(ORG_PROFILE_INTRO)
                    .addJSONObjectBody(jsonObject)
                    .addHeaders("authorization", "Bearer " + token)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("response",""+response);
                            Toast.makeText(EditAboutOrgActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(EditAboutOrgActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_about_org;
    }

    private JSONObject getSendJson(){
        String ttlStr = titlEdt.getText().toString();
        String desc = descEdt.getText().toString();
        String date = descEdt.getText().toString();
        String formatedDate = "";
        if(!date.isEmpty()) {
            formatedDate = ConstantMethods.changeDateFormate(date, "MM/dd/yyyy", "yyyy-MM-dd");
        }
        JSONObject jsonObject = new JSONObject();
        JSONObject locationObject = new JSONObject();
        JSONArray recognitionsArr = new JSONArray();
        JSONObject awrdObj = new JSONObject();
        String teamStrengthStr = teamSpnr.getSelectedItem().toString();
        try {
            awrdObj.put("title",ttlStr);
            awrdObj.put("desc",desc);
            awrdObj.put("date",formatedDate);
            recognitionsArr.put(0,awrdObj);
            locationObject.put("desc",addrsEdt.getText().toString());
            locationObject.put("lng","0");
            locationObject.put("lat","0");
            jsonObject.put("industryId","org-industry-id-2");
            jsonObject.put("website",webEdt.getText().toString());
            jsonObject.put("location",locationObject);
            jsonObject.put("foundationYear",yearEdt.getText().toString());
            jsonObject.put("employeeSize",teamStrengthStr);
            jsonObject.put("missionStatement",misinEdt.getText().toString());
            if(ttlStr.isEmpty() && desc.isEmpty() && date.isEmpty()){
                recognitionsArr = new JSONArray();
            }
            else if(ttlStr.isEmpty() || desc.isEmpty() || date.isEmpty()){
                Toast.makeText(this, "Please enter Title, Description & Date", Toast.LENGTH_SHORT).show();
                jsonObject = new JSONObject();
                recognitionsArr = new JSONArray();
            }
            jsonObject.put("recognitions",recognitionsArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
