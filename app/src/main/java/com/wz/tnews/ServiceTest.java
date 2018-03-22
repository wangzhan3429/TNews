package com.wz.tnews;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * @author wangzhan
 * @version 2018-01-10
 */

public class ServiceTest extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myAidlInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    class MyBinder extends Binder{

        public void sayHi(){
            System.out.println("service say hi");
        }
    }


IMyAidlInterface.Stub myAidlInterface = new IMyAidlInterface.Stub() {


    @Override
    public void sayTest(String text) throws RemoteException {
        System.out.println(text);
    }
};

}
