package com.wz.tnews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//import cn.bmob.push.PushConstants;

public class MyPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
//        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
//            Log.d("bmob", "客户端收到推送内容：" + intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
//        }
    }
}