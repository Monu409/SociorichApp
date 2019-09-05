package com.app.sociorichapp.app_utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
