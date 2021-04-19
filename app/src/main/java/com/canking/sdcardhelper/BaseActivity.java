package com.canking.sdcardhelper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by cx on 16-9-7.
 * Email: changxing@baidu.com
 * Project: SdCardHelper
 */
public class BaseActivity extends Activity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DxFileHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("changxing", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("changxing", "onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("changxing", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("changxing", "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("changxing", "onRestart");
    }
}
