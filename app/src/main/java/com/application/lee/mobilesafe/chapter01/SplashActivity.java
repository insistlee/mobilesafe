package com.application.lee.mobilesafe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter01.utils.MyUtils;
import com.application.lee.mobilesafe.chapter01.utils.VersionUpdateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\12\10
 * 描  述   ：
 * 修订历史  ：
 */
public class SplashActivity extends Activity {
    private String mVersion;
    private TextView mVersionTV;
    protected static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置该Activity没有标题栏,在加载布局之前调用
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mVersion = MyUtils.getVersion(getApplicationContext());
        initView();
        final VersionUpdateUtils updateUtils = new VersionUpdateUtils(mVersion, SplashActivity.this);
        new Thread() {
            public void run() {
//                获取服务器版本号
                updateUtils.getCloudVersion();
            }

            ;
        }.start();
        copyDB("antivirus.db");
    }

    private void copyDB(String filename) {
        File file = new File(getFilesDir(), filename);
        try {
            if (file.exists() && file.length() > 0) {
                Log.i(TAG, "文件已经存在了，不需要再拷贝！");
            } else {
                InputStream is = getAssets().open(filename);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len=is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                }
                is.close();
                fos.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void startAnim(){

    }

    private void initView() {
        mVersionTV = (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("版本号" + mVersion);
    }
}
