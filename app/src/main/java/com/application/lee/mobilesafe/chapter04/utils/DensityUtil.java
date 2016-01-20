package com.application.lee.mobilesafe.chapter04.utils;

import android.content.Context;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\12\10
 * 描  述   ：
 * 修订历史  ：
 */
public class DensityUtil {
    /**
     * dip转换像素px
     */
    public static int dip2px(Context context, float dpValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int)dpValue;
    }

    /**
     * 像素px转换为dip
     */
    public static int px2dip(Context context, float pxValue) {
        try{
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue/scale + 0.5f);
        }catch(Exception e){
            e.printStackTrace();
        }
        return (int)pxValue;
    }
}
