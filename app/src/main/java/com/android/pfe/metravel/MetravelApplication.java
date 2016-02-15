package com.android.pfe.metravel;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.FacebookSdk;

/**
 * Created by hpthai7 on 15/02/16.
 */
public class MetravelApplication extends Application {

    private final static String TAG = MetravelApplication.class.getSimpleName();
    private final static boolean DBG = false;

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e(TAG, "UncaughtException --> will restart app!", ex);
                restartApp();
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    /**
     * Restart application, e.g when configuration change.
     */
    private void restartApp() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(
                getPackageName());
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alm.set(AlarmManager.RTC, System.currentTimeMillis() + 500, pi);

        System.exit(2);
    }
}
