package com.wz.tnews.controller;

import static com.wz.tnews.fragment.PagerChildFragment.FROM_ANDROID;
import static com.wz.tnews.fragment.PagerChildFragment.FROM_APP;
import static com.wz.tnews.fragment.PagerChildFragment.FROM_FULI;
import static com.wz.tnews.fragment.PagerChildFragment.FROM_IOS;
import static com.wz.tnews.fragment.PagerChildFragment.FROM_RECOMMAND;
import static com.wz.tnews.fragment.PagerChildFragment.FROM_RESOURCE;
import static com.wz.tnews.fragment.PagerChildFragment.FROM_WEB;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.wz.tnews.BaseApplication;
import com.wz.tnews.bean.News;
import com.wz.tnews.db.NewsDao;
import com.wz.tnews.fragment.PagerChildFragment;
import com.wz.tnews.net.MessageHandler;
import com.wz.tnews.net.SimpleTaskExecutor;

import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by v_wangzhan on 2017/9/12.
 */

public class PagerChildController {
    private PagerChildFragment.MsgHandler mHandler;
    private int mFrom;
    private final String TAG = "PagerChildController";
    private String BaseUrl = "http://gank.io/api/random/data";
    // http://gank.io/api/random/data/Android/20

    private ConcurrentHashMap<String, List<News>> hashMap = new ConcurrentHashMap();
    private static final ConcurrentHashMap<Integer, Boolean> autoRefreshMap = new
            ConcurrentHashMap<>();

    public PagerChildController(PagerChildFragment.MsgHandler handler, int mFrom) {
        mHandler = handler;
        this.mFrom = mFrom;
        Log.i(TAG, "PagerChildController: ////" + autoRefreshMap);
    }

    public void getDataFromNet(int from) {
        fetchNet(30);
    }

    public void getDataFromCache(final String type) {
        if (hashMap.get(type) != null) {
            List<News> newsArrayList = hashMap.get(type);
            mHandler.sendMessage(mHandler.obtainMessage(mHandler
                    .MSG_LOAD_CACHE_SUCCESS, newsArrayList));
        } else {
            mHandler.sendMessage(mHandler.obtainMessage(mHandler
                    .MSG_LOAD_CACHE_FAIL));
        }
    }

    public void getDataFromSQL(final String type) {
        String tableName = BaseApplication.keyValues.get(type);
        NewsDao.queryHistoryData(tableName, 0, 20, new NewsDao.OnCompletedListener() {
            @Override
            public void onCompleted(Object event) {
                Log.i(TAG, "onCompleted: /...///" + event);
                if (event instanceof List) {
                    mHandler.sendMessage(mHandler.obtainMessage(
                            mHandler.MSG_LOAD_SQL_SUCCESS, event));
                    hashMap.put(type, (List<News>) event); // 缓存进内存中
                } else {
                    mHandler.sendEmptyMessage(
                            mHandler.MSG_LOAD_SQL_FAIL);
                }
            }
        });
    }

    public void pullDownToRefresh() {

    }

    public void alterNews(String from, News news) {
        String tableName = BaseApplication.keyValues.get(from);
        NewsDao.alterNews(tableName, news, null);
    }


    public void pullUpToLoadMore(String from, long timestamp) {
        String tableName = BaseApplication.keyValues.get(from);
        NewsDao.queryHistoryData(tableName, timestamp, 20, new NewsDao.OnCompletedListener() {
            @Override
            public void onCompleted(Object event) {
                Log.i(TAG, "onCompleted: ...pullUpToLoadMore..." + event);
                if (event instanceof List) {
                    List<News> list = (List) event;
                    if (list.size() > 0) {
                        mHandler.sendMessage(mHandler.obtainMessage(mHandler.MSG_PULL_UP_SUCCESS,
                                event));
                        Log.i(TAG, "onCompleted: ....loist//" + list.size());
                        setLastTimestamp(list.get(list.size() - 1).timestemp);
                    } else {
                        mHandler.sendMessage(mHandler.obtainMessage(mHandler.MSG_PULL_UP_SUCCESS));
                    }
                } else {
                    mHandler.sendMessage(mHandler.obtainMessage(mHandler.MSG_PULL_UP_FAIL));
                }
            }
        });

    }

    private String timestemp = System.currentTimeMillis() + "";

    private void setLastTimestamp(String timestemp) {
        this.timestemp = timestemp;
        Log.i("wangzhanzhan", "setLastTimestamp: ..." + timestemp);
    }

    public String getLastTimestamp() {
        return timestemp;
    }

    public boolean isHashAutoRefresh(int from) {
        return autoRefreshMap.get(from) != null ? autoRefreshMap.get(from) : false;
    }

    public void setHashAutoRefresh(int from) {
        autoRefreshMap.put(from, true);
    }

    public void fetchNet(int count) {
        switch (mFrom) {
            case FROM_FULI:
                getDataWithParams("福利", count);
                break;
            case FROM_ANDROID:
                getDataWithParams("Android", count);
                break;
            case FROM_IOS:
                getDataWithParams("iOS", count);
                break;
            case FROM_WEB:
                getDataWithParams("前端", count);
                break;
            case FROM_RESOURCE:
                getDataWithParams("拓展资源", count);
                break;
            case FROM_APP:
                getDataWithParams("App", count);
                break;
            case FROM_RECOMMAND:
                getDataWithParams("瞎推荐", count);
                break;
        }
    }

    private void getDataWithParams(final String type, final int count) {
        SimpleTaskExecutor.scheduleNow(new SimpleTaskExecutor.Task() {
            @Override
            public String getName() {
                return type;
            }

            @Override
            public void run() {
                try {  // http://gank.io/api/random/data/Android/20
                    String url = BaseUrl + "/" + type + "/" + count;
                    Log.i(TAG, "run: ..." + url);
                    Request request = new Request.Builder().url(url).build();
                    OkHttpClient httpClient = BaseApplication.initOkhttp();
                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        setLastTimestamp(System.currentTimeMillis() + "");
                        String msg = response.body().string();
                        List<News> list = MessageHandler.defaultSession.onServerMessage(msg);
                        hashMap.put(type, list); // 缓存进内存中
                        String table = BaseApplication.keyValues.get(type);
                        mHandler.sendMessage(mHandler.obtainMessage(mHandler.MSG_LOAD_NET_SUCCESS, list));
                        Log.i(TAG, "getDataWithParams: .." + list.size() + ".." + table);
                        writeToDataBase(table, list); // 缓存进数据库中
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(mHandler.MSG_LOAD_NET_FAIL);
                }
            }
        });
    }

    private void writeToDataBase(String table, List<News> list) {
        News news = null;
        for (int i = 0; i < list.size(); i++) {
            news = list.get(i);
            Log.i(TAG, "writeToDataBase: ...news  before..." + news);
            news = news.updateTimeStemp(System.currentTimeMillis() + "");
            Log.i(TAG, "writeToDataBase: ...news. after .." + news.timestemp);
            NewsDao.insetIntoDatabase(table, news, new NewsDao.OnCompletedListener() {
                @Override
                public void onCompleted(Object event) {
                    Log.i(TAG, "onCompleted: ...." + event);
                }
            });
        }
    }

}
