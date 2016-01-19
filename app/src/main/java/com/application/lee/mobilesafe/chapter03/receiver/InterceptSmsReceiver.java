package com.application.lee.mobilesafe.chapter03.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;

import com.application.lee.mobilesafe.chapter03.db.dao.BlackNumberDao;

import java.util.Objects;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\18
 * 描  述   ：
 * 修订历史  ：
 */
public class InterceptSmsReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSP = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean BlackNumStatus = mSP.getBoolean("BlackNumStatus",true);
        if(!BlackNumStatus){
            //黑名单拦截关闭
            return;
        }
        //如果是黑名单,则终止广播
        BlackNumberDao dao = new BlackNumberDao(context);
        Object[] objs = (Object[])intent.getExtras().get("pdus");
        for(Object obj:objs){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String sender = smsMessage.getOriginatingAddress();
            String body = smsMessage.getMessageBody();
            if(sender.startsWith("+86")){
                sender=sender.substring(3,sender.length());
            }
            int mode = dao.getBlackContactMode(sender);
            if(mode==2||mode==3){
                //需要拦截短信，拦截广播
                abortBroadcast();
            }
        }
    }
}
