package com.sociorich.app.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sociorich.app.R;
import com.sociorich.app.adapters.MyAdapter;
import com.sociorich.app.app_utils.AndroidMultiPartEntity;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.app_utils.DbHelper;
import com.sociorich.app.app_utils.FileUtils;
import com.sociorich.app.app_utils.Utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static com.sociorich.app.app_utils.AppApis.BASE_URL;
import static com.sociorich.app.app_utils.AppApis.UPDATE_POST;
import static com.sociorich.app.app_utils.AppApis.UPLAOD_SINGLE_IMAGE;
import static com.sociorich.app.app_utils.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;


public class Edit_Post extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private int SELECT_FILE = 1;
    static private boolean isUpdate;
    private String mCurrentPhotoPath;
    static String postupdation = "N";
    String strurl = BASE_URL + "api/v1/media/upload";
    private static final String TAG = Edit_Post.class.getSimpleName();
    private static final int REQUEST_CODE = 6384;
    static String medilalist, value_titel, value_description;
    static String key;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 124;
    long totalSize = 0;
    private String filePath = null;
    private ProgressBar mProgressBar;
    Bitmap myBitmap;
    static String resultq, totalnotification;
    Uri picUri;

    private String userChoosenTask;
    static String valueP, userID;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    static String utype, uurl, related;
    static int j = 0;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private View parentView;
    private GridView listView;
    private ArrayList<Uri> arrayList;
    private ArrayList<String> pathlist;
    ProgressDialog dialog;
    ProgressDialog dialog2;
    private Uri mCapturedImageURI;
    static String usertoken;
    String result;
    private DbHelper mHelper;
    private SQLiteDatabase dataBase;
    int i = 0, length;
    EditText tv_titel, tv_dec;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    public static final int PERMISSION_CAMERA_CODE = 121;
    String[] category = {"Animals", "Community & Development", "Discrimination", "Disasters", "Education", "Environment", "Health", "Homelessness+Poverty", "Spiritual", "Other", "Social News", "Social Discussions", "Social Suggestions", "Social Tasks"};
    private RelativeLayout spinrView;
    private String catID;
    private TextView slctCatTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this, "Edit Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spin = (Spinner) findViewById(R.id.spinner);

        tv_titel = (EditText) findViewById(R.id.titel);
        tv_dec = (EditText) findViewById(R.id.edittext1);
        slctCatTxt = findViewById(R.id.slct_cat_txt);
        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("json_string");
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String title = jsonObject.getString("title");
            String des = jsonObject.getString("desc");
            catID = jsonObject.getString("categoryId");
            String catValue = DashboardActivity.catMap.get(catID);
            tv_titel.setText(title);
            tv_dec.setText(des);
            slctCatTxt.setText(catValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spin.setOnItemSelectedListener(this);
        usertoken = ConstantMethods.getStringPreference("user_token", this);
        userID = ConstantMethods.getUserID(Edit_Post.this);
        listView = (GridView) findViewById(R.id.gv);
        mHelper = new DbHelper(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, category);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        arrayList = new ArrayList<>();
        pathlist = new ArrayList<>();
        Button cnclBtn = findViewById(R.id.cancel_btn);
        cnclBtn.setOnClickListener(c->onBackPressed());
        spinrView = findViewById(R.id.spiner_view);
        spinrView.setOnClickListener(v->{
            Intent intentCat = new Intent(this, CategoryActivity.class);
            startActivityForResult(intentCat,10);
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create__post;
    }

    public void submit_data(View arg) {
        String titleStr,descStr;
        titleStr = tv_titel.getText().toString();
        descStr = tv_dec.getText().toString();
        if(slctCatTxt.getText().toString().equals("Select Category")){
            Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show();
        }
        if(titleStr.isEmpty()||descStr.isEmpty()){
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            dialog2 = new ProgressDialog(Edit_Post.this);
            dialog2.setMessage("please wait");
            dialog2.show();
            dialog2.setCancelable(false);
            new UploadFileToServer().execute();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(),category[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void choosefile(View arg) {


        final CharSequence[] items = {"Camera", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Post.this);
        builder.setTitle("Select Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Edit_Post.this);

                if (items[item].equals("Camera")) {
                    userChoosenTask = "Camera";
                    if (result)
                        if (askForPermission())
                            cameraImage();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        if (askForPermission())
                            showChooser();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(target, getString(R.string.app_name));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == Activity.RESULT_OK){
            String message = data.getStringExtra("cat_name");
            catID = data.getStringExtra("cat_id");
            slctCatTxt.setText(message);
        }
        if (requestCode == CAMERA_PHOTO && resultCode == Activity.RESULT_OK) {
            if (imageToUploadUri != null) {
                Uri selectedImage = imageToUploadUri;
                final String path = FileUtils.getPath(this, selectedImage);
                Log.d("Single File Selected", path);
                pathlist.add(path);
                arrayList.add(selectedImage);
                MyAdapter mAdapter = new MyAdapter(Edit_Post.this, arrayList);
                listView.setAdapter(mAdapter);
            } else {
                Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
            }
        }

        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        int currentItem = 0;
                        while (currentItem < count) {
                            Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                            currentItem = currentItem + 1;
                            Log.d("Uri Selected", imageUri.toString());
                            try {
                                // Get the file path from the URI
                                String path = FileUtils.getPath(this, imageUri);

                                Log.d("Multiple File Selected", path);
                                pathlist.add(path);
                                arrayList.add(imageUri);
                                MyAdapter mAdapter = new MyAdapter(Edit_Post.this, arrayList);
                                listView.setAdapter(mAdapter);

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                    } else if (data.getData() != null) {
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            Log.d("Single File Selected", path);
                            pathlist.add(path);
                            arrayList.add(uri);
                            MyAdapter mAdapter = new MyAdapter(Edit_Post.this, arrayList);
                            listView.setAdapter(mAdapter);

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    try {
//                        mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
//                        mImageView.setImageBitmap(mImageBitmap);
                        Uri uri = Uri.parse(mCurrentPhotoPath);
                        final String path = FileUtils.getPath(this, uri);
                        Log.d("Single File Selected", path);
                        pathlist.add(path);
                        arrayList.add(uri);
                        MyAdapter mAdapter = new MyAdapter(Edit_Post.this, arrayList);
                        listView.setAdapter(mAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }

    }

    private boolean askForPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            int hasCallPermission = ContextCompat.checkSelfPermission(Edit_Post.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission
                // need to request permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(Edit_Post.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // explain
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(Edit_Post.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    // if denied then working here
                } else {
                    // Request for permission
                    ActivityCompat.requestPermissions(Edit_Post.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }

                return false;
            } else {
                // permission granted and calling function working
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    showChooser();
                } else {
                    // Permission Denied
                    Toast.makeText(Edit_Post.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Post.this);
        final AlertDialog dialog = builder.setMessage("You need to grant access to Read External Storage")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(Edit_Post.this, android.R.color.holo_blue_light));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(Edit_Post.this, android.R.color.holo_red_light));
            }
        });

        dialog.show();

    }


    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public class Registration_Data extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Edit_Post.this);
            dialog.setMessage("please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(strurl); // here is your URL path

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("fileSize", "361558");
                postDataParams.put("file", "/Internal storage/DCIM/Camera/IMG_20190211_232409834.jpg");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {


                // return new String("Exception: " + e.getMessage());
                String gk = e.getMessage().toString();

                System.out.println("zinccccc" + gk);
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

            String newresult = result;
            Log.d("widout xml", newresult);

            JSONObject json = null;
            try {
                json = new JSONObject(newresult);


            } catch (JSONException e) {
                e.printStackTrace();
            }

/*
            if(i==1){
               // image_check1.setVisibility(View.VISIBLE);

            }
            else {
               // photoButton.setVisibility(View.GONE);
            }*/


            super.onPostExecute(result);
            dialog.dismiss();
            //{"error":false,"msg":"Photo saved successfully.","data":[{"id":38,"customer_id":"251002584","user_name":"ARAMPAL","distribution_channel":"30","division":"51","salse_office":"S513","zone":"ZONED","img_name":"251002584","image":"customerimage\/5ae1a06d9bd82.jpeg","created_at":"2018-04-26 09:48:29","updated_at":"2018-04-26 09:48:29"},{"id":39,"customer_id":"251002584","user_name":"ARAMPAL","distribution_channel":"30","division":"51","salse_office":"S513","zone":"ZONED","img_name":"251002584","image":"customerimage\/5ae1a218b4419.jpeg","created_at":"2018-04-26 09:55:36","updated_at":"2018-04-26 09:55:36"},{"id":40,"customer_id":"251002584","user_name":"ARAMPAL","distribution_channel":"30","division":"51","salse_office":"S513","zone":"ZONED","img_name":"251002584","image":"customerimage\/5ae1a2f29f052.jpeg","created_at":"2018-04-26 09:59:14","updated_at":"2018-04-26 09:59:14"},{"id":42,"customer_id":"251002584","user_name":"ARAMPAL","distribution_channel":"30","division":"51","salse_office":"S513","zone":"ZONED","img_name":"251002584","image":"customerimage\/5ae1a5625d5a8.jpeg","created_at":"2018-04-26 10:09:38","updated_at":"2018-04-26 10:09:38"},{"id":48,"customer_id":"251002584","user_name":"ARAMPAL","distribution_channel":"30","division":"51","salse_office":"S513","zone":"ZONED","img_name":"251002584","image":"customerimage\/5ae1a78858b35.jpeg","created_at":"2018-04-26 10:18:48","updated_at":"2018-04-26 10:18:48"}]}

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //  progressBar.setProgress(0);
            length = pathlist.size();


         /*   for ( i=0;i<length;i++){
                valueP=pathlist.get(i);
                Log.e(TAG, "Filepath-1 from server: " + valueP);
                new UploadFileToServer().execute();
            }*/


            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //   progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            //  progressBar.setProgress(progress[0]);

            // updating percentage value
            // txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = uploadFile();
            return result;
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            String rubish = "";

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(UPLAOD_SINGLE_IMAGE);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (pathlist.size() != 0) {
                    File sourceFile = new File(pathlist.get(i));
                    i = i + 1;
                    Log.e(TAG, "Filepath from server: " + valueP);
                    System.out.print("File1" + valueP);
                    // Adding file data to http body
                    entity.addPart("file", new FileBody(sourceFile));

                    // Extra parameters if you want to pass to server
				/*entity.addPart("website",
						new StringBody("www.androidhive.info"));*/
                    entity.addPart("fileSize", new StringBody("545466565"));

                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    httppost.setHeader("authorization", "Bearer " + usertoken);

                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(r_entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }
                    rubish = responseString;
                }
                else{
                    rubish = "";
                }

                } catch(ClientProtocolException e){
                    responseString = e.toString();
                } catch(IOException e){
                    responseString = e.toString();
                }

            return rubish;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            if (!result.equals("")) {
            try {
                    JSONObject mainObject = new JSONObject(result);
                    // utype = mainObject.getString("type");
                    utype = "image/png";
                    uurl = mainObject.getString("url");
                    related = mainObject.getString("relativeUrl");
                    saveData();
                } catch(JSONException e){
                    e.printStackTrace();
                }
                // showing the server response in an alert dialog   utype,uurl,related;
//            showAlert(result);
            }else {
                saveData();
            }
                super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void saveData() {
        dataBase = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //  System.out.println("cart_1233"+cart_productnewprice.getText().toString().replace("₹","").replaceAll(" ",""));static  String
        values.put(DbHelper.KEY_PNAME, "image/png");
        values.put(DbHelper.KEY_PRICE, uurl);
        values.put(DbHelper.KEY_QTY, related);


        System.out.println("");
        if (isUpdate) {
            dataBase.update(DbHelper.TABLE_NAME, values, DbHelper.KEY_ID + "=" + j + 1, null);
        } else {
            dataBase.insert(DbHelper.TABLE_NAME, null, values);
//            Toast.makeText(getApplicationContext(), "Add To Cart", Toast.LENGTH_LONG).show();
        }

        if (i < length) {
            new UploadFileToServer().execute();
        } else {
            getResults();
        }

        // dataBase.close();

    }


    public JSONArray getResults() {
        dataBase = mHelper.getWritableDatabase();
        Cursor cursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME, null);
        JSONArray resultSet_ukg = new JSONArray();
        JSONObject returnObj = new JSONObject();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 1; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME:12121", cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME:88", e.getMessage());
                    }
                }
            }
            resultSet_ukg.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TAG_NAME_ukg", "result=" + resultSet_ukg.toString());
        value_titel = tv_titel.getText().toString();
        value_description = tv_dec.getText().toString();
        medilalist = resultSet_ukg.toString();
        medilalist = medilalist.replace("\\", "");
        // result_save = "result="+resultSet.toString();
        UserInspire asyncT = new UserInspire();
        asyncT.execute();

        return resultSet_ukg;
    }


    class UserInspire extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(cntx);
            pDialog.setMessage("Please wait.");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(true);
            pDialog.show();*/
        }

        protected String doInBackground(String... urls) {
//            String url = "{\"mediaList\":" + medilalist + ",\"postedBy\":\"" + userID + "\",\"ownerUserId\":\"" + userID + "\",\"title\":\"" + value_titel + "\",\"desc\":\"" + value_description + "\",\"categoryId\":\"" + "post-category-40" + "\",\"sharedId\":\"" + "" + "\",\"location\":{}}";
            String url1 = "";
            Intent intent = getIntent();
            String url = intent.getStringExtra("json_string");
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(url);
                jsonObject.remove("title");
                jsonObject.remove("desc");
                jsonObject.remove("categoryId");
                jsonObject.remove("mediaList");

                String userId = jsonObject.getString("postedBy");
                String identity = jsonObject.getString("identity");

//                jsonObject.put("title",tv_titel.getText().toString());
//                jsonObject.put("desc",tv_dec.getText().toString());
//                String test = medilalist.replace("\"", "");
//                String test1 = test.replace("\"/", "");
//                jsonObject.put("mediaList",test1);
//                jsonObject.put("categoryId",catID);
                url1 = "{\"mediaList\":" + medilalist + ",\"postedBy\":\"" + userId + "\",\"ownerUserId\":\"" + userId+ "\",\"identity\":\"" + identity + "\",\"title\":\""
                        + tv_titel.getText().toString() + "\",\"desc\":\"" + tv_dec.getText().toString() + "\",\"categoryId\":\""
                        + catID + "\",\"sharedId\":\"" + "" + "\",\"location\":{}}";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(UPDATE_POST);
                //   HttpPost httpPost = new HttpPost("http://34.208.118.103/wcfserviceCustomer.svc/EBSPayment/?");

                String json = String.valueOf(jsonObject);

                StringEntity se = new StringEntity(url1);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("authorization", "Bearer " + usertoken);

                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if (inputStream != null) {
                    resultq = convertInputStreamToString(inputStream);
                } else {
                    resultq = "Did not work!";
                }

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return resultq;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject mainObject = new JSONObject(result);
                dataBase = mHelper.getWritableDatabase();
                dataBase.execSQL("delete from " + DbHelper.TABLE_NAME);
                Iterator<String> iterator = mainObject.keys();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    Log.i("cHECKobj", "key:" + key);
                }
                if (key.equals("errors")) {
                    // Toast.makeText(getApplicationContext(),"User not registered with SocioRich",Toast.LENGTH_LONG).show();
                } else if (key.equals("error")) {
                    //  Toast.makeText(getApplicationContext(),"User not registered with SocioRich",Toast.LENGTH_LONG).show();

                } else {
                    postupdation = "S";
                    Toast.makeText(getApplicationContext(), "Your post publish successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
             /*       nameuser1.setText(usernamenew);
                    nameuser.setText(usernamenew);
                    updatename.setVisibility(View.GONE);*/
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

            //  pDialog.dismiss();
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

    private static final int CAMERA_PHOTO = 111;
    private Uri imageToUploadUri;

    private void cameraImage() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), format + "_IMAGE.jpg");
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
        startActivityForResult(chooserIntent, CAMERA_PHOTO);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
