package com.sociorich.app.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.sociorich.app.app_utils.AppApis.SAVE_ABOUT_UPDATE;

public class AboutUpdateActivity extends BaseActivity {
    private EditText locationEdt, workEdt, dobEdt;
    private Button saveBtn, cnclBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this, "Update");
        locationEdt = findViewById(R.id.location_edt);
        workEdt = findViewById(R.id.work_edt);
        dobEdt = findViewById(R.id.dob_edt);
        Intent intent = getIntent();
        String dobTxt = intent.getStringExtra("dobTxt");
        String date2 = changeDateFormate(dobTxt);
        String workTxt = intent.getStringExtra("workTxt");
        String locationTxt = intent.getStringExtra("locationTxt");
        locationEdt.setText(locationTxt);
        workEdt.setText(workTxt);
        dobEdt.setText(date2);
        saveBtn = findViewById(R.id.save_btn);
        cnclBtn = findViewById(R.id.cancel_btn);
        dobEdt.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                            + "/" + String.valueOf(year);
                    SimpleDateFormat input = new SimpleDateFormat("dd/MM/yy");
                    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date oneWayTripDate = input.parse(date);
                        String dateo = output.format(oneWayTripDate);
                        String todayDate = todayDate();
                        boolean checkDate = isValidPastDate(dateo,todayDate);
                        if(checkDate) {
                            dobEdt.setText(dateo);
                        }
                        else{
                            Toast.makeText(AboutUpdateActivity.this, "Future date is not allowed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, yy, mm, dd);
            datePicker.show();
        });
        saveBtn.setOnClickListener(v -> {
            String locationStr = locationEdt.getText().toString();
            String workStr = workEdt.getText().toString();
            String dobStr = dobEdt.getText().toString() + "T00:00:00.000Z";
            if (locationStr.isEmpty() || workStr.isEmpty() || dobStr.isEmpty()) {
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            } else {
                updateAboutData(locationStr, workStr, dobStr);
            }
        });
        cnclBtn.setOnClickListener(c -> onBackPressed());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_about_update;
    }

    private void updateAboutData(String location, String work, String dob) {
        ConstantMethods.setStringPreference("location_prif", location, this);
        ConstantMethods.setStringPreference("work_prif", work, this);
        ConstantMethods.setStringPreference("dob_prif", dob, this);
        ConstantMethods.showProgressbar(this);
        String userToken = ConstantMethods.getStringPreference("user_token", this);
        JSONObject jsonObject = new JSONObject();
        JSONObject locationObj = new JSONObject();
        try {
            locationObj.put("desc", location);
            locationObj.put("lat", 0);
            locationObj.put("long", 0);
            jsonObject.put("workplace", work);
            jsonObject.put("dob", dob);
            jsonObject.put("location", locationObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(SAVE_ABOUT_UPDATE)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer " + userToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        Log.e("response", "" + response);
                        Toast.makeText(AboutUpdateActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AboutUpdateActivity.this, ProfileUpActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("response", "" + anError);
                        ConstantMethods.dismissProgressBar();
                        Toast.makeText(AboutUpdateActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
    }

    private String changeDateFormate(String prvsDate) {
        String inputPattern = "MMM dd, yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(prvsDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private boolean isValidPastDate(String selectedDateStr, String myDateStr){
        boolean futureDate = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateSlctd = null;
        Date dateMy = null;
        try {
            dateSlctd = sdf.parse(selectedDateStr);
            dateMy = sdf.parse(myDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("date1 : " + sdf.format(dateSlctd));
        System.out.println("date2 : " + sdf.format(dateMy));

        if (dateSlctd.compareTo(dateMy) > 0) {
            futureDate = false;
        } else if (dateSlctd.compareTo(dateMy) < 0) {
            futureDate = true;
        } else if (dateSlctd.compareTo(dateMy) == 0) {
            futureDate = false;
        } else {
            System.out.println("How to get here?");
        }
        return futureDate;
    }

    private String todayDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }
}
