package com.wz.tnews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

/**
 * Created by v_wangzhan on 2017/10/12.
 */
public class CollectActivity extends BaseActivity {

    private Spinner spinner;

    @Override
    public String getToolTitle() {
        return "收藏";
    }

    @NonNull
    @Override
    public View childView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_collect, null);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
//        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "description");
        RecyclerView mRecycle = (RecyclerView) view.findViewById(R.id.collect_recy);

        return view;
    }
}
