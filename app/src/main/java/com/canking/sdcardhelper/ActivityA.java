package com.canking.sdcardhelper;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.File;

/**
 * Created by cx on 16-9-7.
 * Email: changxing@baidu.com
 * Project: SdCardHelper
 */
public class ActivityA extends BaseActivity {
    String filename = Environment.getExternalStorageDirectory() + "/testa";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_layout);
    }

    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File a = DxFileHelper.getFileInstance(ActivityA.this, filename);
                    boolean res = false;
                    if (!DxFileHelper.exist(ActivityA.this, a)) {
                        res = DxFileHelper.createNewFile(ActivityA.this, a);
                    }
                    afterFile(res);
                } catch (Exception e) {

                }

            }
        }).start();
//        File a = DxFileHelper.getFileInstance(this, filename);
//        boolean res = false;
//        if (!DxFileHelper.exist(this, a)) {
//            res = DxFileHelper.createNewFile(this, a);
//        }
//
//        afterFile(res);
    }

    private void afterFile(boolean res) {
        Log.e("changxing", "onClick" + res);
        //update UI
    }
}
