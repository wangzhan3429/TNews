package com.wz.tnews;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by v_wangzhan on 2017/10/12.
 */
public class AboutActivity extends BaseActivity {
    private static final String TAG = "AboutActivity";
    private TextView mVersion;
    private TextView mRight;

    @Override
    public String getToolTitle() {
        return "关于";
    }

    @NonNull
    @Override
    public View childView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_about, null);
        mVersion = (TextView) view.findViewById(R.id.about_version);
        mRight = (TextView) view.findViewById(R.id.about_right);
        mVersion.setText("v" + getVersionName());
        mRight.setText("Copyright © 2017 wangzhan. \n All Rights Reserved.");
        return view;
    }

    private String getVersionName() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    this.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "v0.1";
    }


}
