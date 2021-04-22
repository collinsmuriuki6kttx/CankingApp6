package com.canking.sdcardhelper;
/*
    Copyright 2015 ChangXing kingh.cha@gmail.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by changxing on 15-11-4.
 */
public class PermissionUtils {
    private static final String TAG = "PermissionUtils";
    public static final int PERMISSION_REQUEST_CODE = 0x10;
    public static final int PERMISSION_SETTING_REQ_CODE = 0x1000;

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(Activity cxt, String permission, int requestCode) {
        Log.d(TAG, "checkPermission");
        requestPermissionsWrapper(cxt, requestCode, permission);

        if (!checkSelfPermission(cxt, permission)) {//Context is ok

            requestPermissionsWrapper(cxt, requestCode, permission);
            Log.d(TAG, "requestPermissionsWrapper");

//            if (!shouldShowRequestPermissionRationaleWrapper(cxt, permission)) { //Activity
//                requestPermissionsWrapper(cxt, new String[]{permission}, requestCode);
//            } else {
//                Log.d(TAG, "should show rational");
//            }
            return false;
        }

        return true;
    }


    public static void requestPermissionsWrapper(Activity cxt, int requestCode, String... permission) {
        ActivityCompat.requestPermissions(cxt, permission, requestCode);
    }


    private static boolean shouldShowRequestPermissionRationaleWrapper(Object cxt, String permission) {
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            return ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission);
        } else if (cxt instanceof Fragment) {
            Fragment fragment = (Fragment) cxt;
            return fragment.shouldShowRequestPermissionRationale(permission);
        } else {
            throw new RuntimeException("cxt is net a activity or fragment");
        }
    }

    @TargetApi(23)
    public static boolean checkSelfPermission(Activity cxt, String permission) {
        return ActivityCompat.checkSelfPermission(cxt,
                permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static String[] checkSelfPermissionArray(Activity cxt, String[] permission) {
        ArrayList<String> permiList = new ArrayList<>();
        for (String p : permission) {
            if (!checkSelfPermission(cxt, p)) {
                permiList.add(p);
            }
        }

        return permiList.toArray(new String[permiList.size()]);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermissionArray(Activity cxt, String[] permission, int requestCode) {
        String[] permissionNo = checkSelfPermissionArray(cxt, permission);
        if (permissionNo.length > 0) {
            requestPermissionsWrapper(cxt, requestCode, permissionNo);
            return false;
        } else return true;
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

    /**
     * 检测系统弹出权限
     *
     * @param cxt
     * @param req
     * @return
     */
    @TargetApi(23)
    public static <C extends Context> boolean checkSettingAlertPermission(Activity cxt, int req,C t) {
        Activity activity = (Activity) cxt;
        if (!Settings.canDrawOverlays(activity.getBaseContext())) {
            Log.i(TAG, "Setting not permission");

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, req);
            return false;
        }


        return true;
    }

}
