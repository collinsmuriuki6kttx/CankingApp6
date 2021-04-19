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
public class ActivityB extends BaseActivity {
    String filename = Environment.getExternalStorageDirectory() + "/testb";

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
                    File a = DxFileHelper.getFileInstance(ActivityB.this, filename);
                    boolean res = false;
                    if (!DxFileHelper.exist(ActivityB.this, a)) {
                        res = DxFileHelper.createNewFile(ActivityB.this, a);
                    }
                    afterFile(res);
                } catch (Exception e) {

                }

            }
        }).start();

    }

    private void afterFile(boolean res) {
        Log.e("changxing", "onClick" + res);
        //update UI
    }
}
