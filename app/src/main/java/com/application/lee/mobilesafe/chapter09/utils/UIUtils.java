package com.application.lee.mobilesafe.chapter09.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\24
 * 描  述   ：
 * 修订历史  ：
 */
public class UIUtils {
    public static void showToast(final Activity context,final String msg){
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        }else{
            context.runOnUiThread(new Runnable(){
                public void run(){
                    Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
