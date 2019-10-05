package com.sociorich.app.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;
//import com.sociorich.app.testpac.ServiceTest;
import com.sociorich.app.testpac.service;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
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
    }
}
