package com.app.sociorichapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.modals.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.sociorichapp.app_utils.AppApis.COUNTRY_URL;
import static com.app.sociorichapp.app_utils.AppApis.INDIVISUAL_SIGNUP;

public class IndivisualSignupActivity extends BaseActivity {
    private EditText nameEdt,mobileEdt,emailEdt,passwordEdt,cnfrmPassEdt;
    private Button signUpBtn;
    private Spinner cntryCodSpnr;
    private String cntryCodStr;
    String refer="undefined";
    private TextView alreadyTxt,termsTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Signup as Indivisual");
        nameEdt = findViewById(R.id.name);
        mobileEdt = findViewById(R.id.mobile);
        emailEdt = findViewById(R.id.email);
        passwordEdt = findViewById(R.id.password);
        cnfrmPassEdt = findViewById(R.id.repassowrd);
        signUpBtn = findViewById(R.id.sign_up_button);
        cntryCodSpnr = findViewById(R.id.spinner);
        alreadyTxt = findViewById(R.id.already_txt);
        termsTxt = findViewById(R.id.terms_txt);

        String str1 = "Already Registered,";
        String penalty = " Click here";
        String strb2 = "<u><b><font color='#ef633f'>"+ penalty +"</font></b></u>";
        String strd = str1 +strb2;
        alreadyTxt.setText(Html.fromHtml(strd));
        termsTxt.setOnClickListener(t->{
            Intent intent = new Intent(this, AboutUsActivity.class);
            intent.putExtra("url_is","http://dev.sociorich.com/terms");
            intent.putExtra("title_is","Terms & Conditions");
            startActivity(intent);
        });
        getCountries();


        cntryCodSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedVal = (String) cntryCodSpnr.getSelectedItem();
                    if(selectedVal.equals("Choose Country")){
                        Toast.makeText(IndivisualSignupActivity.this, "Please select country code", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        cntryCodStr = selectedVal.replaceAll("[^0-9]", "");
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        signUpBtn.setOnClickListener(v->postIndivisualData());
        alreadyTxt.setOnClickListener(v->startActivity(new Intent(this, LoginActivity.class)));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_individual_account;
    }

    private void postIndivisualData(){
        String nameStr = nameEdt.getText().toString();
        String mobileStr = mobileEdt.getText().toString();
        String emailStr = emailEdt.getText().toString();
        String passwordStr = passwordEdt.getText().toString();
        String cnfrmPassStr = cnfrmPassEdt.getText().toString();

        if(nameStr.isEmpty()){
            nameEdt.setError("Enter Name");
        }
        else if(mobileStr.isEmpty() && emailStr.isEmpty()){
            mobileEdt.setError("Enter Mobile or Email");
            emailEdt.setError("Enter Mobile or Email");
        }
//        else if(emailStr.isEmpty()){
//            emailEdt.setError("Enter Email");
//        }
        else if(passwordStr.isEmpty()){
            passwordEdt.setError("Enter Password");
        }
        else if(cnfrmPassStr.isEmpty()){
            cnfrmPassEdt.setError("Enter Confirm Password");
        }
        else if(!passwordStr.equals(cnfrmPassStr)){
            passwordEdt.setError("Password not match");
            cnfrmPassEdt.setError("Password not match");
        }
        else{
            ConstantMethods.showProgressbar(this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("emailId",emailStr);
                jsonObject.put("name",nameStr);
                jsonObject.put("password",passwordStr);
                jsonObject.put("phoneCountryCode",cntryCodStr);
                jsonObject.put("phoneNo",mobileStr);
                jsonObject.put("re_password",passwordStr);
                jsonObject.put("refer_identity",refer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking
                    .post(INDIVISUAL_SIGNUP)
                    .addJSONObjectBody(jsonObject)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Content-type", "application/json")
                    .addHeaders("authorization", "aBasic c3JjaC13ZWItYXBwOlhZN2ttem9OemwxMDA=")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String identity= null;
                            try {
                                identity = response.getString("identity");
                                Intent intent = new Intent(IndivisualSignupActivity.this, OtpSendActivity.class);
                                intent.putExtra("user_id", identity);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(IndivisualSignupActivity.this, "Email or phone already exist", Toast.LENGTH_SHORT).show();
                            ConstantMethods.dismissProgressBar();
                        }
                    });
        }
    }

    private void getCountries(){
        ConstantMethods.showProgressbar(this);
        List<String> countryCodeList = new ArrayList<>();
        AndroidNetworking
                .get(COUNTRY_URL)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ConstantMethods.dismissProgressBar();
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String code = jsonObject.getString("dial_code");
                                String country = jsonObject.getString("name");
                                String showString = "+"+code+" ("+country+")";
                                countryCodeList.add(showString);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> counrtAdapter = new ArrayAdapter<String>(IndivisualSignupActivity.this, android.R.layout.simple_spinner_item, countryCodeList);
                        counrtAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        cntryCodSpnr.setAdapter(counrtAdapter);
                        int spinnerPosition = counrtAdapter.getPosition("+91 (India)");
                        cntryCodSpnr.setSelection(spinnerPosition);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
