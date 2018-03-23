package com.wz.tnews.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import com.wz.tnews.BaseApplication;
import com.wz.tnews.UploadService;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Process;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler crashHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        synchronized(CrashHandler.class) {
            if (crashHandler != null) {
                return crashHandler;
            }
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    private Thread.UncaughtExceptionHandler mUncaughtHandler;
    private Context context;

    public void init(Context context) {
        mUncaughtHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            dumpToSDCard(e);// 可以在后面加上传服务器的操作
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        e.printStackTrace();
        if (mUncaughtHandler != null) {
            mUncaughtHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private void dumpToSDCard(Throwable e) {
        String path = Environment.getExternalStorageDirectory().getPath() + File
                .separator + "TNews";
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        String fileName = path + File.separator + "crash_" + formatDateTime(System.currentTimeMillis
                ()) + ".trace";
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            e.printStackTrace(writer);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
            // 上传服务器 TODO
            Intent intent = new Intent(BaseApplication.sApp, UploadService.class);
            intent.putExtra("filePath", fileName);
            BaseApplication.sApp.startService(intent);
        }

    }

    private String formatDateTime(long l) {
        String time = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(l);
        return time;
    }
}