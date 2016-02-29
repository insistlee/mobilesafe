package com.application.lee.mobilesafe.chapter09.utils;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\25
 * 描  述   ：
 * 修订历史  ：
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.application.lee.mobilesafe.chapter09.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类用来获取应用信息，此类重复
*/
public class AppInfoParser {
    /**
     * 获取手机里面的所有的应用程序
     * @param context 上下文
     * @return
    */
    public static List<AppInfo> getAppInfos(Context context){
        //得到一个包管理类
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        List<AppInfo> appinfos = new ArrayList<AppInfo>();
        for(PackageInfo packInfo:packInfos){
            AppInfo appinfo = new AppInfo();
            String packname = packInfo.packageName;
            appinfo.packageName = packname;
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            appinfo.icon = icon;
            String appname = packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appName=appname;
            //应用程序APK包的路径
            String apkpath = packInfo.applicationInfo.sourceDir;
            appinfo.apkPath = apkpath;
            appinfos.add(appinfo);
            appinfo = null;
        }
        return appinfos;
    }
}
