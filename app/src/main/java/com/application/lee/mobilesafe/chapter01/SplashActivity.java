package com.application.lee.mobilesafe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
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
 * 修订历史  ：第一次修改 2016\1\25
 */
public class SplashActivity extends Activity {
    private String mVersion;
    private TextView mVersionTV;
    private RelativeLayout rl_splashacivity;
    protected static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置该Activity没有标题栏,在加载布局之前调用
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        rl_splashacivity = (RelativeLayout) findViewById(R.id.rl_splash_activity);
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
        startAnim();
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

    /**
     * 动画的实现
    */
    private void startAnim(){
        //渐变动画
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(6000);//设置渐变时间
        alpha.setFillAfter(true);

        rl_splashacivity.startAnimation(alpha);
    }

    private void initView() {
        mVersionTV = (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("版本号" + mVersion);
    }
}
