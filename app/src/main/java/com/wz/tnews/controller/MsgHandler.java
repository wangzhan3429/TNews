/*
package com.wz.tnews.controller;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.wz.tnews.adapter.ItemClickAdapter;
import com.wz.tnews.bean.News;
import com.wz.tnews.fragment.PagerChildFragment;

import java.util.List;

*/
/**
 * Created by v_wangzhan on 2017/10/13.
 *//*


public class SearchHandler extends Handler {
    public static final int MSG_LOAD_CACHE_SUCCESS = 0;
    public static final int MSG_LOAD_CACHE_FAIL = 1;
    public static final int MSG_LOAD_SQL_SUCCESS = 2;
    public static final int MSG_LOAD_SQL_FAIL = 3;
    public static final int MSG_LOAD_NET_SUCCESS = 4;
    public static final int MSG_LOAD_NET_FAIL = 5;
    public static final int MSG_PULL_UP_SUCCESS = 6;
    public static final int MSG_PULL_UP_FAIL = 7;
    public static final int MSG_PULL_DOWN_SUCCESS = 8;
    public static final int MSG_PULL_DOWN_FAIL = 9;


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.i("wwwwwww", "handleMessage: ..." + msg.what);
        switch (msg.what) {
            case MSG_LOAD_CACHE_SUCCESS: // 加载缓存成功
                PagerChildFragment fragment = reference.get();
                List<News> list = (List<News>) msg.obj;
//                        fragment.newsList.clear();
                fragment.newsList.addAll(list);
                Log.i(fragment.TAG, "handleMessage:... " + list.size() + "..." + fragment
                        .newsList.size());
                fragment.mAdapter.notifyDataSetChanged();
                fragment.refreshLayout.setRefreshing(false);
                fragment.checkIfAutoRefresh(fragment.mFrom);
                break;
            case MSG_LOAD_CACHE_FAIL: // 加载缓存失败
                PagerChildFragment fragment = reference.get();
                fragment.controller.getDataFromSQL(fragment.getStringName(fragment.mFrom));
                break;
            case MSG_LOAD_SQL_SUCCESS:
                PagerChildFragment fragment = reference.get();
                List<News> list = (List<News>) msg.obj;
                fragment.newsList.addAll(list);
                Log.i(fragment.TAG, "handleMessage:... " + list.size() + "..." + fragment
                        .newsList.size());
                fragment.mAdapter.notifyDataSetChanged();
                fragment.refreshLayout.setRefreshing(false);
                fragment.autoRefresh();
                break;
            case MSG_LOAD_SQL_FAIL:
                PagerChildFragment fragment = reference.get();
                fragment.autoRefresh();
                break;
            case MSG_LOAD_NET_SUCCESS:
                List<News> list = (List<News>) msg.obj;
                fragment.newsList.clear();
                fragment.newsList.addAll(list);
                Log.i(fragment.TAG, "handleMessage:... " + list.size() + "..." + fragment
                        .newsList.size());
                fragment.mAdapter.notifyDataSetChanged();
                fragment.refreshLayout.setRefreshing(false);
                break;
            case MSG_LOAD_NET_FAIL:
                PagerChildFragment fragment = reference.get();
                fragment.refreshLayout.setRefreshing(false);
                Toast.makeText(fragment.getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
                break;
            case MSG_PULL_UP_SUCCESS: // 上拉加载
                List<News> list = (List<News>) msg.obj;
                if (list != null && list.size() > 0) {
                    fragment.newsList.addAll(list);
                    Log.i(fragment.TAG, "handleMessage:... " + list.size() + "..." + fragment
                            .newsList.size());
                    ((ItemClickAdapter) fragment.mRecy.getAdapter()).setLoadState
                            (ItemClickAdapter.STATE_LOAD_SUCCESS);
                    fragment.mAdapter.notifyDataSetChanged();
                } else {
                    ((ItemClickAdapter) fragment.mRecy.getAdapter()).setLoadState
                            (ItemClickAdapter.STATE_LOAD_NONE);
                    fragment.mAdapter.notifyDataSetChanged();
                }
                break;
            case MSG_PULL_UP_FAIL:
                PagerChildFragment fragment = reference.get();
                ((ItemClickAdapter) fragment.mRecy.getAdapter()).setLoadState
                        (ItemClickAdapter.STATE_LOAD_FAIL);
                fragment.mAdapter.notifyDataSetChanged();
                break;
            case MSG_PULL_DOWN_SUCCESS: // 下拉加载

                break;
            case MSG_PULL_DOWN_FAIL:

                break;
        }
    }
}
*/
