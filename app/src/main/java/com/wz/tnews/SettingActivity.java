package com.wz.tnews;

import org.polaric.colorful.ColorPickerDialog;
import org.polaric.colorful.Colorful;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by v_wangzhan on 2017/10/12.
 */
public class SettingActivity extends BaseActivity {
    private final String TAG = "SettingActivity";

    @Override
    public String getToolTitle() {
        return "设置";
    }

    @Override
    public View childView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_setting, null);

        // 意见反馈
        view.findViewById(R.id.setting_fadeback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, CommentActivity.class);
                startActivity(intent);
            }
        });

        // 更换主题
        view.findViewById(R.id.setting_changeTitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog dialog = new ColorPickerDialog(SettingActivity.this);
                dialog.setOnColorSelectedListener(new ColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(Colorful.ThemeColor color) {
                        //TODO: Do something with the color
                        Log.i(TAG, "onColorSelected: ..." + color.getColorRes());
                        Colorful.config(SettingActivity.this)
                                .primaryColor(color)
                                .translucent(false)
                                .dark(false)
                                .apply();
                        restartApp();
                    }
                });
                dialog.show();
            }
        });

        // 赏个好评
        view.findViewById(R.id.setting_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 推荐好友
        view.findViewById(R.id.setting_recommend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    /**
     * 重新启动App -> 不杀进程,缓存的东西不清除,启动快
     */
    public void restartApp() {
        final Intent intent = getPackageManager()
                .getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
