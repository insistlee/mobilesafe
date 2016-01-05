package com.application.lee.mobilesafe.chapter02.receiver;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter02.service.GPSLocationService;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\1\5
 * 描  述   ：
 * 修订历史  ：
 */
public class SmsLostFindReceiver extends BroadcastReceiver {
    private SharedPreferences sharedPreferences;
    private static final String TAG = SmsLostFindReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences("config", Activity.MODE_PRIVATE);
        boolean protecting = sharedPreferences.getBoolean("protecting",true);
        if(protecting){//防盗保护已经开启
//           获取超级管理员
            DevicePolicyManager dpm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            Object[] objs = (Object[])intent.getExtras().get("pdus");
            for(Object obj:objs){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])obj);
                String sender = smsMessage.getOriginatingAddress();
                String body = smsMessage.getMessageBody();
                String safephone = sharedPreferences.getString("safephone", null);
//                如果该短信是安全号码发送的
                if(!TextUtils.isEmpty(safephone)&sender.equals(safephone)){
                    if("#*location*#".equals(body)){
                        Log.i(TAG,"返回位置信息.");
//                        获取位置放在服务里面去实现
                        Intent service = new Intent(context,GPSLocationService.class);
                        context.startService(service);
                        abortBroadcast();
                    }else if("#*alarm*#".equals(body)){
                        Log.i(TAG,"播放报警音乐.");
                        MediaPlayer player = MediaPlayer.create(context, R.raw.warning);
                        player.setVolume(1.0f,1.0f);
                        player.start();
                        abortBroadcast();
                    }else if("#*wipedata*#".equals(body)){
                        Log.i(TAG,"远程清楚数据");
                        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                        abortBroadcast();
                    }else if("#*lockscreen*#".equals(body)){
                        Log.i(TAG,"远程锁屏.");
                        dpm.resetPassword("123",0);
                        dpm.lockNow();//没有管理员权限，调用时会崩溃
                        abortBroadcast();
                    }
                }
            }
        }
    }
}
