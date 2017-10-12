package wz.com.tnews;

import android.app.Application;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import wz.com.tnews.db.SQLiteWorker;

import static wz.com.tnews.sql.DataOpenHelper.*;

/**
 * Created by v_wangzhan on 2017/9/1.
 */

public class BaseApplication extends Application {
    public OkHttpClient client;
    public static Application sApp;
    public static final HashMap<String,String> keyValues = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        client = new OkHttpClient();
        SQLiteWorker.getSharedInstance().start();
//        initDataBase();
        initKeyValues();
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
//       builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor
//               .Level.BODY));
        builder.connectTimeout(5000, TimeUnit.MILLISECONDS);
        return builder.build();
    }


}
