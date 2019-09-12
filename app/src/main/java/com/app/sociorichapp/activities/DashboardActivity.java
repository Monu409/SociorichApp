package com.app.sociorichapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.adapters.DashbordAdapter;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.app_utils.DbHelper;
import com.app.sociorichapp.modals.CommentModal;
import com.app.sociorichapp.modals.DashModal;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.app.sociorichapp.app_utils.AppApis.BASE_URL;
import static com.app.sociorichapp.app_utils.AppApis.GET_BANNER_DATA;
import static com.app.sociorichapp.app_utils.AppApis.HOMEPAGE_URL_2;
import static com.app.sociorichapp.app_utils.AppApis.HOMEPAGE_URL_LOGIN;
import static com.app.sociorichapp.app_utils.AppApis.HOMEPAGE_URL_LOGOUT;
import static com.app.sociorichapp.app_utils.AppApis.MY_INTEREST_URL;
import static com.app.sociorichapp.app_utils.AppApis.MY_NETWORK_URL;

public class DashboardActivity extends AppCompatActivity {
    Map<String,String> catMap = new HashMap<>();
    private RecyclerView homeList;
    private LinearLayout createPostView,withLoginHeader,tabView;
    private RelativeLayout logoutHeader;
    private TextView loginTxt,signupTxt,globlTxt,netwrkTxt,intrstTxt;
    private LinearLayout globalView,networkView,intrestView;
    static String postupdation="N";
    private SQLiteDatabase dataBase;
    private Button aboutBtn,socioDayBtn;

    int visibleItemCount, totalItemCount = 1;
    int firstVisiblesItems = 0;
    int totalPages = 10; // get your total pages from web service first response
    int current_page = 1;
    boolean canLoadMoreData = true; // make this variable false while your web service call is going on.
    LinearLayoutManager linearLayoutManager;
    String tabTag = "Global";
    int i = 0;
    List<DashModal> dashModals = new ArrayList<>();
    List<String> imgList = new ArrayList<>();
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS= 7;
    int whichPosition = 0;
    DashbordAdapter dashbordAdapter;
    int itemLimit = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkAndRequestPermissions()) {
            String checkLogin = ConstantMethods.getStringPreference("login_status", this);
            if (checkLogin.equals("login")) {

            } else {
                getSupportActionBar().hide();
            }
            setContentView(R.layout.activity_dashboard);
            setTitle("SocioRich");
//            checkAndroidVersion();
            loginTxt = findViewById(R.id.login_txt);
            signupTxt = findViewById(R.id.sign_up_txt);
            aboutBtn = findViewById(R.id.about_btn);
            loginTxt.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
            signupTxt.setOnClickListener(view -> startActivity(new Intent(this, CreateAccActivity.class)));
            homeList = findViewById(R.id.home_list);
            createPostView = findViewById(R.id.create_post_view);
//        withLoginHeader = findViewById(R.id.with_login_header);
            tabView = findViewById(R.id.tab_view);
            logoutHeader = findViewById(R.id.logout_header);
            globalView = findViewById(R.id.global_view);
            networkView = findViewById(R.id.network_view);
            intrestView = findViewById(R.id.interest_view);
            globlTxt = findViewById(R.id.globl_txt);
            netwrkTxt = findViewById(R.id.netwrk_txt);
            intrstTxt = findViewById(R.id.intrst_txt);
            socioDayBtn = findViewById(R.id.socio_day_btn);
            socioDayBtn.setOnClickListener(v->startActivity(new Intent(this,SocioOfDayActivity.class)));

//        getBannerData();
            if (checkLogin.equals("login")) {
//            withLoginHeader.setVisibility(View.VISIBLE);
                createPostView.setVisibility(View.VISIBLE);
                tabView.setVisibility(View.VISIBLE);
                logoutHeader.setVisibility(View.GONE);
                getHomePageData(HOMEPAGE_URL_LOGIN, "0");
                getCatName(HOMEPAGE_URL_2);
                globlTxt.setTextColor(Color.parseColor("#ef633f"));
                globalView.setOnClickListener(v -> {
                    dashModals.clear();
                    tabTag = "Global";
                    changeTextColor();
                    globlTxt.setTypeface(null, Typeface.BOLD);
                    getHomePageData(HOMEPAGE_URL_LOGIN, "0");
                    getCatName(HOMEPAGE_URL_2);
                });
                networkView.setOnClickListener(v -> {
                    dashModals.clear();
                    tabTag = "Network";
                    changeTextColor();
                    netwrkTxt.setTypeface(null, Typeface.BOLD);
                    getHomePageData(MY_NETWORK_URL, "0");
                    getCatName(HOMEPAGE_URL_2);
                });
                intrestView.setOnClickListener(v -> {
                    dashModals.clear();
                    tabTag = "Intrest";
                    changeTextColor();
                    intrstTxt.setTypeface(null, Typeface.BOLD);
                    getHomePageData(MY_INTEREST_URL, "0");
                    getCatName(HOMEPAGE_URL_2);
                });
                aboutBtn.setText("Feedback");
                aboutBtn.setOnClickListener(view -> startActivity(new Intent(this, FeedbackActivity.class)));
            } else if (checkLogin.equals("logout")||checkLogin.equals("")) {
//            withLoginHeader.setVisibility(View.GONE);
                createPostView.setVisibility(View.GONE);
                tabView.setVisibility(View.GONE);
                logoutHeader.setVisibility(View.VISIBLE);
                getHomePageData(HOMEPAGE_URL_LOGOUT, "");
                getCatName(HOMEPAGE_URL_2);
                aboutBtn.setText("About");
                aboutBtn.setOnClickListener(view -> {
                    Intent intent = new Intent(this, AboutUsActivity.class);
                    intent.putExtra("url_is", BASE_URL+"about");
                    intent.putExtra("title_is", "About Us");
                    startActivity(intent);
                });
            } else {
//            withLoginHeader.setVisibility(View.GONE);
                createPostView.setVisibility(View.GONE);
                tabView.setVisibility(View.GONE);
                logoutHeader.setVisibility(View.VISIBLE);
                getHomePageData(HOMEPAGE_URL_LOGOUT, "");
                getCatName(HOMEPAGE_URL_2);
            }
            linearLayoutManager = new LinearLayoutManager(this);
            homeList.setLayoutManager(linearLayoutManager);
            dashbordAdapter = new DashbordAdapter(dashModals,DashboardActivity.this);
            homeList.setAdapter(dashbordAdapter);

//            homeList.setNestedScrollingEnabled(false);

            homeList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = linearLayoutManager.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();
                        firstVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                        if (canLoadMoreData) {
                            if ((visibleItemCount + firstVisiblesItems) >= totalItemCount) {
                                if (current_page < totalPages) {
                                    canLoadMoreData = false;
                                    dashbordAdapter.notifyDataSetChanged();

                                    whichPosition=itemLimit;
                                    itemLimit = itemLimit+10;
                                    i =i+1;
                                    String pageNoStr = String.valueOf(current_page);
                                    if (checkLogin.equals("login")) {
                                        if (tabTag.equals("Global")) {
                                            getHomePageData(HOMEPAGE_URL_LOGIN, String.valueOf(i));
                                        } else if (tabTag.equals("Network")) {
                                            getHomePageData(MY_NETWORK_URL, pageNoStr);
                                        } else if (tabTag.equals("Intrest")) {
                                            getHomePageData(MY_INTEREST_URL, pageNoStr);
                                        }
                                    }
//                                getHomePageData1(pageNoStr);
                                }
                            }
                        }

                    }
                }
            });
            createPostView.setOnClickListener(v -> {
                Intent intent = new Intent(this, Create_Post.class);
                startActivity(intent);
            });

            if (postupdation == null) {

            } else {
                if (postupdation.equals("S")) {
                    postupdation = "N";
                    DbHelper db = new DbHelper(this);
                    dataBase = db.getWritableDatabase();
                    dataBase.execSQL("delete from " + DbHelper.TABLE_NAME);
                } else {

                }
            }
        }
    }

    private void getHomePageData(String url, String pageNo){
        ConstantMethods.showProgressbar(this);
        String userToken = ConstantMethods.getStringPreference("user_token",this);
        String checkLogin = ConstantMethods.getStringPreference("login_status", this);
        String authStr;
        String barToken;
        if(checkLogin.equals("login")){
            authStr = "authorization";
            barToken = "Bearer "+userToken;
        }
        else {
            authStr = "";
            barToken = "";
        }
        AndroidNetworking
                .get(url+pageNo)
                .setPriority(Priority.MEDIUM)
                .addHeaders(authStr,barToken)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<DashModal> dashModalsNew = new ArrayList<>();
                        ConstantMethods.dismissProgressBar();
                        for (int i=0;i<response.length();i++){
                            DashModal dashModal = new DashModal();
                            try {
                                JSONObject allObjs = response.getJSONObject(i);
                                JSONObject postJsonObj = allObjs.getJSONObject("post");
                                dashModal.setPostObj(postJsonObj);
                                JSONObject uProfileJsonObj = allObjs.getJSONObject("userProfile");
                                String displayName = uProfileJsonObj.getString("displayName");
                                String postId = postJsonObj.getString("identity");
                                dashModal.setPostIdStr(postId);
                                String createdAt = postJsonObj.getString("modifiedAt");
                                String theDate = ConstantMethods.getDateAsWeb(createdAt);
                                String title = postJsonObj.getString("title");
                                String socioCrdt = postJsonObj.getString("socioMoneyDonated");
                                String catId = postJsonObj.getString("categoryId");
                                String desStr = postJsonObj.getString("desc");
                                String postOwnerUserId = postJsonObj.getString("ownerUserId");
                                JSONObject profilePicObj = null;
                                dashModal.setCategoryId(catId);
                                try{
                                    profilePicObj = uProfileJsonObj.getJSONObject("profilePic");
                                }catch(JSONException je){
                                    //json object not found
                                }

                                JSONArray mediaArr = postJsonObj.getJSONArray("mediaList");
                                List<String> mediaList = new ArrayList<>();
                                for(int j=0;j<mediaArr.length();j++){
                                    JSONObject mediaObj = mediaArr.getJSONObject(j);
                                    String mediaStr = mediaObj.getString("url");
                                    if(!mediaStr.equals(""))
                                        mediaList.add(mediaStr);
                                }
                                dashModal.setMediaList(mediaList);



                                JSONObject commentObj = allObjs.getJSONObject("comments");
                                String totalComntStr = commentObj.getString("totalElements");
                                JSONArray contentArr = commentObj.getJSONArray("content");
                                List<String> userList = new ArrayList<>();
                                List<String> comentList = new ArrayList<>();
                                List<CommentModal> commentModals = new ArrayList<>();
                                for(int k=0;k<contentArr.length();k++){
                                    CommentModal commentModal = new CommentModal();
                                    JSONObject contentObj = contentArr.getJSONObject(k);
                                    JSONObject userObject = contentObj.getJSONObject("user");
                                    JSONObject comentObject = contentObj.getJSONObject("comment");
                                    String userStr = userObject.getString("displayName");
                                    String commentStr = comentObject.getString("comment");
                                    String dateTime = comentObject.getString("createdAt");
                                    dashModal.setLongTime(dateTime);
                                    String comntDate = ConstantMethods.getDateForComment(dateTime);
                                    commentModal.setComntStr(commentStr);
                                    commentModal.setUserStr(userStr);
                                    commentModal.setTimeDateStr(comntDate);
                                    commentModals.add(commentModal);
                                    dashModal.setCommentModals(commentModals);
                                }


                                JSONObject sharedProfile = null;
                                try{
                                    sharedProfile = allObjs.getJSONObject("sharedProfile");
                                    String shrName = sharedProfile.getString("displayName");
                                    String shrDateTime = sharedProfile.getString("modifiedAt");
//                                    String shrCat = sharedProfile.getString("categoryId");
                                    JSONObject profilePicObjShr = null;
                                    try{
                                        profilePicObjShr = uProfileJsonObj.getJSONObject("profilePic");
                                        dashModal.setShrObj(sharedProfile);
                                    }catch(JSONException je){
                                        dashModal.setShrObj(sharedProfile);
                                    }
                                    String shrCat = "Descrimination";
                                    String shrTheDate = ConstantMethods.getDateAsWeb(shrDateTime);
                                    dashModal.setShareUsrName(shrName);
                                    dashModal.setShareDate(shrTheDate);
                                    dashModal.setShareCat(shrCat);
                                }catch(JSONException je){
                                    //json object not found
                                }


                                dashModal.setTestComments(comentList);
                                dashModal.setTestUsers(userList);

                                String createdBy = postJsonObj.getString("createdBy");
                                dashModal.setUserIdentity(createdBy);

                                JSONArray userExprsnArr = allObjs.getJSONArray("userExpressions");
                                Log.e("value",""+userExprsnArr);
                                if(userExprsnArr.length()==0){
                                    dashModal.setmVeryfyStr("");
                                    dashModal.setmLikeStr("");
                                }
                                else{
                                    dashModal.setmVeryfyStr("VERIFY");
                                    dashModal.setmLikeStr("LIKE");
                                }
                                String exprsnStr = postJsonObj.getString("expressions");
                                if (exprsnStr.equals("null")) {
                                    dashModal.setLikeStr("0");
                                    dashModal.setVerifyStr("0");

                                } else {
                                    JSONObject exprsnObj = postJsonObj.getJSONObject("expressions");
                                    JSONObject obj4 = exprsnObj.getJSONObject("countByType");

                                    if (obj4.isNull("VERIFY") == true) {
                                        //   profilepic = ;
                                        dashModal.setVerifyStr("0");
                                    } else {
                                        String verfy = obj4.getString("VERIFY");
                                        dashModal.setVerifyStr(verfy);
                                        // actor.setSt_imgfile(cname1);
                                    }
                                    String like = obj4.getString("LIKE");
                                    dashModal.setLikeStr(like);

                                }
                                String profilePicUrl = "";
                                if (profilePicObj != null) {
                                    profilePicUrl = profilePicObj.getString("url");
                                }
                                String catName = catMap.get(catId);
                                dashModal.setCatNameStr(catName);
                                dashModal.setUserNameStr(displayName);
                                dashModal.setDateStr(theDate);
                                dashModal.setPostDataStr(title);
                                dashModal.setProfilePicStr(profilePicUrl);
                                dashModal.setSocioCreStr(socioCrdt);
                                dashModal.setCommentStr(totalComntStr);
                                dashModal.setDesStr(desStr);
                                dashModal.setPostOwnerUserId(postOwnerUserId);
                                dashModalsNew.add(dashModal);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(dashModalsNew.size()==0){
                            Toast.makeText(DashboardActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                        dashbordAdapter.addMoreData(dashModalsNew);
                        canLoadMoreData = true;

//                        homeList.scrollToPosition(whichPosition);
//                        dashbordAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                        Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getCatName(String url){
        AndroidNetworking
                .get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String createdAt = jsonObject.getString("identity");
                                String name = jsonObject.getString("name");
                                catMap.put(createdAt,name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DashboardActivity.this, "Some data is not there", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void changeTextColor(){
        globlTxt.setTypeface(null, Typeface.NORMAL);
        netwrkTxt.setTypeface(null, Typeface.NORMAL);
        intrstTxt.setTypeface(null, Typeface.NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.main_menu, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                    Intent intent = new Intent(DashboardActivity.this,SearchResultActivity.class);
                    intent.putExtra("search_key",query);
                    startActivity(intent);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_menu:
                startActivity(new Intent(this,ProfileUpActivity.class));
                return true;
            case R.id.global_menu:
                // Do whatever you want to do on logout click.
                return true;
            case R.id.settings_menu:
                startActivity(new Intent(this,SettingsActivity.class));
                return true;
            case R.id.help_guide:
                Intent intent = new Intent(this, AboutUsActivity.class);
                intent.putExtra("url_is",BASE_URL+"tutorial");
                intent.putExtra("title_is","Help & Guide");
                startActivity(intent);
                return true;
            case R.id.logout_menu:
                alertDialogForLogout(this);
                return true;
            case R.id.badge:
                Intent intentNo = new Intent(this,NotificationActivity.class);
                startActivity(intentNo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void alertDialogForLogout(Context context){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Logout");
        builder1.setMessage("Do you want to logout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //deleteAppData();
                        //deleteCache(DashboardActivity.this);
                        LoginManager.getInstance().logOut();
                        ConstantMethods.setStringPreference("login_status","logout",context);
                        finish();
                        startActivity(getIntent());
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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void getBannerData(){
        AndroidNetworking
                .get(GET_BANNER_DATA)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject bannerObject = response.getJSONObject(i);
                                JSONObject postObj = bannerObject.getJSONObject("post");
                                JSONArray mediaList = postObj.getJSONArray("mediaList");
                                JSONObject imgObj = mediaList.getJSONObject(0);
                                String urlImg = imgObj.getString("url");
                                imgList.add(urlImg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        slider = findViewById(R.id.banner_slider);
//                        slider.setAdapter(new MainSliderAdapter(imgList));
//                        slider.setInterval(3000);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        } else {
            // code for lollipop and pre-lollipop devices
        }

    }

    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return true;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("in fragment on request", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("in fragment on request", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera and Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


}

