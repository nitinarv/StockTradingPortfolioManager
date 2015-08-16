package app.stockticker.com.stockticker.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.stockticker.com.stockticker.R;
import app.stockticker.com.stockticker.gson.Stock;

public final class Tools {

    public static boolean isNetworkOnline(Context context) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo i = connectivityManager.getActiveNetworkInfo();
            if (i == null)
                return false;
            if (!i.isConnected())
                return false;
            if (!i.isAvailable())
                return false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
