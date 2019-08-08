package com.app.sociorichapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.CircleImageView;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.fragments.AboutFragment;
import com.app.sociorichapp.fragments.GalleryFragment;
import com.app.sociorichapp.fragments.NetworkFragment;
import com.app.sociorichapp.fragments.PostFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.app.sociorichapp.app_utils.CommonVariables.POST_CATEGORY_PROFILE_KEYS;

public class ProfileActivity extends BaseActivity {
    private LinearLayout intrestLay;
    private CircleImageView profileImg;
    private static final int RESULT_LOAD_IMAGE = 100;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intrestLay = findViewById(R.id.intrest_view);
        intrestLay.setOnClickListener(v->selectIntrest());
        ConstantMethods.setTitleAndBack(this,"Profile");
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.pager);
        profileImg = findViewById(R.id.profile_img);
        profileImg.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });
        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile;
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
    List<String> catVals;
    private void selectIntrest(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose some animals");

        boolean[] checkedItems = {false, false, false, false, false, false, false, false};
        builder.setMultiChoiceItems(POST_CATEGORY_PROFILE_KEYS, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                catVals = new ArrayList<>();
                if(isChecked){
                    String valuech = POST_CATEGORY_PROFILE_KEYS[which];
                    catVals.add(valuech);
                }
                else{
                    String valuech = POST_CATEGORY_PROFILE_KEYS[which];
                    catVals.remove(valuech);
                }
                Log.e("finalV",catVals.toString());
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profileImg.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage(){

    }
}
