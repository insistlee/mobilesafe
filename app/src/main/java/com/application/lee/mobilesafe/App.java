package com.application.lee.mobilesafe;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\12\10
 * 描  述   ：
 * 修订历史  ：
 */
public class App extends Application {
    public void onCreate(){
        super.onCreate();
        correctSIM();
    }

    public void correctSIM() {
//        检查SIM卡是否发生变化
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
//        获取防盗保护的状态
        boolean protecting = sp.getBoolean("protecting",true);
        if(protecting){
//            得到绑定的sim卡串号
            String bindsim = sp.getString("sim","");
//            得到手机现在的sim卡串号
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            为了测试在手机序列号上data已模拟sim卡被更换的情况
            String realsim = tm.getSimSerialNumber();
            if(bindsim.equals(realsim)){
                Log.i("","sim卡未发生变化，还是您的手机");
            }else{
                Log.i("","sim卡变化了");
                String safenumber = sp.getString("safephone","");
                if(!TextUtils.isEmpty(safenumber)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safenumber,null,"你的亲友手机的sim卡已经被更换",null,null);
                }
            }
        }
    }
}
