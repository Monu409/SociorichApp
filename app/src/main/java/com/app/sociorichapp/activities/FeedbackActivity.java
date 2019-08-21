package com.app.sociorichapp.activities;

import android.os.Bundle;
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

import static com.app.sociorichapp.app_utils.AppApis.FEEDBACK_DATA;

public class FeedbackActivity extends BaseActivity {
    private EditText nameEdt,emailEdt,mobileEdt,cityEdt,messageEdt;
    private Button submitBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Send Your Feedback");
        nameEdt = findViewById(R.id.name_edt);
        emailEdt = findViewById(R.id.email_edt);
        mobileEdt = findViewById(R.id.mobile_edt);
        cityEdt = findViewById(R.id.city_edt);
        messageEdt = findViewById(R.id.message_edt);
        submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(v->sendFeedback());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_feedback;
    }

    private void sendFeedback(){
        String userToken = ConstantMethods.getStringPreference("user_token",this);
        String nameStr = nameEdt.getText().toString();
        String emailStr = emailEdt.getText().toString();
        String mobileStr = mobileEdt.getText().toString();
        String cityStr = cityEdt.getText().toString();
        String messageStr = messageEdt.getText().toString();

        if(nameStr.isEmpty()||emailStr.isEmpty()||mobileStr.isEmpty()||cityStr.isEmpty()||messageStr.isEmpty()){
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        }
        else {
            ConstantMethods.showProgressbar(this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("city", nameStr);
                jsonObject.put("emailId", emailStr);
                jsonObject.put("message", messageStr);
                jsonObject.put("name", cityStr);
                jsonObject.put("phoneNo", mobileStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking
                    .post(FEEDBACK_DATA)
                    .addJSONObjectBody(jsonObject)
                    .addHeaders("authorization", "Bearer " + userToken)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ConstantMethods.dismissProgressBar();
                            try {
                                String result = response.getString("result");
                                if (result.equals("success")) {
                                    Toast.makeText(FeedbackActivity.this, "Submit your data", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(FeedbackActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            ConstantMethods.dismissProgressBar();
                            Toast.makeText(FeedbackActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
