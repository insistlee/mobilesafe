package com.application.lee.mobilesafe.chapter07.entity;

import android.graphics.drawable.Drawable;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\29
 * 描  述   ：
 * 修订历史  ：
 */
public class TaskInfo {
    /**
     * 正在运行的app信息
     */
    public String appName;
    public Drawable appIcon;
    public String packageName;
    public long appMemory;
    /**用来标记app是否被选中*/
    public boolean isChecked;
    public boolean isUserApp;
}
