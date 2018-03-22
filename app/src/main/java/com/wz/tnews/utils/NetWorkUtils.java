package com.wz.tnews.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wz.tnews.BaseApplication;

/**
 * Created by v_wangzhan on 2017/9/25.
 */

public class NetWorkUtils {
    private static ConnectivityManager manager;

    static {
        manager = (ConnectivityManager) BaseApplication.sApp.getSystemService(Context
                .CONNECTIVITY_SERVICE);
    }

    public static boolean isNetConnected() {
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
}
