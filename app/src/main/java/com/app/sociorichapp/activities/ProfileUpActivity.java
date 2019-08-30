package com.app.sociorichapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.app.sociorichapp.adapters.DeleteIntrestAdapter;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;;import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.CircleImageView;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.app_utils.OnAboutDataReceivedListener;
import com.app.sociorichapp.fragments.AboutFragment;
import com.app.sociorichapp.fragments.GalleryFragment;
import com.app.sociorichapp.fragments.NetworkFragment;
import com.app.sociorichapp.fragments.PostFragment;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.app.sociorichapp.app_utils.AppApis.BANNER_DELETE;
import static com.app.sociorichapp.app_utils.AppApis.BANNER_EDIT;
import static com.app.sociorichapp.app_utils.AppApis.GET_PROFILE_PICK;
import static com.app.sociorichapp.app_utils.AppApis.MY_INTEREST;
import static com.app.sociorichapp.app_utils.AppApis.UPLOAD_PROFILE_PICK;
import static com.app.sociorichapp.app_utils.CommonVariables.POST_CATEGORY_PROFILE_KEYS;
import static com.app.sociorichapp.app_utils.CommonVariables.POST_CATEGORY_PROFILE_VALUES;

public class ProfileUpActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private CircleImageView profileImg;
    public String profileDes;
    private TextView intrextTxt, intrestAllTxt, soCrTxt, eqCrTxt, qualityTxt;

    private static final int PICK_IMAGE_CAMERA = 1;
    private static final int PICK_IMAGE_GALLERY = 2;

    private boolean appBarExpanded = true;
    String userToken;
    private OnAboutDataReceivedListener mAboutDataListener;
    Map<String, String> interestMap = new HashMap<>();
    private ImageView showAllTxt;
    private ImageView deleteBnrImg,edtBnrImg,bannerImg;
    private String clickImageView = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_up);
        toolbar = findViewById(R.id.anim_toolbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        appBarLayout = findViewById(R.id.appbar);
        intrextTxt = findViewById(R.id.intrest_txt);
        soCrTxt = findViewById(R.id.so_cr_txt);
        eqCrTxt = findViewById(R.id.eq_cr_txt);
        qualityTxt = findViewById(R.id.quality_txt);
        showAllTxt = findViewById(R.id.showall_img);
        intrestAllTxt = findViewById(R.id.intrest_all);
        deleteBnrImg = findViewById(R.id.dlt_bnr_img);
        edtBnrImg = findViewById(R.id.edt_bnr_img);
        bannerImg = findViewById(R.id.header);
        userToken = ConstantMethods.getStringPreference("user_token", this);
        for (int i = 0; i < POST_CATEGORY_PROFILE_KEYS.length; i++) {
            interestMap.put(POST_CATEGORY_PROFILE_KEYS[i], POST_CATEGORY_PROFILE_VALUES[i]);
        }
        deleteBnrImg.setOnClickListener(db->deleteBannerImage());
        edtBnrImg.setOnClickListener(db-> {
            selectImage();
            clickImageView = "banner";
        });
        qualityTxt.setOnClickListener(q -> {
            Intent intent = new Intent(this, QualityActivity.class);
            startActivity(intent);
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar.setTitle(" ");
        getProfileData();
        profileImg = findViewById(R.id.profile_img);
        profileImg.setOnClickListener(v -> {
            selectImage();
            clickImageView = "profile";
        });
        intrextTxt.setOnClickListener(v -> selectIntrest());
        showAllTxt.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeleteIntrestActivity.class);
            startActivity(intent);
        });

        List<String> catVals = ConstantMethods.getArrayListShared(ProfileUpActivity.this, "interest_save");
        if(catVals==null) {
            Toast.makeText(this, "Please select any intrest", Toast.LENGTH_SHORT).show();
        }
        String allIntrestStr = "";
        if (catVals == null) {
            catVals = new ArrayList<>();
        }
        for (int i = 0; i < catVals.size(); i++) {
            allIntrestStr = allIntrestStr + catVals.get(i) + ", ";
            intrestAllTxt.setText(allIntrestStr);
        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.landscape);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {
                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                collapsingToolbar.setContentScrimColor(vibrantColor);
                collapsingToolbar.setStatusBarScrimColor(R.color.black_trans80);
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                } else {
                    appBarExpanded = true;
                }
                invalidateOptionsMenu();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> catVals = ConstantMethods.getArrayListShared(ProfileUpActivity.this, "interest_save");
        if (catVals == null) {
            catVals = new ArrayList<>();
        }
        String allIntrestStr = "";
        for (int i = 0; i < catVals.size(); i++) {
            allIntrestStr = allIntrestStr + catVals.get(i) + ", ";
            intrestAllTxt.setText(allIntrestStr);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        if (item.getTitle() == "Add") {
            Toast.makeText(this, "Add menu clicked!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AboutFragment();
                case 1:
                    return new PostFragment();
                case 2:
                    return new GalleryFragment();
                case 3:
                    return new NetworkFragment();
                default:
                    return new AboutFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "About";
                case 1:
                    return "Post";
                case 2:
                    return "Gallery";
                case 3:
                    return "Network";
                default:
                    return "Second Tab";
            }
        }
    }

    List<String> catVals = new ArrayList<>();

    private void selectIntrest() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("In which areas you are intersted?");

        boolean[] checkedItems = {false, false, false, false, false, false, false, false, false, false};
        builder.setMultiChoiceItems(POST_CATEGORY_PROFILE_KEYS, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    String valuech = POST_CATEGORY_PROFILE_KEYS[which];
                    catVals.add(valuech);
                    HashSet<String> hashSet = new HashSet<>();
                    hashSet.addAll(catVals);
                    catVals.clear();
                    catVals.addAll(hashSet);
                } else {
                    String valuech = POST_CATEGORY_PROFILE_KEYS[which];
                    catVals.remove(valuech);
                }
                ConstantMethods.saveArrayListShared(catVals, ProfileUpActivity.this, "interest_save");
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> catVals = ConstantMethods.getArrayListShared(ProfileUpActivity.this, "interest_save");
                if (catVals == null) {
                    catVals = new ArrayList<>();
                }
                String allIntrestStr = "";
                for (int i = 0; i < catVals.size(); i++) {
                    allIntrestStr = allIntrestStr + catVals.get(i) + ", ";
                    intrestAllTxt.setText(allIntrestStr);
                }
                List<String> sendKeys = new ArrayList<>();
                for (int j = 0; j < catVals.size(); j++) {
                    String firstVal = interestMap.get(catVals.get(j));
                    sendKeys.add(firstVal);
                }
                setInterestValues(sendKeys);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteIntrest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Intrest");

        boolean[] checkedItems = {false, false, false, false, false, false, false, false, false, false};
        builder.setMultiChoiceItems(POST_CATEGORY_PROFILE_KEYS, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    String valuech = POST_CATEGORY_PROFILE_KEYS[which];
                    catVals.add(valuech);
                    HashSet<String> hashSet = new HashSet<>();
                    hashSet.addAll(catVals);
                    catVals.clear();
                    catVals.addAll(hashSet);
                } else {
                    String valuech = POST_CATEGORY_PROFILE_KEYS[which];
                    catVals.remove(valuech);
                }
                ConstantMethods.saveArrayListShared(catVals, ProfileUpActivity.this, "interest_save");
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> catVals = ConstantMethods.getArrayListShared(ProfileUpActivity.this, "interest_save");
                if (catVals == null) {
                    catVals = new ArrayList<>();
                }
                String allIntrestStr = "";
                for (int i = 0; i < catVals.size(); i++) {
                    allIntrestStr = allIntrestStr + catVals.get(i) + ", ";
                    intrestAllTxt.setText(allIntrestStr);
                }
                List<String> sendKeys = new ArrayList<>();
                for (int j = 0; j < catVals.size(); j++) {
                    String firstVal = interestMap.get(catVals.get(j));
                    sendKeys.add(firstVal);
                }
                setInterestValues(sendKeys);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void updateProfilePicture(File file) {
        String usertoken = ConstantMethods.getStringPreference("user_token", this);
        AndroidNetworking
                .upload(UPLOAD_PROFILE_PICK)
                .addMultipartFile("file", file)
                .addMultipartParameter("fileSize", String.valueOf(file.getTotalSpace()))
                .addHeaders("authorization", "Bearer " + usertoken)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.e("progress", "" + bytesUploaded);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("progress", "" + response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("progress", "" + error);
                    }
                });
    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String filePath = Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name);
                File destination = new File(filePath, "IMG_" + timeStamp + ".jpg");
                File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String imgPath = destination.getAbsolutePath();
                if(clickImageView.equals("profile")) {
                    profileImg.setImageBitmap(bitmap);
                    File file = new File(imgPath);
                    updateProfilePicture(file);
                }
                else if(clickImageView.equals("banner")){
                    bannerImg.setImageBitmap(bitmap);
                    File file = new File(imgPath);
                    editBannerImage(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                String imgPath = getRealPathFromURI(selectedImage);
                File destination = new File(imgPath);
                if(clickImageView.equals("profile")) {
                    profileImg.setImageBitmap(bitmap);
                    updateProfilePicture(destination);
                }
                else if(clickImageView.equals("banner")){
                    bannerImg.setImageBitmap(bitmap);
                    editBannerImage(destination);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void getProfileData() {
        userToken = ConstantMethods.getStringPreference("user_token", this);
        AndroidNetworking
                .get(GET_PROFILE_PICK)
                .addHeaders("authorization", "Bearer " + userToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("test", "" + response);
                        try {
                            profileDes = response.getString("profileDesc");
                            String dob = response.getString("dob");
                            String wrkPlace = response.getString("workplace");
                            JSONObject locationObj = null;
                            String location = "";
                            try {
                                locationObj = response.getJSONObject("location");
                                location = locationObj.getString("desc");
                            } catch (Exception e) {
                                e.getStackTrace();
                            }
                            JSONObject bannerObj = null;
                            String bannerStr = "";
                            try {
                                bannerObj = response.getJSONObject("coverPhoto");
                                bannerStr = bannerObj.getString("url");
                                Glide.with(ProfileUpActivity.this)
                                        .load(bannerStr)
                                        .placeholder(R.drawable.user_profile)
                                        .error(R.drawable.user_profile)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .priority(com.bumptech.glide.Priority.HIGH)
                                        .into(bannerImg);
                            } catch (Exception e) {
                                e.getStackTrace();
                            }
                            mAboutDataListener.onDataReceived(profileDes, wrkPlace, location, dob);
                            JSONObject profileObj = response.getJSONObject("profilePic");
                            String profileUrl = profileObj.getString("url");
                            Glide.with(ProfileUpActivity.this)
                                    .load(profileUrl)
                                    .placeholder(R.drawable.user_profile)
                                    .error(R.drawable.user_profile)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .priority(com.bumptech.glide.Priority.HIGH)

                                    .into(profileImg);
                            String socioMoneyBalance = response.getString("socioMoneyBalance");
                            String equaMoneyBalance = response.getString("equaMoneyBalance");
                            socioMoneyBalance = socioMoneyBalance.substring(0, socioMoneyBalance.length() - 2);
                            equaMoneyBalance = equaMoneyBalance.substring(0, equaMoneyBalance.length() - 2);
                            soCrTxt.setText(socioMoneyBalance);
                            eqCrTxt.setText(equaMoneyBalance);
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("test", "" + anError);
                    }
                });
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }

    private void setInterestValues(List<String> selectedValues) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < selectedValues.size(); i++) {
            try {
                jsonArray.put(i, selectedValues.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            jsonObject.put("interestCategories", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String userToken = ConstantMethods.getStringPreference("user_token", this);
        AndroidNetworking
                .post(MY_INTEREST)
                .addHeaders("authorization", "Bearer " + userToken)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("test", "" + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("test", "" + anError);
                    }
                });
    }

    private void editBannerImage(File file){
        String usertoken = ConstantMethods.getStringPreference("user_token", this);
        AndroidNetworking
                .upload(BANNER_EDIT)
                .addMultipartFile("file", file)
                .addMultipartParameter("fileSize", String.valueOf(file.getTotalSpace()))
                .addHeaders("authorization", "Bearer " + usertoken)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.e("progress", "" + bytesUploaded);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("progress", "" + response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("progress", "" + error);
                    }
                });
    }

    private void deleteBannerImage(){
        ConstantMethods.showProgressbar(this);
        String token = ConstantMethods.getStringPreference("user_token", this);
        AndroidNetworking
                .delete(BANNER_DELETE)
                .addHeaders("authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        try {
                            String result = response.getString("result");
                            if(result.equals("success")){
                                Toast.makeText(ProfileUpActivity.this, "Cover page deleted", Toast.LENGTH_SHORT).show();
                                bannerImg.setImageResource(R.drawable.banner);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                        Toast.makeText(ProfileUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
