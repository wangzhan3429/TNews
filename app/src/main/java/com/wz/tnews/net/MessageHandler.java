package com.wz.tnews.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wz.tnews.bean.News;

import android.util.Log;

/**
 * Created by v_wangzhan on 2017/10/13.
 */

public class MessageHandler {
    private static final String TAG = "MessageHandler";

    public static MessageHandler defaultSession = new MessageHandler();

    public List<News> onServerMessage(String message) {
        List<News> lists = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(message);
            JSONArray jsonArray = jsonObject.optJSONArray("results");
            TypeToken typeToken = new TypeToken<ArrayList<News>>() {
            };
            lists = new Gson().fromJson(jsonArray.toString(), typeToken.getType());
            Log.i(TAG, "parseMsg: ...." + lists.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }


}
