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
package com.wz.tnews.db;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * All sql should be executed by this singleton class, all database operate on foreground just post, and you can handle others,
 * it's work asynchronous, on sql complete, it will call you on main thread.
 * you need call start() method to init work, when stop() called, the remain task will discard.
 *
 * @author John Kenrinus Lee
 * @version 2014-06-01
 */
public final class SQLiteWorker {
    private static final String TAG = "SQLiteWorker";

    private Handler mainHandler;
    private BlockingQueue<SQLable> dmlQueue;
    private DMLThread dmlThread;
    private ExecutorService dqlService;
    private boolean running;

    private static final class SQLiteWorkerHolder {
        static final SQLiteWorker instance = new SQLiteWorker();
    }

    public static final SQLiteWorker getSharedInstance() {
        return SQLiteWorkerHolder.instance;
    }

    private SQLiteWorker() {
    }

    /**
     * before use service, you should call this to init
     */
    public synchronized SQLiteWorker start() {
        if (running) {
            return this;
        }
        running = true;
        mainHandler = new Handler(Looper.getMainLooper());
        dmlQueue = new LinkedBlockingQueue<>();
        dmlThread = new DMLThread();
        dqlService = Executors.newCachedThreadPool();
        return this;
    }

    /**
     * when you won't use the service any more, you should call this to destroy service
     */
    public synchronized SQLiteWorker stop() {
        running = false;
        dqlService.shutdown();
        dqlService = null;
        dmlQueue = null;
        dmlThread = null;
        return this;
    }

    /**
     * post a DML task
     */
    public synchronized void postDML(SQLable sql) {
        if (checkState()) {
            dmlQueue.offer(sql);
            if (!dmlThread.isAlive()) {
                dmlThread.start();
            }
        }
    }

    /**
     * post a DQL task
     */
    public synchronized void postDQL(SQLable sql) {
        if (checkState()) {
            dqlService.execute(new ExecuteDQL(sql));
        }
    }

    private synchronized boolean checkState() {
        if (!running) {
            Log.e(TAG, "illegel state: please call start() method before call this");
            return false;
        }
        return true;
    }

    // for database multi-read, no catch exception
    private final class ExecuteDQL implements Runnable {
        SQLable sql;

        public ExecuteDQL(SQLable sql) {
            this.sql = sql;
        }

        @Override
        public void run() {
            try {
                final Object event = sql.call();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sql.onCompleted(event);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // for one database write use a queue on loop, no catch exception
    private final class DMLThread extends Thread {
        @Override
        public void run() {
            while (running) {
                try {
                    Log.i("wangzhan", "run: ..DMLThread...before");
                    final SQLable sql = dmlQueue.take();
                    Log.i("wangzhan", "run: ..DMLThread..."+sql+"//after");
                    if (sql != null) {
                        Log.i("wangzhan", "run: ..DMLThread...before..call()");
                        final Object event = sql.call();
                        Log.i("wangzhan", "run: ..DMLThread...after..call()");
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                sql.onCompleted(event);
                                Log.i(TAG, "run: ..DMLThread...onCompleted()");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.i("wangzhan", "run: ...InterruptedException");
                    e.printStackTrace();
                    break;
                } catch (Exception e) {
                    Log.i("wangzhan", "run: ...Exception");
                    e.printStackTrace();
                } finally {
                    Log.i("wangzhan", "run: .....isrunning..."+running);
                }
            }
        }
    }

    /**
     * describe a sql task, if possible, you should use AbstractSQLable
     */
    public interface SQLable {
        /**
         * you code sql in this method, it's not run on main thread, you can return a Cursor or Model pojo
         */
        public Object call();

        /**
         * run on main thread when call() completed, the param of event is the return value of call()
         */
        public void onCompleted(Object event);
    }

    /**
     * abstract class which implement SQLable, it make default implements with onCompleted(), and allow publish intermediate
     * products
     */
    public abstract static class AbstractSQLable implements SQLable {
        /**
         * the parameter object which push at constructor
         */
        protected Object param;

        public AbstractSQLable() {
        }

        public AbstractSQLable(Object param) {
            this.param = param;
        }

        /**
         * implement the call() method in SQLable
         */
        public abstract Object doAysncSQL();

        /**
         * when the doAysncSQL method throw a exception, event is Throwable, else it is the return value from doAysncSQL
         */
        public void onCompleted(Object event) {
        }

        /**
         * when call publish() in call() method to publish intermediate products, this method will be called on main thread
         */
        public void onPublish(Object event) {
        }

        /**
         * not on main thread, in call(), you can call this to publish intermediate products
         */
        protected final void publish(final Object event) {
            SQLiteWorker.getSharedInstance().mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onPublish(event);
                }
            });
        }

        /**
         * you shouldn't override this method, use doAysncSQL instead
         */
        @Deprecated
        public Object call() {
            try {
                Log.i("wangzhan", "call: ..");
                return doAysncSQL();
            } catch (Exception e) {
                e.printStackTrace();
                return e;
            }
        }
    }
}