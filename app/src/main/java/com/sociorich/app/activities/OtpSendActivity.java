package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sociorich.app.app_utils.AppApis.OTP_URL;

public class OtpSendActivity extends BaseActivity {
    private EditText otpEdt;
    private Button submitOtp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Enter OTP");
        otpEdt = findViewById(R.id.otp_edt);
        submitOtp = findViewById(R.id.submit_otp);
        submitOtp.setOnClickListener(v->sendOtp());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_otp_send;
    }

    private void sendOtp(){
        ConstantMethods.showProgressbar(this);
        String otpStr = otpEdt.getText().toString();
        String userId = getIntent().getStringExtra("user_id");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("otpCode",otpStr);
            jsonObject.put("userId",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(OTP_URL)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-type", "application/json")
                .addHeaders("authorization", "aBasic c3JjaC13ZWItYXBwOlhZN2ttem9OemwxMDA=")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("data",response.toString());
                        ConstantMethods.dismissProgressBar();
                        Intent intent = new Intent(OtpSendActivity.this, SuccessActvity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
