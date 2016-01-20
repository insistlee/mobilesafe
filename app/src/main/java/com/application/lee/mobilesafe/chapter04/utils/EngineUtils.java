package com.application.lee.mobilesafe.chapter04.utils;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\19
 * 描  述   ：
 * 修订历史  ：
 */

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.application.lee.mobilesafe.chapter04.entity.AppInfo;
import com.stericson.RootTools.RootTools;

/**业务工具类*/
public class EngineUtils {
    /**
     * 分享应用
     */
    public static void shareApplication(Context context,AppInfo appInfo){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"推荐您使用一款软件，名称："+appInfo.packageName);
        context.startActivity(intent);
    }
    /**
     * 开启应用程序
     */
    public static void startApplication(Context context,AppInfo appInfo){
        //打开这个应用程序的入口activity
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);
        if(intent!=null){
            context.startActivity(intent);
        }else{
            Toast.makeText(context,"该应用没有启动界面",Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 开启应用设置界面
     * @param  context
     * @param  appInfo
     */
    public static void SettingAppDetail(Context context ,AppInfo appInfo){
        Intent intent = new Intent();
        intent.setAction("android.setting.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+ appInfo.packageName));
        context.startActivity(intent);
    }
    /**卸载应用*/
    public static void uninstallApplication(Context context,AppInfo appInfo){
        if(appInfo.isUserApp){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:"+appInfo.packageName));
            context.startActivity(intent);
        }else{
           /* //系统应用需要root权限，利用linux命令删除文件
            if(!RootTools.isRootAvailable()){
                Toast.makeText(context,"卸载系统应用，必须要root权限",Toast.LENGTH_LONG).show();
                return;
            }
            try{
                if(!RootTools.isAccessGiven()){
                    Toast.makeText(context,"请授权手机小护卫root权限",Toast.LENGTH_LONG).show();
                    return;
                }
                RootTools.runShellCommand("adb.exe","mount -o remount ,rw /system");
                RootTools.runShellCommand("rm -r"+appInfo.apkPath,30000);
            }catch(Exception e){
                e.printStackTrace();
            }*/
            Toast.makeText(context,"卸载系统应用，必须要root权限",Toast.LENGTH_LONG).show();
        }
    }
}
