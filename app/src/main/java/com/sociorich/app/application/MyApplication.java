package com.sociorich.app.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.util.List;

public class MyApplication extends Application {
 
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();
 
        //finally initialize twitter with created configs
        Twitter.initialize(config);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
//        boolean b = isAppIsInBackground(this);
//        Log.e("Applicaion",""+b);
        AppVisibilityDetector.init(this, new AppVisibilityDetector.AppVisibilityCallback() {
            @Override
            public void onAppGotoForeground() {
                Log.e("Applicaion","onAppGotoForeground");
                String loginStatus = ConstantMethods.getStringPreference("login_status", MyApplication.this);
                if (loginStatus.equals("login")) {
                    String loginType = ConstantMethods.getStringPreference("login_type", MyApplication.this);
                    if (loginType.equals("social")) {
                        String email = ConstantMethods.getStringPreference("email_prif", MyApplication.this);
                        String firstName = ConstantMethods.getStringPreference("first_name", MyApplication.this);
                        ConstantMethods.socialLogin(email, firstName, MyApplication.this);
                    }
                }
            }
            @Override
            public void onAppGotoBackground() {
                Log.e("Applicaion","onAppGotoBackground");
            }
        });
    }
}