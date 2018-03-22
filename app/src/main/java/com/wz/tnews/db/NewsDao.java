package com.wz.tnews.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.wz.tnews.db.DbUtils.getInt;
import static com.wz.tnews.db.DbUtils.getString;
import static com.wz.tnews.sql.DataOpenHelper.NewsTableMetaData;


import com.wz.tnews.bean.News;
import com.wz.tnews.sql.DataOpenHelper;


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
                                    String id = getString(cursor, NewsTableMetaData.NEWS_ID);
                                    int type = getInt(cursor, NewsTableMetaData.TYPE);
                                    String desc = getString(cursor, NewsTableMetaData.DESC);
                                    String time = getString(cursor, NewsTableMetaData
                                            .UPDATE_TIME);
                                    String url = getString(cursor, NewsTableMetaData.URL);
                                    String author = getString(cursor, NewsTableMetaData.AUTHOR);
                                    String timestamp = getString(cursor, NewsTableMetaData
                                            .TIMESTEMP);
                                    int is_connect = getInt(cursor, NewsTableMetaData.IS_CONNECT);
                                    list.add(new News(id, desc, url, author, time, timestamp,
                                            is_connect));
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

    public static void clearTableNews(final String tableName, final OnCompletedListener listener) {
        SQLiteWorker.getSharedInstance().postDML(
                new SQLiteWorker.AbstractSQLable() {
                    // work thread
                    @Override
                    public Object doAysncSQL() {
                        final SQLiteDatabase db = DataOpenHelper.singleInstance.getWritableDatabase();
                        return DbUtils.exec(db, null, new DbUtils.DmlFunction<Void>() {
                                    @Override
                                    public Void doExec(SQLiteDatabase db) {
                                        db.execSQL("delete from " + tableName);
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

    public static void alterNews(final String tableName, final News news, final OnCompletedListener
            listener) {
        // UPDATE COMPANY SET ADDRESS = 'Texas' WHERE ID = 6;
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
//                                String sql = "UPDATE " + tableName
//                                        + "SET (" + NewsTableMetaData.IS_CONNECT + "," +
//                                        NewsTableMetaData.DESC + "," + NewsTableMetaData.AUTHOR + ","
//                                        + "" + NewsTableMetaData.URL + "," + NewsTableMetaData
//                                        .NEWS_ID + "," + NewsTableMetaData.UPDATE_TIME + "," +
//                                        NewsTableMetaData.TIMESTEMP + ")"
//                                        + " values(?,?,?,?,?,?,?)";
//                                db.execSQL(sql,
//                                        new Object[]{N.type, N.desc,
//                                                N.author, N.url,
//                                                N.id, N.time, N.timestemp
//                                        });
//                                db.execSQL("update " + tableName + " set " + NewsTableMetaData
//                                        .IS_CONNECT + " = " + 1 + " where " + NewsTableMetaData
//                                        .NEWS_ID + " = " + news.id + "");
                                ContentValues values = new ContentValues();
                                values.put(NewsTableMetaData.IS_CONNECT, 1);
                                db.update(tableName, values, NewsTableMetaData.NEWS_ID + "=?", new
                                        String[]{news.id});
                                News result = null;
                                return result;
                            }
                        });
                    }

                    // main thread
                    @Override
                    public void onCompleted(Object event) {
                    }
                }
        );
    }

    public static void insetIntoDatabase(final String tableName, final News news, final OnCompletedListener listener) {
        SQLiteWorker.getSharedInstance().postDML(
                new SQLiteWorker.AbstractSQLable() {
                    // work thread
                    @Override
                    public Object doAysncSQL() {
                        Log.i("wangzhan", "doAysncSQL: .before.." + news.id);
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
                                        new Object[]{news.type, news.desc,
                                                news.author, news.url,
                                                news.id, news.time, news.timestemp
                                        });
                                News result = null;
//                                cursor.moveToFirst();
//                                if (!cursor.isAfterLast()) {
//                                    result = news.withIdAndTimestamp(DbUtils.getLong(cursor, NewsTableMetaData._ID),
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
