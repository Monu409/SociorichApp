package com.app.sociorichapp.application;

import android.app.Application;
import android.content.Intent;
import android.os.StrictMode;

public class DefaultApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}