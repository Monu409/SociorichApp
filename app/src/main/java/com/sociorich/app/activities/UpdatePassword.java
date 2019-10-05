package com.sociorich.app.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import okhttp3.HttpUrl;

import static com.sociorich.app.app_utils.AppApis.BASE_URL;


public class UpdatePassword extends BaseActivity {
EditText password,repassword;
    String pasw,repasw;
    String result;
    static String key,code,userid,emailiddd;
    private ProgressDialog pDialog;
    ProgressDialog dialog;
    String mainurl = BASE_URL+"api/v1/user/setpwd";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Update Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repassword=(EditText) findViewById(R.id.repassword);
        password=(EditText) findViewById(R.id.password);
        Bundle bundle = getIntent().getExtras();
        code = bundle.getString("code");
        emailiddd = bundle.getString("emaiid");
        userid = bundle.getString("userid");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_update_password;
    }

    public  void  update(View arg){
        pasw=password.getText().toString();
        repasw=repassword.getText().toString();
        if (pasw.equals("")){
            Toast.makeText(getApplicationContext(),"Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if (pasw.equals(repasw)){
            AsyncT asyncT = new AsyncT();
            asyncT.execute();
        }
        else {
            Toast.makeText(getApplicationContext(),"Password do not match", Toast.LENGTH_SHORT).show();

        }
    }
    public static HttpUrl buildURLCheck() {
        return new HttpUrl.Builder()
                .scheme("https") //http
                .host("www.a.com")
                .addPathSegment("a")//adds "/pathSegment" at the end of hostname
                .addQueryParameter("a", "a") //add query parameters to the URL
                .addEncodedQueryParameter("a", "a")//add encoded query parameters to the URL
                .build();
    }


    class AsyncT extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdatePassword.this);
            pDialog.setMessage("Please wait.");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(true);
            pDialog.show();
        }
        protected String doInBackground(String... urls) {

            String url ="{\"password\":\""+pasw+"\",\"userId\":\"" + userid + "\",\"verifyCode\":\"" + code + "\"}";
            //  System.out.print("JSON-DATA"+url1);
            InputStream inputStream = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(mainurl);
                //   HttpPost httpPost = new HttpPost("http://34.208.118.103/wcfserviceCustomer.svc/EBSPayment/?");

                String json = url;

                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("authorization", "aBasic c3JjaC13ZWItYXBwOlhZN2ttem9OemwxMDA=");

                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream != null){
                    result = convertInputStreamToString(inputStream);
                }
                else{
                    result = "Did not work!";
                }

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject mainObject = new JSONObject(result);


                Iterator<String> iterator = mainObject.keys();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    Log.i("cHECKobj","key:"+key );
                }
                if(key.equals("error")){
                    Toast.makeText(getApplicationContext(),"Something went wrong.Please try again", Toast.LENGTH_LONG).show();
                }
                else if(key.equals("error")){
                    Toast.makeText(getApplicationContext(),"Something went wrong.Please try again", Toast.LENGTH_LONG).show();

                }
                else if(key.equals("userId")){
                    userid=mainObject.getString("userId");
                    String verfycode=mainObject.getString("verifyCode");
                    Intent intent = new Intent(UpdatePassword.this, SuccessActvity.class);
                  /*  intent.putExtra("userid", userid);
                    intent.putExtra("type", "forget");
                    intent.putExtra("email", emaiid);
                    intent.putExtra("code", verfycode);*/
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(UpdatePassword.this, SuccessActvity.class);
                  /*  intent.putExtra("userid", userid);
                    intent.putExtra("type", "forget");
                    intent.putExtra("email", emaiid);
                    intent.putExtra("code", verfycode);*/
                    startActivity(intent);
                }
               /* json1 = mainObject.getString("StrMessage");
                OrderID = mainObject.getInt("OrderID");*/
            } catch (Exception e) {
                Log.d("InputStream1", e.getLocalizedMessage());
            }


/*
            Toast.makeText(getApplicationContext(),"OrderID-1 : "+OrderID,Toast.LENGTH_LONG).show();
            System.out.println("json1"+json1+result);
            System.out.println("OrderID"+OrderID);*/

            pDialog.dismiss();
        }
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
