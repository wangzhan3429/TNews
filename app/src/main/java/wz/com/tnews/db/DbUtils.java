/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package wz.com.tnews.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * A database utils
 *
 * @author John Kenrinus Lee
 * @version 2014-06-01
 */
public final class DbUtils {
    private static final String TAG = "DbUtils";

    public static final String getString(@NonNull final Cursor pCursor, @NonNull final String pColumnName) {
        return pCursor.getString(pCursor.getColumnIndexOrThrow(pColumnName));
    }

    public static final long getLong(@NonNull final Cursor pCursor, @NonNull final String pColumnName) {
        return pCursor.getLong(pCursor.getColumnIndexOrThrow(pColumnName));
    }

    public static final int getInt(@NonNull final Cursor pCursor, @NonNull final String pColumnName) {
        return pCursor.getInt(pCursor.getColumnIndexOrThrow(pColumnName));
    }

    public static final byte[] getBlob(@NonNull final Cursor pCursor, @NonNull final String pColumnName) {
        return pCursor.getBlob(pCursor.getColumnIndexOrThrow(pColumnName));
    }

    public static final boolean getBoolean(@NonNull final Cursor pCursor, @NonNull final String pColumnName) {
        return getInt(pCursor, pColumnName) == 1 ? true : false;
    }

    public static final long getTimestampMillis(@NonNull final Cursor pCursor, @NonNull final String pColumnName) {
        final String time = getString(pCursor, pColumnName);
//        if (StringUtils.isNotBlank(time)) {
//            try {
//                long millis = DateTime.parse(time, DateTimeFormat
//                        .forPattern("yyyy-MM-dd HH:mm:ss.SSS")).getMillis();
//                if (millis <= 0) {
//                    return System.currentTimeMillis();
//                } else {
//                    return millis;
//                }
//            } catch (Throwable e) {
//                return System.currentTimeMillis();
//            }
//        } else {
//            return System.currentTimeMillis();
//        }
        return 0;
    }

    public static final String preParseSql(String sql, String... args) {
        String[] segments = sql.split("#\\{\\w+?\\}");
        StringBuilder stringBuilder = new StringBuilder();
        assert segments.length == args.length + 1;
        for (int i = 0; i < args.length; i++) {
            stringBuilder.append(segments[i]).append(args[i]);
        }
        return stringBuilder.append(segments[segments.length - 1]).toString();
    }

    public static final <T> T query(@NonNull final Cursor pCursor,
                                    final T pDefaultValue,
                                    @NonNull final DqlFunction<T> pFunction) {
        try {
            return pFunction.doQuery(pCursor);
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return pDefaultValue;
        } finally {
            if (pCursor != null && !pCursor.isClosed()) {
                pCursor.close();
            }
        }
    }

    public static final <T> T exec(@NonNull final SQLiteDatabase db,
                                   final T pDefaultValue,
                                   @NonNull final DmlFunction<T> pFunction) {
        try {
            Log.i("wangzhan", "exec: ...before transaction..");
            db.beginTransaction();
            T result = pFunction.doExec(db);
            Log.i("wangzhan", "exec: ...after transaction..");
            db.setTransactionSuccessful();
            Log.i("wangzhan", "exec: ...end transaction..");
            return result;
        } catch (Exception e) {
            Log.e("wangzhan", "wangzhan....", e);
            return pDefaultValue;
        } finally {
            if (db != null && db.inTransaction()) {
                db.endTransaction();
            }
        }
    }

    public interface DqlFunction<T> {
        public T doQuery(Cursor pCursor);
    }

    public interface DmlFunction<T> {
        public T doExec(SQLiteDatabase db);
    }

}