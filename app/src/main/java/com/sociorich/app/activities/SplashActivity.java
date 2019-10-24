package com.sociorich.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

import java.net.URLConnection;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    String url1 = "https://s3.ap-south-1.amazonaws.com/srch-dev-media/fe6b9f8f-c36e-4127-a3a3-9bd3f2f42f78/fbf48d5b-7941-415c-8ea9-326fa593e0ff.jpg";
    String url2 = "https://s3.ap-south-1.amazonaws.com/srch-dev-media/fe6b9f8f-c36e-4127-a3a3-9bd3f2f42f78/445852ca-0230-4b8b-91fb-d25aafbba41d.mp4";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        boolean b = isImageFile(url1);
        boolean b2 = isImageFile(url2);
//        startService(new Intent(SplashActivity.this, service.class));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String loginStatus = ConstantMethods.getStringPreference("login_status", this);
//            if(loginStatus.equals("login")) {
//                startForegroundService(new Intent(SplashActivity.this, service.class));
//            }
//        }
//        startService(new Intent(this, ServiceTest.class));
        String loginStatus = ConstantMethods.getStringPreference("login_status", this);
        if (loginStatus.equals("login")) {
            String loginType = ConstantMethods.getStringPreference("login_type", this);
            if (loginType.equals("social")) {
                String email = ConstantMethods.getStringPreference("email_prif", this);
                String firstName = ConstantMethods.getStringPreference("first_name", this);
                ConstantMethods.socialLogin(email, firstName, this);
            }
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,DashboardActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sociorich.com/"));
//                startActivity(browserIntent);
//                finish();
//            }
//        }, SPLASH_DISPLAY_LENGTH);
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }
}
