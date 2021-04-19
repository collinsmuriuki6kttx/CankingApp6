package com.canking.sdcardhelper;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

/**
 * Created by cx on 16-9-7.
 * Email: changxing@baidu.com
 * Project: SdCardHelper
 */
public class DemoService extends Service implements Runnable {
    int x;
    String filename = Environment.getExternalStorageDirectory() + "/testb";

    @Override
    public void run() {
        for (; ; ) {
            x++;
            filename = filename + x;

            try {
                File file = DxFileHelper.getFileInstance(null, filename);
                if (!DxFileHelper.exist(null, file)) {
                    DxFileHelper.createNewFile(null, file);
                }


                Log.e("changxing", "thread sleep...");
                Thread.sleep(9000);
            } catch (Exception e) {

            }
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread t = new Thread(this);
        t.start();
        Log.e("changxing", "service start...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("changxing", "onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
