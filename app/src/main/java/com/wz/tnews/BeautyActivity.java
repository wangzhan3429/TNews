package com.wz.tnews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
/**
 * Created by v_wangzhan on 2017/10/24.
 */

public class BeautyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty);
        String url = getIntent().getStringExtra("url");
        ImageView mImage = (ImageView) findViewById(R.id.beauty_img);
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImage);
    }
}
