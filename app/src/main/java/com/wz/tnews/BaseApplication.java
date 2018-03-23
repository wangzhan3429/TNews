package com.wz.tnews;

import static com.wz.tnews.sql.DataOpenHelper.NewsTableMetaData;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.polaric.colorful.Colorful;
import org.polaric.colorful.ThemeDelegate;

import com.baidu.crabsdk.CrabSDK;
import com.tencent.tauth.Tencent;
import com.wz.tnews.db.SQLiteWorker;
import com.wz.tnews.utils.CrashHandler;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import cn.bmob.v3.Bmob;
import okhttp3.OkHttpClient;

/**
 * Created by v_wangzhan on 2017/9/1.
 */

public class BaseApplication extends Application {
    public OkHttpClient client;
    public static BaseApplication sApp;
    public static final HashMap<String, String> keyValues = new HashMap<>();
    public static Tencent mTencent;
    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        client = new OkHttpClient();
        SQLiteWorker.getSharedInstance().start();
        CrabSDK.init(this, "16250b50d9a8928b");
        initKeyValues();
        initQQ();
        initBmob();
        initCrashHandler();
        Colorful.init(this);
        ThemeDelegate delegate = Colorful.getThemeDelegate();
        if (delegate.getPrimaryColor() == null || delegate.getAccentColor() == null) {
            Colorful.defaults()
                    .primaryColor(Colorful.ThemeColor.GREEN)
                    .translucent(true)
                    .dark(false);
        } else {
            Colorful.defaults()
                    .primaryColor(Colorful.getThemeDelegate().getPrimaryColor())
                    .translucent(true)
                    .dark(false);
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
        }
        return preferences;
    }

    private void initQQ() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance("1106489860", this);
        }
    }

    private void initBmob() {
        //第一：默认初始化
        Bmob.initialize(this, "da589021476b7f2d47d75edf19561267");
    }

    private void initKeyValues() {
        keyValues.put("福利", NewsTableMetaData.TABLE_NAME_FULI);
        keyValues.put("Android", NewsTableMetaData.TABLE_NAME_ANDROID);
        keyValues.put("iOS", NewsTableMetaData.TABLE_NAME_IOS);
        keyValues.put("前端", NewsTableMetaData.TABLE_NAME_WEB);
        keyValues.put("拓展资源", NewsTableMetaData.TABLE_NAME_MORE);
        keyValues.put("App", NewsTableMetaData.TABLE_NAME_APP);
        keyValues.put("瞎推荐", NewsTableMetaData.TABLE_NAME_CHOSEN);
    }

    //    public synchronized static SQLiteDatabase getDataBase() {
    //        if (database == null) {
    //            database = singleInstance.getWritableDatabase();
    //        }
    //        return database;
    //    }

    public static OkHttpClient initOkhttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
               builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor
                       .Level.BODY));
        builder.connectTimeout(5000, TimeUnit.MILLISECONDS);
        return builder.build();
    }


    public void initCrashHandler(){
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(this);
    }

}
