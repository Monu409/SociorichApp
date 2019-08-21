package com.app.sociorichapp.application;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.app.sociorichapp.R;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

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
    }
}