package com.app.sociorichapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.sociorichapp.app_utils.AppApis.BASE_URL;

/**
 * Created by PC on 7/1/2019.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText emailEdt,passEdt;
    private Button loginBtn;
    private TextView signUpTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Login");
        emailEdt = findViewById(R.id.email_edt);
        passEdt = findViewById(R.id.password_edt);
        signUpTxt = findViewById(R.id.create_acc_txt);
        loginBtn = findViewById(R.id.login_btn);
        signUpTxt.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_acc_txt:
                startActivity(new Intent(this,CreateAccActivity.class));
                break;
            case R.id.login_btn:
                loginUser();
                break;
        }
    }

    private void loginUser(){
        String userNameOrPhone = emailEdt.getText().toString();
        String password = passEdt.getText().toString();
        String url = BASE_URL+"oauth/token";
        if(userNameOrPhone.isEmpty()){
            emailEdt.setError("Enter email or mobile");
        }
        else if(password.isEmpty()){
            passEdt.setError("Enter Password");
        }
        else {
            ConstantMethods.showProgressbar(this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("grant_type","password");
                jsonObject.put("username",userNameOrPhone);
                jsonObject.put("password",password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking
                    .post(url)
                    .addBodyParameter("Content-Type", "application/x-www-form-urlencoded")
                    .addBodyParameter("Accept", "application/json")
                    .addBodyParameter("authorization","Basic c3JjaC13ZWItYXBwOlhZN2ttem9OemwxMDA=")
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ConstantMethods.dismissProgressBar();
                            Log.e("testRes",""+response);
                        }

                        @Override
                        public void onError(ANError anError) {
                            ConstantMethods.dismissProgressBar();
                            Log.e("testRes",""+anError);
                        }
                    });
        }
    }

    private void verifyUser(){
        String purl = BASE_URL+"api/v1/user/currentuserprofile";
        AndroidNetworking
                .get(purl)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}


/* private void verifyCode_user() {
        //   dialog.ShowProgressDialog();
        if (requestQueue == null) {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("please wait");
            dialog.show();
            dialog.setCancelable(false);
            requestQueue = Volley.newRequestQueue(LoginActivity.this);
        }
        stringRequest12  = new UserRequestCustom(LoginActivity.this, Request.Method.GET, purl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //  dialog.CancelProgressDialog();
                    JSONObject ob1j = new JSONObject(response);
                    username = ob1j.getString("displayName");
                    identity = ob1j.getString("identity");

                    Methods.saveUserID(LoginActivity.this,identity);
                 //   Toast.makeText(getApplicationContext(),"Invalid username or password-2289",Toast.LENGTH_SHORT).show();
                    Intent intent_call= new Intent(LoginActivity.this,UserDashboard.class);
                    intent_call.addFlags(intent_call.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_call);

                } catch (JSONException e) {
                    Intent intent_call= new Intent(LoginActivity.this,UserDashboard.class);
                    intent_call.addFlags(intent_call.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_call);
                   // Toast.makeText(getApplicationContext(),"Invalid username or password---111",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent_call= new Intent(LoginActivity.this,UserDashboard.class);
                intent_call.addFlags(intent_call.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_call);
              //  Toast.makeText(getApplicationContext(),"Invalid username or password-222",Toast.LENGTH_SHORT).show();
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // dialog.CancelProgressDialog();
            }
        }) {
        }

        ;

        stringRequest12.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest12);
    }*/