package com.wz.tnews;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author wangzhan
 * @version 2018-01-10
 */

public class ServiceTestActivity extends Activity {
    String  TAG = "ServiceTestActivity";

    private IMyAidlInterface myAidlInterface;

    ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "onServiceConnected: ");
           /* ((ServiceTest.MyBinder)iBinder).sayHi();
            myAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
            try {
                myAidlInterface.sayTest("service has connected");
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*Intent intent = new Intent(this,ServiceTest.class);
        bindService(intent,mConn,BIND_AUTO_CREATE);*/

    }
}
