package wz.com.tnews.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * A sample
 *
 * @author John Kenrinus Lee
 * @version 2017-09-12
 */
class DbExamples {
    void setUp() {
        SQLiteWorker.getSharedInstance().start();
    }

//    void testDQL() {
//        final String tableName = NewsSqliteHelper.NewsTableMetaData.TABLE_NAME_CHOSEN;
//        doDQL("select * from " + tableName, null, null, new DbUtils.DqlFunction<Message>() {
//            @Override
//            public Message doQuery(Cursor pCursor) {
//                pCursor.moveToFirst();
//                int type = DbUtils.getInt(pCursor, "type");
//                String content = DbUtils.getString(pCursor, "content");
//                return new Message(type, content, 1, System.currentTimeMillis());
//            }
//        }, new OnCompletedListener() {
//            @Override
//            public void onCompleted(Object event) {
//                System.out.println("onCompleted");
//            }
//        });
//    }

    void testDML() {
        doDML(null, new DbUtils.DmlFunction<Void>() {
            @Override
            public Void doExec(SQLiteDatabase db) {
                final String tableName = NewsSqliteHelper.NewsTableMetaData.TABLE_NAME_CHOSEN;
                db.execSQL("insert into " + tableName + " values (0, 'this is test message')");
                return null;
            }
        }, new OnCompletedListener() {
            @Override
            public void onCompleted(Object event) {
                System.out.println("onCompleted");
            }
        });
    }

    void tearDown() {
        SQLiteWorker.getSharedInstance().stop();
    }

    interface OnCompletedListener {
        void onCompleted(Object event);
    }

    static <T> void doDQL(final String sql, final String[] args, final T defaultValue,
                          final DbUtils.DqlFunction<T> function, final OnCompletedListener onCompleted) {
        SQLiteWorker.getSharedInstance().postDQL(new SQLiteWorker.AbstractSQLable() {
            // work thread
            @Override
            public Object doAysncSQL() {
                final SQLiteDatabase db = NewsSqliteHelper.singleInstance.getReadableDatabase();
                return DbUtils.query(db.rawQuery(sql, args), defaultValue, function);
            }

            // main thread
            @Override
            public void onCompleted(Object event) {
                if (onCompleted != null) {
                    onCompleted.onCompleted(event);
                }
            }
        });
    }

    static <T> void doDML(final T defaultValue,
                          final DbUtils.DmlFunction<T> function, final OnCompletedListener onCompleted) {
        SQLiteWorker.getSharedInstance().postDML(new SQLiteWorker.AbstractSQLable() {
            // work thread
            @Override
            public Object doAysncSQL() {
                final SQLiteDatabase db = NewsSqliteHelper.singleInstance.getWritableDatabase();
                return DbUtils.exec(db, defaultValue, function);
            }

            // main thread
            @Override
            public void onCompleted(Object event) {
                if (onCompleted != null) {
                    onCompleted.onCompleted(event);
                }
            }
        });
    }
}
