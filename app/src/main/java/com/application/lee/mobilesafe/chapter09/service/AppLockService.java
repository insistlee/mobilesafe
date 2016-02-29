package com.application.lee.mobilesafe.chapter09.service;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\26
 * 描  述   ：
 * 修订历史  ：
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import com.application.lee.mobilesafe.App;
import com.application.lee.mobilesafe.chapter09.EnterPswActivity;
import com.application.lee.mobilesafe.chapter09.db.dao.AppLockDao;

import java.util.List;

/**
 * 程序锁服务
 * @author Lee
 */
public class AppLockService extends Service{
    /**是否开启程序锁服务的标志*/
    private boolean flag = false;
    private AppLockDao dao;
    private Uri uri = Uri.parse("content://com.application.lee.mobilesafe.applock");
    private List<String> packagenames;
    private Intent intent;
    private ActivityManager am;
    private List<ActivityManager.RunningTaskInfo> taskInfos;
    private ActivityManager.RunningTaskInfo taskInfo;
    private String packagename;
    private String tempStopProtectPackname;
    private AppLockReceiver receiver;
    private MyObserver observer;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        //创建AppLockDao
        dao = new AppLockDao(this);
        observer = new MyObserver(new Handler());
        getContentResolver().registerContentObserver(uri,true,observer);
        //获取数据库中的所有包名
        packagenames=dao.findAll();
        receiver = new AppLockReceiver();
        IntentFilter filter = new IntentFilter("com.application.lee.mobilesafe.applock");
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver,filter);
        //创建Intent实例用来打开输入密码页面
        intent = new Intent(AppLockService.this,EnterPswActivity.class);
        //获取ActivityManager对象
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        startApplockService();
        super.onCreate();
    }

    /**开启监控程序服务*/
    private void startApplockService() {
        new Thread(){
            public void run(){
                flag = true;
                while(flag){
                    //监视任务栈的情况，使用的打开的任务栈在集合的最前面
                    taskInfos = am.getRunningTasks(1);
                    //最近使用的任务栈
                    taskInfo = taskInfos.get(0);
                    packagename = taskInfo.topActivity.getPackageName();
                    //判断这个包名是否需要被保护
                    if(!packagename.equals(tempStopProtectPackname)){
                        //需要保护，弹出一个输入密码的界面
                        intent.putExtra("packagename",packagename);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //广播接收者
    class AppLockReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if("com.application.lee.mobilesafe.applock".equals(intent.getAction())){
                tempStopProtectPackname = intent.getStringExtra("packagename");
            }else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
                tempStopProtectPackname = null;
                //停止监控程序
                if(flag==false){
                    startApplockService();
                }
            }
        }
    }

    //内容观察者
    class MyObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange){
            packagenames=dao.findAll();
            super.onChange(selfChange);
        }
    }

    public void onDestroy(){
        flag = false;
        unregisterReceiver(receiver);
        receiver = null;
        getContentResolver().unregisterContentObserver(observer);
        observer = null;
        super.onDestroy();
    }
}
