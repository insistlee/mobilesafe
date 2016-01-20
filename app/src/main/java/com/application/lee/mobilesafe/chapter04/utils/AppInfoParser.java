package com.application.lee.mobilesafe.chapter04.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.application.lee.mobilesafe.chapter04.entity.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\19
 * 描  述   ：
 * 修订历史  ：
 */
public class AppInfoParser {
    /**
     * 获取手机里面所有的应用程序
     * @param context 上下文
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context){
        //得到一个包管理器
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for(PackageInfo packageInfo:packageInfos){
            AppInfo appInfo = new AppInfo();
            String packname = packageInfo.packageName;
            appInfo.packageName = packname;
            Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
            appInfo.icon = icon;
            String appname = packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.appName = appname;
            //应用程序apk包的路径
            String apkpath = packageInfo.applicationInfo.sourceDir;
            appInfo.apkPath = apkpath;
            File file = new File(apkpath);
            long appSize = file.length();
            appInfo.appSize = appSize;
            //应用程序安装的位置
            int flags = packageInfo.applicationInfo.flags;//二进制映射
            if((ApplicationInfo.FLAG_EXTERNAL_STORAGE&flags)!=0){
                //外部存储
                appInfo.isInRoom = false;
            }else{
                //手机内存
                appInfo.isInRoom = true;
            }
            if((ApplicationInfo.FLAG_SYSTEM&flags)!=0){
                //系统应用
                appInfo.isUserApp = false;
            }else{
                //用户应用
                appInfo.isUserApp = true;
            }
            appInfos.add(appInfo);
            appInfo = null;
        }
        return appInfos;
    }
}
