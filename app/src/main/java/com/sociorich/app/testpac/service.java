package com.sociorich.app.testpac;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.sociorich.app.activities.DashboardActivity;
import com.sociorich.app.activities.SplashActivity;
import com.sociorich.app.app_utils.ConstantMethods;

public class service extends Service {
   private static BroadcastReceiver m_ScreenOffReceiver;

   @Override
   public IBinder onBind(Intent arg0)
   {
      return null;
   }

   @Override
   public void onCreate()
   {
      if (Build.VERSION.SDK_INT >= 26) {
         String CHANNEL_ID = "my_channel_01";
         NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                 "Channel human readable title",
                 NotificationManager.IMPORTANCE_DEFAULT);

         ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

         Notification notification = new NotificationCompat.Builder(service.this, CHANNEL_ID)
                 .setContentTitle("")
                 .setContentText("").build();

         startForeground(1, notification);
      }
      registerScreenOffReceiver();
   }

   @Override
   public void onDestroy()
   {
      unregisterReceiver(m_ScreenOffReceiver);
      m_ScreenOffReceiver = null;
   }

   private void registerScreenOffReceiver()
   {
      m_ScreenOffReceiver = new BroadcastReceiver()
      {
         @SuppressLint("WrongConstant")
         @Override
         public void onReceive(Context context, Intent intent) {
            Log.d("tagaaaaaa", "ACTION_SCREEN_OFF");
            String loginStatus = ConstantMethods.getStringPreference("login_status", service.this);
            if (loginStatus.equals("login")) {
               String loginType = ConstantMethods.getStringPreference("login_type", service.this);
               if (loginType.equals("social")) {
                  String email = ConstantMethods.getStringPreference("email_prif", service.this);
                  String firstName = ConstantMethods.getStringPreference("first_name", service.this);
                  ConstantMethods.socialLogin(email, firstName, service.this);
               }
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                  stopForeground(1);
               }
            }
         }
      };
      IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
      registerReceiver(m_ScreenOffReceiver, filter);
   }
}