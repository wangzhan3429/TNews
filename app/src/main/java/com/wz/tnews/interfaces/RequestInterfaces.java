package com.wz.tnews.interfaces;

import com.wz.tnews.RespData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @author wangzhan
 * @version 2018-03-19
 */

public interface RequestInterfaces {
    // base  https://interface.meiriyiwen.com/article
    // https://interface.meiriyiwen.com/article/day?dev=1&date=20170216  按日期
    // https://interface.meiriyiwen.com/article/random?dev=1  随机一篇
    // https://interface.meiriyiwen.com/article/today?dev=1  每日

    @Headers("User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6)")
    @GET("day")
    Call<RespData> getArticleByDay(@Query("dev") String dev,@Query("date") String date);

    @Headers("User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6)")
    @GET("random")
    Call<RespData> getArticleRandom(@Query("dev") int dev);

    @Headers("User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6)")
    @GET("today")
    Call<RespData> getArticalToday(@Query("dev") String dev);
}
