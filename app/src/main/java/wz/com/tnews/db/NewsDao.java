package wz.com.tnews.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static wz.com.tnews.db.DbUtils.getInt;
import static wz.com.tnews.db.DbUtils.getString;
import static wz.com.tnews.sql.DataOpenHelper.NewsTableMetaData;


import wz.com.tnews.bean.News;
import wz.com.tnews.sql.DataOpenHelper;


/**
 * News Data Access Object
 *
 * @author John Kenrinus Lee
 * @version 2017-09-14
 */
public class NewsDao {
    private static final String LOG_TAG = "NewsDao";

    private NewsDao() {
    }

    public interface OnCompletedListener {
        void onCompleted(Object event);
    }

    public static void queryHistoryData(final String tableName, final long lastTimestemp, final int size,
                                        final OnCompletedListener listener) {
        SQLiteWorker.getSharedInstance().postDQL(
                new SQLiteWorker.AbstractSQLable() {
                    // work thread
                    @Override
                    public Object doAysncSQL() {
                        String sql = "select * from " + tableName;
                        if (lastTimestemp > 0) {
                            sql += " where " + NewsTableMetaData.TIMESTEMP + "<" + lastTimestemp;
                        }
                        sql += " order by " + NewsTableMetaData.TIMESTEMP + " desc limit " + size;
                        Log.i("wangzhanzhan", "doAysncSQL:... " + sql);
                        final SQLiteDatabase db = DataOpenHelper.singleInstance.getReadableDatabase();
                        return DbUtils.query(db.rawQuery(sql, null), null, new DbUtils
                                .DqlFunction<List<News>>() {
                            @Override
                            public List<News> doQuery(Cursor cursor) {
                                final List<News> list = new ArrayList<>();
                                Log.i("wangzhanzhan", "doQuery: ..." + cursor.getCount());
                                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                                    int type = getInt(cursor, NewsTableMetaData.TYPE);
                                    String desc = getString(cursor, NewsTableMetaData.DESC);
                                    String time = getString(cursor, NewsTableMetaData
                                            .UPDATE_TIME);
                                    String url = getString(cursor, NewsTableMetaData.URL);
                                    String author = getString(cursor, NewsTableMetaData.AUTHOR);
                                    String timestamp = getString(cursor, NewsTableMetaData
                                            .TIMESTEMP);
                                    list.add(new News(desc, url, author, time, timestamp));
                                    Log.i("wangzhanzhan", "doQuery: ..." + timestamp);
                                }
                                return list;
                            }
                        });
                    }

                    // main thread
                    @Override
                    public void onCompleted(Object event) {
                        if (listener != null) {
                            listener.onCompleted(event);
                        }
                    }
                }

        );
    }

    public static void deleteNews(final String tableName, final News News, final OnCompletedListener
            listener) {
        SQLiteWorker.getSharedInstance().postDML(
                new SQLiteWorker.AbstractSQLable() {
                    // work thread
                    @Override
                    public Object doAysncSQL() {
                        final SQLiteDatabase db = DataOpenHelper.singleInstance.getWritableDatabase();
                        return DbUtils.exec(db, null, new DbUtils.DmlFunction<Void>() {
                                    @Override
                                    public Void doExec(SQLiteDatabase db) {
                                        db.execSQL("delete from " + tableName + " where "
                                                + NewsTableMetaData._ID + "=" + News.id);
                                        return null;
                                    }
                                }
                        );
                    }

                    // main thread
                    @Override
                    public void onCompleted(Object event) {
                        if (listener != null) {
                            listener.onCompleted(event);
                        }
                    }
                }
        );
    }

    public static void insetIntoDatabase(final String tableName, final News News, final OnCompletedListener listener) {
        SQLiteWorker.getSharedInstance().postDML(
                new SQLiteWorker.AbstractSQLable() {
                    // work thread
                    @Override
                    public Object doAysncSQL() {
                        Log.i("wangzhan", "doAysncSQL: .before..");
                        final SQLiteDatabase db = DataOpenHelper.singleInstance.getWritableDatabase();
                        return DbUtils.exec(db, null, new DbUtils.DmlFunction<News>() {
                            @Override
                            public News doExec(SQLiteDatabase db) {
                                String sql = "insert or replace into " + tableName
                                        + "(" + NewsTableMetaData.TYPE + "," +
                                        NewsTableMetaData.DESC + "," + NewsTableMetaData.AUTHOR + ","
                                        + "" + NewsTableMetaData.URL + "," + NewsTableMetaData
                                        .NEWS_ID + "," + NewsTableMetaData.UPDATE_TIME + "," +
                                        NewsTableMetaData.TIMESTEMP + ")"
                                        + " values(?,?,?,?,?,?,?)";
                                db.execSQL(sql,
                                        new Object[]{News.type, News.desc,
                                                News.author, News.url,
                                                News.id, News.time, News.timestemp
                                        });
                                News result = null;
//                                cursor.moveToFirst();
//                                if (!cursor.isAfterLast()) {
//                                    result = News.withIdAndTimestamp(DbUtils.getLong(cursor, NewsTableMetaData._ID),
//                                            DbUtils.getTimestampMillis(cursor, NewsTableMetaData.UPDATE_TIME));
//                                }
//                                cursor.close();
                                return result;
                            }
                        });
                    }

                    // main thread
                    @Override
                    public void onCompleted(Object event) {
                        if (listener != null) {
                            listener.onCompleted(tableName);
                        }
                    }
                }
        );
    }
}
