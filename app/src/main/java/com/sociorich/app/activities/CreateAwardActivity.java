package com.sociorich.app.activities;

import android.os.Bundle;
import android.widget.EditText;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateAwardActivity extends BaseActivity{
    private EditText titleEdt,dateEdt,descEdt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleEdt = findViewById(R.id.titl_edt);
        dateEdt = findViewById(R.id.desc_edt);
        descEdt = findViewById(R.id.date_edt);

        dateEdt.setOnClickListener(v-> ConstantMethods.selectAnyDate(dateEdt,this));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_award;
    }

    private JSONObject getSendJson(){
        String ttlStr = titleEdt.getText().toString();
        String desc = descEdt.getText().toString();
        String date = descEdt.getText().toString();
        String formatedDate = ConstantMethods.changeDateFormate(date,"MM/dd/yyyy","yyyy-MM-dd");
        JSONObject jsonObject = new JSONObject();
        JSONObject locationObject = new JSONObject();
        JSONArray recognitionsArr = new JSONArray();
        JSONObject awrdObj = new JSONObject();
        try {
            awrdObj.put("title",ttlStr);
            awrdObj.put("desc",desc);
            awrdObj.put("date",formatedDate);
            recognitionsArr.put(0,awrdObj);
            locationObject.put("desc","desc");
            locationObject.put("lng","0");
            locationObject.put("lat","0");
            jsonObject.put("industryId","industryId");
            jsonObject.put("website","industryId");
            jsonObject.put("location",locationObject);
            jsonObject.put("foundationYear",locationObject);
            jsonObject.put("employeeSize",locationObject);
            jsonObject.put("missionStatement",locationObject);
            jsonObject.put("recognitions",recognitionsArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
