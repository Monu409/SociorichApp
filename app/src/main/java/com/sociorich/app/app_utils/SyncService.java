package com.sociorich.app.app_utils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class SyncService extends Service {

   private Handler mHandler;
   // default interval for syncing data
    public static final long DEFAULT_SYNC_INTERVAL = 1000;

        // task to be run here
        private Runnable runnableService = new Runnable() {
            @Override
            public void run() {
                syncData();
                // Repeat this runnable code block again every ... min
                mHandler.postDelayed(runnableService, DEFAULT_SYNC_INTERVAL);
            }
        };

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // Create the Handler object
            mHandler = new Handler();
            // Execute a runnable task as soon as possible
            mHandler.post(runnableService);

            return START_STICKY;
        }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private synchronized void syncData() {
        Log.e("test service","test service");
        }
    }