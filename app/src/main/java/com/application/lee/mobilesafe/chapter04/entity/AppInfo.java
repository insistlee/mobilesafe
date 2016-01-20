package com.application.lee.mobilesafe.chapter04.entity;

import android.graphics.drawable.Drawable;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\19
 * 描  述   ：
 * 修订历史  ：
 */
public class AppInfo {
    /**应用程序包名*/
    public String packageName;
    /**应用程序图标*/
    public Drawable icon;
    /**应用程序名称*/
    public String appName;
    /**应用程序路径*/
    public String apkPath;
    /**应用程序大小*/
    public long appSize;
    /**是否手机存储*/
    public boolean isInRoom;
    /**是否用户应用*/
    public boolean isUserApp;
    /**是否选中，默认为false*/
    public boolean isSelected = false;
    /**拿到app位置字符串*/
    public String getAppLocation(boolean isInRoom){
        if(isInRoom){
            return "手机内存";
        }else{
            return "外部存储";
        }
    }
}
