package com.canking.sdcardhelper;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText("has SdCard:" + sdCardExist + " \n getExternalStorageState:" + Environment.getExternalStorageState() + "\n不需要任何权限");

    }

    public void onClick(View v) {
        File sd = Environment.getExternalStorageDirectory();
        int id = v.getId();
        switch (id) {
            case R.id.btn1:
                File sdcard = Environment.getExternalStorageDirectory();
                TextView textView = (TextView) findViewById(R.id.text1);
                textView.setText("getExternalStorageDirectory:" + sdcard +
                        "\ngetAbsolutePath:" + sdcard.getAbsolutePath() +
                        "\ncanRead:" + sdcard.canRead() + " canWrite:" + sdcard.canWrite() +
                        "\n不需要任何权限");
                break;
            case R.id.btn2:
                TextView textView2 = (TextView) findViewById(R.id.text2);
                StringBuilder stringBuilder = new StringBuilder();
                if (sd.isDirectory()) {
                    File[] list = sd.listFiles();
                    if (list != null) {
                        for (File l : list) {
                            stringBuilder.append("\nF:" + l.toString());
                        }
                    } else {
                        stringBuilder.append("sd.listFiles() = null");
                    }
                } else {
                    stringBuilder.append("SD card is not directory!");
                }
                textView2.setText(stringBuilder);
                break;
            case R.id.btn3:
                StringBuffer buffer = new StringBuffer();
                String filename = this.getExternalCacheDir() + "/testa";
                File fileC = this.getExternalFilesDir(android.os.Environment.DIRECTORY_MUSIC);
                File fileD = this.getExternalFilesDir(android.os.Environment.DIRECTORY_ALARMS);

                buffer.append("fileMusic:" + fileC.toString()+" read:"+fileC.canRead()+" write:"+fileC.canWrite()+"\n");
                buffer.append("fileAlarms:" + fileD.toString()+" read:"+fileD.canRead()+" fileD:"+fileC.canWrite()+"\n");

                File newFile = new File(filename);
                if (newFile.exists()) {
                    boolean d = newFile.delete();
                    buffer.append("newFile exists, and Delete:" + d);
                } else {
                    buffer.append("newFile not exists\n");

                    try {
                        boolean t = newFile.createNewFile();
                        buffer.append("newFile create:" + t);
                    } catch (Exception e) {
                        buffer.append("newFile create error:" + e.getMessage());
                    }
                }
                TextView textView3 = (TextView) findViewById(R.id.text3);
                textView3.setText(buffer);
                break;
            case R.id.btn4:
                boolean read = PermissionUtils.checkSelfPermissionWrapper(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                boolean write = PermissionUtils.checkSelfPermissionWrapper(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                TextView textView4 = (TextView) findViewById(R.id.text4);
                textView4.setText("App Read:" + read + " Write:" + write);
                break;
            case R.id.btn5:
                boolean check = PermissionUtils.checkPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, 1);
                TextView textView5 = (TextView) findViewById(R.id.text5);
                textView5.setText("check read:" + check);
                break;
        }
    }
}
