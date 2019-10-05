package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;
import org.json.JSONException;
import org.json.JSONObject;
import static com.sociorich.app.app_utils.AppApis.CHANGE_NUMBER;

public class ChangeNumberActivity extends BaseActivity{
    private EditText numEdt;
    private Button sendBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Change Number");
        numEdt = findViewById(R.id.email);
        sendBtn = findViewById(R.id.sign_up_button);
        sendBtn.setOnClickListener(v->{
            String otpStr = numEdt.getText().toString();
            if(otpStr.isEmpty()){
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();
            }
            else{
                sendOtp(otpStr);
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_change_number;
    }

    private void sendOtp(String otpStr){
        String token = ConstantMethods.getStringPreference("user_token", this);
        String token2 = ConstantMethods.getStringPreference("user_id",this);
        String userId = ConstantMethods.getUserID(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("otpCode", otpStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(CHANGE_NUMBER)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer " + token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("result");
                            if(result.equals("success")){
                                Toast.makeText(ChangeNumberActivity.this, "Phone number change successfully", Toast.LENGTH_SHORT).show();
                                String number = getIntent().getStringExtra("phone_no");
                                ConstantMethods.setStringPreference("phone_no_prif",number,ChangeNumberActivity.this);
                                Intent intent = new Intent(ChangeNumberActivity.this,SettingsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ChangeNumberActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
