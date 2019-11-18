package com.sociorich.app.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.app_utils.CommonVariables;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sociorich.app.app_utils.AppApis.EVENT_CREATE;
import static com.sociorich.app.app_utils.CommonVariables.POST_CATEGORY_PROFILE_VALUES;

public class CreateEventActivity extends BaseActivity {
    private EditText title,desc,uname,cohost,strtDate,endDate,keyAgnda,trgtEstmt,estmtBdgt,location;
    private Button submitBtn;
    private Spinner catSpnr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Create Event");
        title = findViewById(R.id.title);
        desc = findViewById(R.id.description);
        uname = findViewById(R.id.hostname);
        cohost = findViewById(R.id.co_host);
        strtDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        keyAgnda = findViewById(R.id.key_agends);
        trgtEstmt = findViewById(R.id.trgt_estimate);
        estmtBdgt = findViewById(R.id.estmt_bdgt);
        location = findViewById(R.id.location);
        submitBtn = findViewById(R.id.submit_button);
        catSpnr = findViewById(R.id.cause_spinner);
        String userName = ConstantMethods.getStringPreference("first_name",this);
        uname.setText(userName);
        strtDate.setOnClickListener(v->ConstantMethods.setDate(strtDate,this));
        endDate.setOnClickListener(v->ConstantMethods.setDate(endDate,this));
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, CommonVariables.POST_CATEGORY_PROFILE_KEYS);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpnr.setAdapter(aa);

        submitBtn.setOnClickListener(v->{
            JSONObject jsonObject = getJSONForEvent();
            createEvent(jsonObject);
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_event;
    }

    private JSONObject getJSONForEvent(){
        JSONObject jsonObject = new JSONObject();
        String titlStr = title.getText().toString();
        String desStr = desc.getText().toString();
        String unameStr = uname.getText().toString();
        String cohostStr = cohost.getText().toString();
        String strtdtStr = strtDate.getText().toString();
        String enddtStr = endDate.getText().toString();
        String keyagndStr = keyAgnda.getText().toString();
        String trgtEstiStr = trgtEstmt.getText().toString();
        String estibdgtStr = estmtBdgt.getText().toString();
        String locationStr = location.getText().toString();

        String userId = ConstantMethods.getUserID(this);

        int selectedVal = catSpnr.getSelectedItemPosition();
        String catId = POST_CATEGORY_PROFILE_VALUES[selectedVal];

        String sDateForServer = ConstantMethods.changeDateFormate(strtdtStr,"MM/dd/yyyy","yyyy-MM-dd");
        String eDateForServer = ConstantMethods.changeDateFormate(enddtStr,"MM/dd/yyyy","yyyy-MM-dd");

        if(titlStr.isEmpty()||desStr.isEmpty()||unameStr.isEmpty()||cohostStr.isEmpty()||strtdtStr.isEmpty()||
                enddtStr.isEmpty()||keyagndStr.isEmpty()||trgtEstiStr.isEmpty()||estibdgtStr.isEmpty()||locationStr.isEmpty()){
            Toast.makeText(this, "Enter all the fields", Toast.LENGTH_SHORT).show();
        }
        else {
            JSONObject budgtJSON = new JSONObject();
            JSONObject ticketJSON = new JSONObject();
            JSONArray agendaArr = new JSONArray();
            JSONObject agendaObj = new JSONObject();
            JSONArray hostsArr = new JSONArray();
            JSONObject hostsObj = new JSONObject();
            JSONObject locatioObj = new JSONObject();
            JSONArray mediaListArr = new JSONArray();

            try {
                budgtJSON.put("amount",estibdgtStr);
                budgtJSON.put("currency","INR");

                ticketJSON.put("price","200");
                ticketJSON.put("buyingSite","www.bookmyshow.com");

                agendaObj.put("desc",keyagndStr);
                agendaArr.put(0,agendaObj);

                hostsObj.put("hostId",userId);
                hostsObj.put("role","Host");
                hostsArr.put(0,hostsObj);

                locatioObj.put("desc",locationStr);
                locatioObj.put("lat","0");
                locatioObj.put("lng","0");


                jsonObject.put("postedBy",userId);
                jsonObject.put("ownerUserId",userId);
                jsonObject.put("title",titlStr);
                jsonObject.put("desc",desStr);
                jsonObject.put("status","DRAFT");
                jsonObject.put("targetSegment",trgtEstiStr);
                jsonObject.put("budget",budgtJSON);
                jsonObject.put("ticketInfo",ticketJSON);
                jsonObject.put("categoryId",catId);
                jsonObject.put("agendas",agendaArr);
                jsonObject.put("hosts",hostsArr);
                jsonObject.put("expectedStartDate",sDateForServer);
                jsonObject.put("startDate",sDateForServer);
                jsonObject.put("endDate",eDateForServer);
                jsonObject.put("location",locatioObj);
                jsonObject.put("mediaList",mediaListArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    private void createEvent(JSONObject jsonObject) {
        String token = ConstantMethods.getStringPreference("user_token", this);
        AndroidNetworking
                .post(EVENT_CREATE)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer " + token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response",""+response);
                        Toast.makeText(CreateEventActivity.this, "Event created succefully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("response",""+anError);
                        Toast.makeText(CreateEventActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
