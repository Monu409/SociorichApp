package com.sociorich.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import static com.sociorich.app.app_utils.AppApis.BASE_URL;


public class AdminDetail extends BaseActivity {
static String orgname_p,orgtype_p,orgfselect_p,orgadress_p,orgyear_p,orgwebsite_p,orgtstren_p,adminname_tv,adminemail_tv,adminpassword_tv,adminconfirmpassoword;
    EditText adminname,adminemail,adminpassword,repassowrd;
    String aboutOrgnStr,orgnRnumStr;
    private ProgressDialog pDialog;
    static String key;
    String strurl = BASE_URL+"api/v1/user/createorg";
    String result;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Other Details");
        adminname= findViewById(R.id.adminname);
        adminemail= findViewById(R.id.adminemail);
        adminpassword=(EditText) findViewById(R.id.adminpassword);
        repassowrd=(EditText) findViewById(R.id.repassowrd);

        Bundle bundle = getIntent().getExtras();
        orgname_p = bundle.getString("orgname");
        orgtype_p = bundle.getString("orgtype");
        orgfselect_p = bundle.getString("orgtypes");
        orgadress_p = bundle.getString("orgadress");
        orgyear_p = bundle.getString("orgeyear");
        orgwebsite_p = bundle.getString("prgwebsite");
        orgtstren_p = bundle.getString("orgstrenth");
        aboutOrgnStr = bundle.getString("aboutor_txt");
        orgnRnumStr = bundle.getString("orgn_rnum");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_admin_detail;
    }

    public void submitdata(View arg){
        adminname_tv=adminname.getText().toString();
        adminemail_tv=adminemail.getText().toString();
        adminpassword_tv=adminpassword.getText().toString();
        adminconfirmpassoword=repassowrd.getText().toString();
        if(adminname_tv.isEmpty()||adminemail_tv.isEmpty()||adminpassword_tv.isEmpty()||adminconfirmpassoword.isEmpty()){
            Toast.makeText(this, "All Fields are mandatory", Toast.LENGTH_SHORT).show();
        }
        else {
            AsyncT asyncT = new AsyncT();
            asyncT.execute();
        }
    }

    class AsyncT extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdminDetail.this);
            pDialog.setMessage("Please wait.");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(true);
            pDialog.show();
        }
        protected String doInBackground(String... urls) {

            JSONObject completeObj = new JSONObject();
            JSONObject profileJson = new JSONObject();
            JSONObject locationJson = new JSONObject();
            JSONObject adminJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            try {
                adminJson.put("name",adminname_tv);
                adminJson.put("email",adminemail_tv);
                adminJson.put("phoneNo","null");
                adminJson.put("designation","null");

                locationJson.put("desc",orgadress_p);

                profileJson.put("displayName",orgname_p);
                profileJson.put("website",orgwebsite_p);
                profileJson.put("industryId","org-industry-id-2");
                profileJson.put("otherOrgTypeId","null");
                profileJson.put("supportedCauses",jsonArray);
                profileJson.put("employeeSize",orgtstren_p);
                profileJson.put("location",locationJson);
                profileJson.put("foundationYear",orgyear_p);
                profileJson.put("profileDesc",aboutOrgnStr);
                profileJson.put("orgType","CSR");
                profileJson.put("admin",adminJson);
                profileJson.put("regNum",orgnRnumStr);
                profileJson.put("referIdentity","undefined");

                completeObj.put("password",adminpassword_tv);
                completeObj.put("profile",profileJson);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            //  System.out.print("JSON-DATA"+url1);
            InputStream inputStream = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strurl);
                //   HttpPost httpPost = new HttpPost("http://34.208.118.103/wcfserviceCustomer.svc/EBSPayment/?");

                String json = completeObj.toString();

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
                    Toast.makeText(getApplicationContext(),"This email id is already register",Toast.LENGTH_LONG).show();
                }
                else {
                    confirmEmail(AdminDetail.this);
                }
            } catch (Exception e) {
                Log.d("InputStream1", e.getLocalizedMessage());
            }

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

    private void confirmEmail(Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Login now");
        builder1.setMessage("An Email has been sent to your email id\nPlease Confirm.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(AdminDetail.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
