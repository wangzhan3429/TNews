package com.wz.tnews;

import java.io.File;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * @author wangzhan
 * @version 2018-03-23
 */

public class UploadService extends IntentService {

    private String filePath = null;

    public UploadService() {
        super("upload");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File file = new File(filePath);
        final BmobFile bmobFile = new BmobFile(file);
        Log.i("UploadService", "onHandleIntent: ..." + file.getName() + "..." + bmobFile+"。。"+Thread.currentThread().getId());
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                System.out.println("UploadService upload success");
                Toast.makeText(UploadService.this, "crash file upload success!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        filePath = intent.getStringExtra("filePath");
        Log.i("UploadService", "onStartCommand: ..." + filePath+"....."+Thread.currentThread().getId());
        return super.onStartCommand(intent, flags, startId);
    }
}
