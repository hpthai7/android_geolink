package com.android.pfe.metravel.common;

import android.content.Context;
import android.os.Bundle;
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

    public static boolean isFacebookLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public static void logoutOfFacebook() {
        if (isFacebookLoggedIn()) {
            LoginManager.getInstance().logOut();
        }
    }

}
