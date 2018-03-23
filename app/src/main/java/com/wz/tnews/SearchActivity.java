package com.wz.tnews;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.wz.tnews.adapter.ItemClickAdapter;
import com.wz.tnews.adapter.NormalAdapter;
import com.wz.tnews.bean.News;
import com.wz.tnews.interfaces.OnItemClickListener;
import com.wz.tnews.net.MessageHandler;
import com.wz.tnews.net.SimpleTaskExecutor;
import com.wz.tnews.utils.NetWorkUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by v_wangzhan on 2017/10/12.
 */
public class SearchActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, OnItemClickListener {
    private static final String TAG = "SearchActivity";

    private final String BASE_URL = "http://gank.io/api/search/query/"; // listview/category/Android/count/10/page/1
    private EditText mEdit;
    private Spinner spinner;
    private String searchType = "Android";
    private List<News> newsLists = new ArrayList<>();
    private NormalAdapter searchAdapter;
    private RecyclerView mRecycle;
    private SearchHandler searchHandler;

    private static class SearchHandler extends Handler {
        private WeakReference<SearchActivity> reference;
        public static final int TYPE_SUCCESS = 0;
        public static final int TYPE_FAIL = 1;

        public SearchHandler(SearchActivity activity) {
            reference = new WeakReference<SearchActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (reference != null && reference.get() != null) {
                SearchActivity activity = reference.get();
                switch (msg.what) {
                    case TYPE_SUCCESS:
                        activity.newsLists.clear();
                        activity.newsLists.addAll((Collection<? extends News>) msg.obj);
                        Log.i(TAG, "run: ....." + activity.newsLists.size());
                        (activity.searchAdapter).setLoadState
                                (ItemClickAdapter.STATE_LOAD_NONE);
                        activity.searchAdapter.notifyDataSetChanged();
                        break;
                    case TYPE_FAIL:
                        activity.newsLists.clear();
                        (activity.searchAdapter).setLoadState
                                (ItemClickAdapter.STATE_LOAD_FAIL);
                        activity.searchAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }

    @Override
    public String getToolTitle() {
        return "搜索";
    }

    @NonNull
    @Override
    public View childView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_search, null);
        spinner = (Spinner) view.findViewById(R.id.collect_spinner);
        mRecycle = (RecyclerView) view.findViewById(R.id.search_recy);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(manager);
        searchAdapter = new NormalAdapter(this, newsLists);
        mRecycle.setAdapter(searchAdapter);
        searchAdapter.setOnItemClickListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);
        mEdit = (EditText) view.findViewById(R.id.edt_search);
        mEdit.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mEdit.getText().toString() == null) {
                        Toast.makeText(SearchActivity.this, "请输入搜索信息", Toast.LENGTH_SHORT).show();
                        mEdit.requestFocus();
                    } else {
                        if (NetWorkUtils.isNetConnected()) {
                            hideSoftKeyBoard();
                            searchNews(mEdit.getText().toString());
                        } else {
                            Toast.makeText(SearchActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return false;
            }
        });

        searchHandler = new SearchHandler(this);
        return view;
    }

    private void hideSoftKeyBoard() {
        // 先隐藏键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(SearchActivity.this
                                .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void searchNews(final String words) {
        SimpleTaskExecutor.scheduleNow(new SimpleTaskExecutor.Task() {
            @Override
            public String getName() {
                return "searchNews";
            }

            @Override
            public void run() {
                try {
                    String url = BASE_URL + words + "/category/" + searchType + "/count/50/page/1";
                    Log.i(TAG, "run: ////" + url);
                    Request request = new Request.Builder().url(url).build();
                    OkHttpClient httpClient = BaseApplication.initOkhttp();
                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String msg = response.body().string();
                        List<News> lists = MessageHandler.defaultSession.onServerMessage(msg);
                        searchHandler.sendMessage(searchHandler.obtainMessage(searchHandler
                                .TYPE_SUCCESS, lists));
                    } else {
                        searchHandler.sendEmptyMessage(searchHandler.TYPE_FAIL);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    searchHandler.sendEmptyMessage(searchHandler.TYPE_FAIL);
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner.setSelection(position);
        searchType = (String) spinner.getSelectedItem();
        Log.i(TAG, "onItemSelected:... " + searchType);

//        HashSet<Integer> set = new HashSet<>();
////        set.add()
//        String s = "wwwaaa";
//        s.replace('w', 'c');

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onItemClick(int position, View view) {
        openUrl(this, newsLists.get(position).url);
    }

    /**
     * Open URL using system browser
     *
     * @param context
     * @param url
     *
     */
    public static void openUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}