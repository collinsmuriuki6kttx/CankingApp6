package com.canking.sdcardhelper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cx on 16-9-5.
 * Project: SdCardHelper
 */
public class DxFileHelper {

    private static boolean mWaiting, mIsGrant;



    public static File getFileInstance(@Nullable Activity activity, String path) {
        return new File(path);
    }

    public static File getExternalStorageDirectory(Activity activity) {
        return Environment.getExternalStorageDirectory();
    }

    public static String[] getMutiSDDirectory(Activity context) {
        List<String> pathsList = new ArrayList<String>();
        StorageManager storageManager = (StorageManager) context.getApplicationContext().getSystemService(Context.STORAGE_SERVICE);
        try {
            Method method = StorageManager.class.getDeclaredMethod("getVolumePaths");
            method.setAccessible(true);
            Object result = method.invoke(storageManager);
            if (result != null && result instanceof String[]) {
                String[] pathes = (String[]) result;
                StatFs statFs;
                for (String path : pathes) {
                    if (!TextUtils.isEmpty(path) && new File(path).exists()) {
                        statFs = new StatFs(path);
                        if (statFs.getBlockCount() * statFs.getBlockSize() != 0) {
                            pathsList.add(path);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            File externalFolder = Environment.getExternalStorageDirectory();
            if (externalFolder != null) {
                pathsList.add(externalFolder.getAbsolutePath());
            }
        }
        return pathsList.toArray(new String[pathsList.size()]);
    }

    public static boolean delete(Activity activity, File file) {
        boolean res;
        if (PermissionUtils.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            res = file.delete();
        } else {
            res = false;
        }

        return res;
    }

    public static boolean mkdirs(Activity activity, File file) {
        boolean res;
        if (PermissionUtils.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            res = file.mkdirs();
        } else {
            res = false;
        }

        return res;
    }

    public static boolean mkdir(Activity activity, File file) {
        boolean res;
        if (PermissionUtils.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            res = file.mkdir();
        } else {
            res = false;
        }

        return res;
    }

    public static boolean createNewFile(Activity activity, File file) throws IOException {
        boolean res = false;

        if (PermissionUtils.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            res = file.createNewFile();
        } else {
            requestPermission(activity);
            Log.e("changxing", "requestPermissionsWrapper" + mIsGrant);

            if (mIsGrant) {
                res = file.createNewFile();
            }
        }

        return res;
    }

    /**
     * 同一个request 的onRequestPermissionsResult 未返回时，再次调用将被系统忽略
     *
     * @param activity
     */
    private static void requestPermission(Activity activity) {
        PermissionUtils.requestPermissionsWrapper(activity, PermissionUtils.PERMISSION_REQUEST_CODE, Manifest.permission.READ_SMS);
        mIsGrant = false;
        mWaiting = true;
        while (mWaiting) {
        }
        Log.e("changxing", "requestPermissionsWrapper" + mIsGrant);

    }

    @TargetApi(16)
    public static boolean exist(Activity activity, File file) {
        return file.exists();
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (String p : permissions) {
            Log.e("changxing", "p:" + p);
        }
        for (int s : grantResults) {
            Log.e("changxing", "grantResults:" + s);
        }

        switch (requestCode) {
            case PermissionUtils.PERMISSION_REQUEST_CODE:
                if (verifyPermissions(grantResults)) {
                    mIsGrant = true;
                    Log.e("changxing", "verifyPermissions:" + mIsGrant);
                    // Permission Granted
                    // do you action
                } else {
                    // Permission Denied
//                    Toast.makeText(this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
//                            .show();
                }
                break;
        }
        mWaiting = false;
    }

    private static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
