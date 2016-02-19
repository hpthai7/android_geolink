package com.android.pfe.metravel.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Utils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void log(String tag, String message) {
        Log.d(tag, message);
    }

    public static boolean isFacebookLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public static void logoutOfFacebook() {
        if (isFacebookLoggedIn()) {
            LoginManager.getInstance().logOut();
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static void saveFacebookInformation(Context context, Bundle bundle) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_FB_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.FB_ID, bundle.getString(Constants.FB_ID));
        editor.putString(Constants.FB_AVA, bundle.getString(Constants.FB_AVA));
        editor.putString(Constants.FB_FNAME, bundle.getString(Constants.FB_FNAME));
        editor.putString(Constants.FB_LNAME, bundle.getString(Constants.FB_LNAME));
        editor.putString(Constants.FB_EMAIL, bundle.getString(Constants.FB_EMAIL));
        editor.putString(Constants.FB_GENDER, bundle.getString(Constants.FB_GENDER));
        editor.putString(Constants.FB_DOB, bundle.getString(Constants.FB_DOB));
        editor.putString(Constants.FB_LOCATION, bundle.getString(Constants.FB_LOCATION));
        editor.apply();
    }
    
    public static String getFacebookInformation(Context context, String column) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_FB_INFO, Context.MODE_PRIVATE);
        return prefs.getString(column, null);
    }

    public static void clearFacebookInformation(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_FB_INFO, Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

}
