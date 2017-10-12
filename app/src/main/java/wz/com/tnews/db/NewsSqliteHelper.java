package wz.com.tnews.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wz.com.tnews.BaseApplication;

/**
 * NewsSqliteHelper
 * @author John Kenrinus Lee
 * @version 2017-09-12*/


public class NewsSqliteHelper extends SQLiteHelper {
    private static final String LOG_TAG = "NewsSqliteHelper";
    public static final NewsSqliteHelper singleInstance = new NewsSqliteHelper();

    private static final String NAME = "news.db";
    private static final int VERSION = 1;

    private NewsSqliteHelper() {
        super(BaseApplication.sApp, NAME, null, VERSION);
        Log.i("NewsSqliteHelper", "NewsSqliteHelper: ....");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w(LOG_TAG, "NewsSqliteHelper onCreate() called");
        db.execSQL(NewsTableMetaData.getCreateChosenSqlString());
        db.execSQL(NewsTableMetaData.getCreateAndroidSqlString());
        db.execSQL(NewsTableMetaData.getCreateIOSSqlString());
        db.execSQL(NewsTableMetaData.getCreateFuLiSqlString());
        db.execSQL(NewsTableMetaData.getCreateAppSqlString());
        db.execSQL(NewsTableMetaData.getCreateWebSqlString());
        db.execSQL(NewsTableMetaData.getCreateMoreSqlString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "NewsSqliteHelper onUpgrade() called with destroy all old data from "
                + oldVersion + " to " + newVersion);
//        db.execSQL(NewsTableMetaData.getDropSqlString());
        onCreate(db);
    }

    public static final class NewsTableMetaData implements BaseColumns {
        public static final String TABLE_NAME_CHOSEN = "recommand";
        public static final String TABLE_NAME_FULI = "beauty";
        public static final String TABLE_NAME_ANDROID = "android";
        public static final String TABLE_NAME_IOS = "ios";
        public static final String TABLE_NAME_WEB = "web";
        public static final String TABLE_NAME_APP = "app";
        public static final String TABLE_NAME_MORE = "more";
        // Additional Columns start here.
        public static final String TYPE = "type";
        public static final String UPDATE_TIME = "time";
        public static final String DESC="desc";
        public static final String AUTHOR ="my";
        public static final String URL = "url";
        public static final String NEWS_ID = "news_id";


        private NewsTableMetaData() {
        }

        public static String getCreateChosenSqlString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME_CHOSEN).append(" (");
            sb.append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            sb.append(NEWS_ID).append(" TEXT ,");
            sb.append(TYPE).append(" INTEGER ,");
            sb.append(URL).append(" TEXT, ");
            sb.append(AUTHOR).append(" TEXT, ");
            sb.append(DESC).append(" TEXT, ");
            sb.append(UPDATE_TIME).append(" TIMESTAMP DEFAULT (strftime('%Y-%m-%d %H:%M:%f','now','localtime')) ");
            sb.append(");");
            return sb.toString();
        }

        public static String getDropSqlString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("DROP TABLE IF EXISTS ").append(TABLE_NAME_CHOSEN);
            return sb.toString();
        }

        public static Map<String, String> getDefaultProjectionMap() {
            HashMap<String, String> projectionMap = new HashMap<>();
            projectionMap.put(_ID, _ID);
            projectionMap.put(UPDATE_TIME, UPDATE_TIME);
            return projectionMap;
        }

        public static String[] getDefaultProjection() {
            ArrayList<String> projection = new ArrayList<>();
            projection.add(_ID);
            projection.add(UPDATE_TIME);
            return projection.toArray(new String[projection.size()]);
        }

        public static String getCreateAndroidSqlString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME_ANDROID).append(" (");
            sb.append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            sb.append(NEWS_ID).append(" TEXT ,");
            sb.append(TYPE).append(" INTEGER ,");
            sb.append(URL).append(" TEXT, ");
            sb.append(AUTHOR).append(" TEXT, ");
            sb.append(DESC).append(" TEXT, ");
            sb.append(UPDATE_TIME).append(" TIMESTAMP DEFAULT (strftime('%Y-%m-%d %H:%M:%f','now','localtime')) ");
            sb.append(");");
            return sb.toString();
        }

        public static String getCreateIOSSqlString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME_IOS).append(" (");
            sb.append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            sb.append(NEWS_ID).append(" TEXT ,");
            sb.append(TYPE).append(" INTEGER ,");
            sb.append(URL).append(" TEXT, ");
            sb.append(AUTHOR).append(" TEXT, ");
            sb.append(DESC).append(" TEXT, ");
            sb.append(UPDATE_TIME).append(" TIMESTAMP DEFAULT (strftime('%Y-%m-%d %H:%M:%f','now','localtime')) ");
            sb.append(");");
            return sb.toString();
        }

        public static String getCreateFuLiSqlString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME_FULI).append(" (");
            sb.append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            sb.append(NEWS_ID).append(" TEXT ,");
            sb.append(TYPE).append(" INTEGER ,");
            sb.append(URL).append(" TEXT, ");
            sb.append(AUTHOR).append(" TEXT, ");
            sb.append(DESC).append(" TEXT, ");
            sb.append(UPDATE_TIME).append(" TIMESTAMP DEFAULT (strftime('%Y-%m-%d %H:%M:%f','now','localtime')) ");
            sb.append(");");
            return sb.toString();
        }

        public static String getCreateAppSqlString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME_APP).append(" (");
            sb.append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            sb.append(NEWS_ID).append(" TEXT ,");
            sb.append(TYPE).append(" INTEGER ,");
            sb.append(URL).append(" TEXT, ");
            sb.append(AUTHOR).append(" TEXT, ");
            sb.append(DESC).append(" TEXT, ");
            sb.append(UPDATE_TIME).append(" TIMESTAMP DEFAULT (strftime('%Y-%m-%d %H:%M:%f','now','localtime')) ");
            sb.append(");");
            Log.i("getCreateAppSqlString", "getCreateAppSqlString: ");
            return sb.toString();
        }

        public static String getCreateWebSqlString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME_WEB).append(" (");
            sb.append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            sb.append(NEWS_ID).append(" TEXT ,");
            sb.append(TYPE).append(" INTEGER ,");
            sb.append(URL).append(" TEXT, ");
            sb.append(AUTHOR).append(" TEXT, ");
            sb.append(DESC).append(" TEXT, ");
            sb.append(UPDATE_TIME).append(" TIMESTAMP DEFAULT (strftime('%Y-%m-%d %H:%M:%f','now','localtime')) ");
            sb.append(");");
            return sb.toString();
        }

        public static String getCreateMoreSqlString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME_MORE).append(" (");
            sb.append(_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            sb.append(NEWS_ID).append(" TEXT ,");
            sb.append(TYPE).append(" INTEGER ,");
            sb.append(URL).append(" TEXT, ");
            sb.append(AUTHOR).append(" TEXT, ");
            sb.append(DESC).append(" TEXT, ");
            sb.append(UPDATE_TIME).append(" TIMESTAMP DEFAULT (strftime('%Y-%m-%d %H:%M:%f','now','localtime')) ");
            sb.append(");");
            return sb.toString();
        }
    }
}
