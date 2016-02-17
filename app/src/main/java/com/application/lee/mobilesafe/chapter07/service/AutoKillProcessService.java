package com.application.lee.mobilesafe.chapter07.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\16
 * 描  述   ：
 * 修订历史  ：
 */
public class AutoKillProcessService extends Service {
    private ScreenLockReceiver receiver;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
        receiver = new ScreenLockReceiver();
        registerReceiver(receiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    public void onDestroy(){
        unregisterReceiver(receiver);
        receiver = null;
        super.onDestroy();
    }

    class ScreenLockReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            for(ActivityManager.RunningAppProcessInfo info:am.getRunningAppProcesses()){
                String packname = info.processName;
                am.killBackgroundProcesses(packname);
            }
        }
    }
}
