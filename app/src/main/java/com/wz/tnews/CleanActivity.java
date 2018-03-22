package com.wz.tnews;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.wz.tnews.db.NewsDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by v_wangzhan on 2017/10/12.
 */
public class CleanActivity extends BaseActivity {
    private static final String TAG = "CleanActivity";

    private CheckBox checkBox0;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private CheckBox checkBox6;

    private List<CheckBox> checkLists = new ArrayList<>();

    @Override
    public String getToolTitle() {
        return "清除缓存";
    }

    @NonNull
    @Override
    public View childView() {
        checkLists.clear();
        View view = LayoutInflater.from(this).inflate(R.layout.activity_clean, null);
        checkBox0 = (CheckBox) view.findViewById(R.id.clean_cb0);
        checkBox1 = (CheckBox) view.findViewById(R.id.clean_cb1);
        checkBox2 = (CheckBox) view.findViewById(R.id.clean_cb2);
        checkBox3 = (CheckBox) view.findViewById(R.id.clean_cb3);
        checkBox4 = (CheckBox) view.findViewById(R.id.clean_cb4);
        checkBox5 = (CheckBox) view.findViewById(R.id.clean_cb5);
        checkBox6 = (CheckBox) view.findViewById(R.id.clean_cb6);
        checkLists.add(checkBox0);
        checkLists.add(checkBox1);
        checkLists.add(checkBox2);
        checkLists.add(checkBox3);
        checkLists.add(checkBox4);
        checkLists.add(checkBox5);
        checkLists.add(checkBox6);
        view.findViewById(R.id.clean_btn).setOnClickListener(this);
        return view;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clean_btn) {
            List<CheckBox> lists = new ArrayList<>();
            selectItemHasChecked(lists);
            if (lists.size() == 0) {
                Toast.makeText(this, "请选择要删除的栏目", Toast.LENGTH_SHORT).show();
            } else {
                deleteCache(lists);
            }
        } else {
            super.onClick(v);
        }
    }

    private void deleteCache(List<CheckBox> lists) {
        for (int i = 0; i < lists.size(); i++) {
            String text = lists.get(i).getText().toString();
            String tableName = BaseApplication.keyValues.get(text);
            Log.i(TAG, "deleteCache: ..." + tableName);
            NewsDao.clearTableNews(tableName, null);
        }
    }

    private void selectItemHasChecked(List<CheckBox> lists) {
        for (int i = 0; i < 7; i++) {
            if (checkLists.get(i).isChecked()) {
                lists.add(checkLists.get(i));
            }
        }
        Log.i(TAG, "selectItemHasChecked: .." + lists.size());
    }
}
