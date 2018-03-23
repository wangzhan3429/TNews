package com.wz.tnews.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wz.tnews.BeautyActivity;
import com.wz.tnews.R;
import com.wz.tnews.WebViewActivity;
import com.wz.tnews.adapter.BeautyAdapter;
import com.wz.tnews.adapter.ItemClickAdapter;
import com.wz.tnews.adapter.NormalAdapter;
import com.wz.tnews.bean.News;
import com.wz.tnews.controller.PagerChildController;
import com.wz.tnews.interfaces.OnItemClickListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class PagerChildFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //    "福利", "Android", "iOS ",  " 拓展资源 ", " 前端"
    private static final String ARG_FROM = "arg_from";
    public static final int FROM_RECOMMAND = 0;
    public static final int FROM_APP = 1;
    public static final int FROM_FULI = 2;
    public static final int FROM_ANDROID = 3;
    public static final int FROM_IOS = 4;
    public static final int FROM_RESOURCE = 5;
    public static final int FROM_WEB = 6;

    private int mFrom;

    private RecyclerView mRecy;
    private RecyclerView.Adapter mAdapter;
    private String TAG = "PagerChildFragment";
    private List<News> newsList = new ArrayList<>();
    private MsgHandler handler;
    private SwipeRefreshLayout refreshLayout;
    private String BaseUrl = "http://gank.io/api/data";
    private SQLiteDatabase mDatabase;
    private PagerChildController controller;

    public static class MsgHandler extends Handler {
        private WeakReference<PagerChildFragment> reference;
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

        public MsgHandler(PagerChildFragment fragment) {
            reference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("wwwwwww", "handleMessage: ..." + msg.what);
            switch (msg.what) {
                case MSG_LOAD_CACHE_SUCCESS: // 加载缓存成功
                    if (reference.get() != null) {
                        PagerChildFragment fragment = reference.get();
                        List<News> list = (List<News>) msg.obj;
                        //                        fragment.newsList.clear();
                        fragment.newsList.addAll(list);
                        Log.i(fragment.TAG, "handleMessage:... " + list.size() + "..." + fragment
                                .newsList.size());
                        fragment.mAdapter.notifyDataSetChanged();
                        fragment.refreshLayout.setRefreshing(false);
                        fragment.checkIfAutoRefresh(fragment.mFrom);
                    }
                    break;
                case MSG_LOAD_CACHE_FAIL: // 加载缓存失败
                    if (reference.get() != null) {
                        PagerChildFragment fragment = reference.get();
                        fragment.controller.getDataFromSQL(fragment.getStringName(fragment.mFrom));
                    }
                    break;
                case MSG_LOAD_SQL_SUCCESS:
                    if (reference.get() != null) {
                        PagerChildFragment fragment = reference.get();
                        List<News> list = (List<News>) msg.obj;
                        fragment.newsList.addAll(list);
                        Log.i(fragment.TAG, "handleMessage:... " + list.size() + "..." + fragment
                                .newsList.size());
                        fragment.mAdapter.notifyDataSetChanged();
                        fragment.refreshLayout.setRefreshing(false);
                        fragment.autoRefresh();
                    }
                    break;
                case MSG_LOAD_SQL_FAIL:
                    if (reference.get() != null) {
                        PagerChildFragment fragment = reference.get();
                        fragment.autoRefresh();
                    }
                    break;
                case MSG_LOAD_NET_SUCCESS:
                    if (reference.get() != null) {
                        PagerChildFragment fragment = reference.get();
                        List<News> list = (List<News>) msg.obj;
                        fragment.newsList.clear();
                        fragment.newsList.addAll(list);
                        Log.i(fragment.TAG, "handleMessage:... " + list.size() + "..." + fragment
                                .newsList.size());
                        fragment.mAdapter.notifyDataSetChanged();
                        fragment.refreshLayout.setRefreshing(false);
                    }
                    break;
                case MSG_LOAD_NET_FAIL:
                    if (reference.get() != null) {
                        PagerChildFragment fragment = reference.get();
                        fragment.refreshLayout.setRefreshing(false);
                        Toast.makeText(fragment.getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MSG_PULL_UP_SUCCESS: // 上拉加载
                    if (reference.get() != null) {
                        PagerChildFragment fragment = reference.get();
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
                    }
                    break;
                case MSG_PULL_UP_FAIL:
                    if (reference.get() != null) {
                        PagerChildFragment fragment = reference.get();
                        ((ItemClickAdapter) fragment.mRecy.getAdapter()).setLoadState
                                (ItemClickAdapter.STATE_LOAD_FAIL);
                        fragment.mAdapter.notifyDataSetChanged();
                    }
                    break;
                case MSG_PULL_DOWN_SUCCESS: // 下拉加载

                    break;
                case MSG_PULL_DOWN_FAIL:

                    break;
            }
        }
    }

    private static HashMap<Integer, PagerChildFragment> pagerFragmentMap = new HashMap<>();

    public static PagerChildFragment newInstance(int from) {
        Bundle args = new Bundle();
        args.putInt(ARG_FROM, from);

        if (pagerFragmentMap.get(from) != null) {
            return pagerFragmentMap.get(from);
        }
        PagerChildFragment fragment = new PagerChildFragment();
        fragment.setArguments(args);
        pagerFragmentMap.put(from, fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new MsgHandler(this);
        Bundle args = getArguments();
        if (args != null) {
            mFrom = args.getInt(ARG_FROM);
        }

        controller = new PagerChildController(handler, mFrom);
        Log.i(TAG, "onCreate: ..." + mFrom + "..." + controller);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecy = (RecyclerView) view.findViewById(R.id.recy);

        mAdapter = getAdapterFromType(mFrom);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecy.setLayoutManager(manager);
        mRecy.setAdapter(mAdapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int mLastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
                Log.i(TAG, "onScrollStateChanged: ...." + mLastPosition + "..." + mAdapter.getItemCount());
                if (mLastPosition + 1 == mAdapter
                        .getItemCount() && (newState == RecyclerView.SCROLL_STATE_IDLE)) {
                    Log.i(TAG, "onScrollStateChanged: ..." + newState);
                    ((ItemClickAdapter) recyclerView.getAdapter()).setLoadState(ItemClickAdapter.STATE_LOADING);
                    startLoadMore();
                }
            }
        });
    }

    private void startLoadMore() {
        long lastTimestamp = 0;
        if (newsList.size() > 0) {
            lastTimestamp = Long.parseLong(controller.getLastTimestamp());
        }
        Log.i("wangzhanzhan", "startLoadMore: .from==" + mFrom + "..." + lastTimestamp);
        controller.pullUpToLoadMore(getStringName(mFrom), lastTimestamp);
    }

    private RecyclerView.Adapter getAdapterFromType(final int mFrom) {
        Log.i(TAG, "getAdapterFromType: ..." + mFrom);
        RecyclerView.Adapter adapter = null;
        switch (mFrom) {
            case PagerChildFragment.FROM_FULI:
                // 美女图
                adapter = new BeautyAdapter(getContext(), newsList);
                ((BeautyAdapter) adapter).setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Log.i(TAG, "onItemClick: ..." + position + "..beauty");
                        Intent intent = new Intent(getActivity(), BeautyActivity.class);
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(), view, getString(R.string.transitions_name));
                        intent.putExtra("url", newsList.get(position).url);
                        startActivity(intent, optionsCompat.toBundle());

                    }
                });
                break;
            case PagerChildFragment.FROM_RECOMMAND:
            case PagerChildFragment.FROM_APP:
            case PagerChildFragment.FROM_ANDROID:
            case PagerChildFragment.FROM_IOS:
            case PagerChildFragment.FROM_WEB:
            case PagerChildFragment.FROM_RESOURCE:
                adapter = new NormalAdapter(getContext(), newsList);
                ((NormalAdapter) adapter).setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Log.i(TAG, "onItemClick: ..." + position + "..resource");
                        openUrlUseDefaultExploer(getActivity(), newsList.get(position).url);
                    }
                });
                // 收藏
                //                ((NormalAdapter) adapter).setOnConnectListener(new NormalAdapter.OnConnectListener() {
                //                    @Override
                //                    public void onConnectClick(int position, View view) {
                //                        controller.alterNews(getStringName(mFrom), newsList.get(position));
                //                        ((ImageView) view).setImageResource(R.drawable.list_open_collected);
                //                    }
                //                });
                break;
        }
        return adapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecy.setAdapter(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData(mFrom);
    }

    private void loadData(int from) {
        /*首先访问内存，如果内存里面有数据，则先取内存里面的数据加载，然后访问网络
        如果内存里面没有数据，则访问数据库，如果数据库有数据则加载数据库里面数据，然后访问网络
        * */
        if (from == 0) {
            // 因为第一个条目进入时候不会走onitemselected，所以第一个条目默认是true
            isItemSelected = true;
        }
        controller.getDataFromCache(getStringName(from));
    }

    private void checkIfAutoRefresh(int from) {
        Log.i(TAG, "onItemSelected: ..." + isItemSelected + "..." + controller.isHashAutoRefresh(from) + ""
                + ".." + from);
        if (isItemSelected && !controller.isHashAutoRefresh(from)) {
            refreshLayout.setRefreshing(true);
            controller.setHashAutoRefresh(from);
            autoRefresh();
        }
    }

    private void autoRefresh() {
        controller.fetchNet(30);
    }

    private String getStringName(int from) {
        String urlName = "瞎推荐";
        switch (from) {
            case FROM_FULI:
                urlName = "福利";
                break;
            case FROM_ANDROID:
                urlName = "Android";
                break;
            case FROM_IOS:
                urlName = "iOS";
                break;
            case FROM_WEB:
                urlName = "前端";
                break;
            case FROM_RESOURCE:
                urlName = "拓展资源";
                break;
            case FROM_APP:
                urlName = "App";
                break;
            case FROM_RECOMMAND:
                urlName = "瞎推荐";
                break;
        }
        return urlName;
    }

    /**
     * Open URL using system browser
     *
     * @param context
     * @param url
     */
    public static void openUrlUseDefaultExploer(Context context, String url) {
        String s = null;
        Log.i("test", "openUrlUseDefaultExploer: .."+s.substring(0,1));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.stay);
    }

    public static void openUrlUseActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.stay);
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh: ...");
        refreshLayout.setRefreshing(true);
        controller.getDataFromNet(mFrom);
    }

    boolean isItemSelected = false;

    public void onItemSelected(int position) {
        isItemSelected = true;
        Log.i(TAG, "onItemSelected: ..." + isItemSelected + "..." + controller + "..." + position
                + ".." + mFrom + "..." + handler);
        if (controller == null) {
            controller = new PagerChildController(handler, mFrom);
        }
        if (isItemSelected && !controller.isHashAutoRefresh(position)) {
            refreshLayout.setRefreshing(true);
            controller.fetchNet(30);
            controller.setHashAutoRefresh(position);
        }
    }
}
