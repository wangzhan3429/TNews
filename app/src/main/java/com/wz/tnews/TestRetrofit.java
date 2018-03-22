package com.wz.tnews;

import com.wz.tnews.interfaces.RequestInterfaces;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author wangzhan
 * @version 2018-03-19
 */

public class TestRetrofit extends BaseActivity {
    @Override
    public String getToolTitle() {
        return null;
    }

    @NonNull
    @Override
    public View childView() {
        Button button = new Button(this);
        button.setText("今天天气怎么样？？？");
        Retrofit retrofit = BaseApplication.initRetrofit();
        RequestInterfaces interfaces = retrofit.create(RequestInterfaces.class);
        final Call<RespData> respData = interfaces.getArticleByDay("1","20180103");
        respData.enqueue(new Callback<RespData>() {
            @Override
            public void onResponse(Call<RespData> call, Response<RespData> response) {
                Log.i("respData", "onResponse: ...."+response.isSuccessful());
                if (response.isSuccessful()) {
                    RespData data = (RespData) response.body();
                    Log.i("respData", "onCreate: ==" + data.data.content);
                }
            }

            @Override
            public void onFailure(Call<RespData> call, Throwable t) {

            }
        });
        return button;
    }




}
