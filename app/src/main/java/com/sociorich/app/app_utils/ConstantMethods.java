package com.sociorich.app.app_utils;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static com.sociorich.app.app_utils.AppApis.SOCIAL_LOGIN;

/**
 * Created by PC on 7/1/2019.
 */

public class ConstantMethods {
    public static ProgressDialog progressDialog;
    public static void setTitleAndBack(AppCompatActivity activity,String title){
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.setTitle(title);
    }

    public static void showProgressbar(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public static void dismissProgressBar(){
        progressDialog.dismiss();
    }
    public static boolean saveAccessToken(Context context, String token){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("token",token).commit();
    }
    public static String getAccessToken(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("token", null);
    }
    public  static SharedPreferences getPreferances(Context context){
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }
    public static boolean saveUserID(Context context, String UserID){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("UserID",UserID).commit();
    }
    public static String getUserID(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("UserID", null);
    }

    public static boolean saveTokenRefresh(Context context, String tokenrefresh){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("tokenrefresh",tokenrefresh).commit();
    }
    public static String getTokenRefresh(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("tokenrefresh", null);
    }

    public static void setStringPreference(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringPreference(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        return date;
    }
    public static String getDateAsWeb(String time) {
        long millisecond = Long.parseLong(time);
        String dateString = DateFormat.format("MMM dd, yyyy", new Date(millisecond)).toString();
        return dateString;
    }
    public static String getDateForComment(String time) {
        long millisecond = Long.parseLong(time);
        String dateString = DateFormat.format("MM/dd/yyyy hh:mm a", new Date(millisecond)).toString();
        return dateString;
    }


    public static String currentDate(){
        Date d = new Date();
        CharSequence s  = DateFormat.format("dd/MM/yyyy,hh:mm a", d.getTime());
        return String.valueOf(s);
    }

    public static void saveArrayListShared(List<String> list, Context context,String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public static List<String> getArrayListShared(Context context,String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("tag", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("tag", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("Tag", "printHashKey()", e);
        }
    }

    public static boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }
//    public static Map<String,String> catIdValue(){
//        Map<String,String> map = new HashMap<>();
//        map.put()
//        return map;
//    }

    public static void twoColoredText(TextView textView, String blackStr, String coloredStr){
        String strClrd = "<b><font color='#F35454'>"+ coloredStr +"</font></b>";
        String strBlk = blackStr +strClrd;
        textView.setText(Html.fromHtml(strBlk));
    }

    public static void showAllInterest(String message,Context context){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("All Interest");
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void socialLogin(String email, String displayName, Context context){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",email);
            jsonObject.put("displayName",displayName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(SOCIAL_LOGIN)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ConstantMethods.setStringPreference("email_prif",email, context);
                            ConstantMethods.setStringPreference("first_name",displayName,context);
                            String identity = response.getString("identity");
                            String accesstoken = response.getString("accessToken");
                            ConstantMethods.saveUserID(context, identity);
                            ConstantMethods.setStringPreference("login_status", "login", context);
                            ConstantMethods.setStringPreference("user_token", accesstoken, context);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public static void setDate(EditText editText, Context context){
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year);
                SimpleDateFormat input = new SimpleDateFormat("dd/MM/yy");
                SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date oneWayTripDate = input.parse(date);
                    String dateo = output.format(oneWayTripDate);
                    String todayDate = todayDate();
                    boolean checkDate = isValidPastDate(todayDate,dateo);
                    if(checkDate) {
                        editText.setText(dateo);
                    }
                    else{
                        Toast.makeText(context, "Past date is not allowed", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, yy, mm, dd);
        datePicker.show();
    }

    public static String todayDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private static boolean isValidPastDate(String selectedDateStr, String myDateStr){
        boolean futureDate = false;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
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

}
