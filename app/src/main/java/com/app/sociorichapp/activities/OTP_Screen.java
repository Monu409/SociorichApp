package com.app.sociorichapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;

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

import static com.app.sociorichapp.app_utils.AppApis.BASE_URL;


public class OTP_Screen extends BaseActivity {
static String type,emailid,userid,emaiid;
    Button resend;
    EditText email;
    String result;
    //private String TAG = JsonRequestActivity.class.getSimpleName();
    static String key;
    private ProgressDialog pDialog;
    ProgressDialog dialog;
    String mainurl = BASE_URL+"api/v1/user/verifypwdcode";
    String resendurl = BASE_URL+"api/v1/user/forgotpwd";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"OTP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ConstantMethods.setTitleAndBack(this,"Otp");
        resend=(Button) findViewById(R.id.resend);
        email=(EditText) findViewById(R.id.email);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        emailid = bundle.getString("email");
        userid = bundle.getString("userid");
        if (type.equals("forget")) {
            resend.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_otp__screen;
    }

    public void resendotp(View arg){


        ResendOtp asyncT = new ResendOtp();
        asyncT.execute();
    }


    public void submit(View arg){
        emailid=email.getText().toString();
        if (emailid.equals("")){
            Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_LONG).show();
        }
        else {
            AsyncT asyncT = new AsyncT();
            asyncT.execute();
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
            pDialog = new ProgressDialog(OTP_Screen.this);
            pDialog.setMessage("Please wait.");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(true);
            pDialog.show();
        }
        protected String doInBackground(String... urls) {

            String url ="{\"resetCode\":\""+emailid+"\",\"userId\":\"" + userid + "\"}";



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
                if(key.equals("errors")){
                    Toast.makeText(getApplicationContext(),"Something went wrong.Please try again", Toast.LENGTH_LONG).show();
                }
                else if(key.equals("error")){
                    Toast.makeText(getApplicationContext(),"Something went wrong.Please try again", Toast.LENGTH_LONG).show();

                }
                else {
                    userid=mainObject.getString("userId");
                    String verfycode=mainObject.getString("verifyCode");
                    Intent intent = new Intent(OTP_Screen.this, UpdatePassword.class);
                    intent.putExtra("userid", userid);
                    intent.putExtra("type", "forget");
                    intent.putExtra("email", emaiid);
                    intent.putExtra("code", verfycode);
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




    class ResendOtp extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OTP_Screen.this);
            pDialog.setMessage("Please wait.");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(true);
            pDialog.show();
        }
        protected String doInBackground(String... urls) {

            String url ="{\"username\":\""+emailid+"\"}";



            //  System.out.print("JSON-DATA"+url1);
            InputStream inputStream = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(resendurl);
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
                if(key.equals("errors")){
                    Toast.makeText(getApplicationContext(),"Something went wrong.Please try again", Toast.LENGTH_LONG).show();
                }
                else if(key.equals("error")){
                    Toast.makeText(getApplicationContext(),"Something went wrong.Please try again", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),"OTP sent on the username provided", Toast.LENGTH_LONG).show();

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
}
