package com.application.lee.mobilesafe.chapter07.utils;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\16
 * 描  述   ：
 * 修订历史  ：
 */

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter07.entity.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务信息进程信息的解析器
 * @author Lee
 */
public class TaskInfoParser {
    /**
     *获取正在运行的所有进程的信息
     * @param context 上下文
     * @return 进程信息的集合
     */
    public static List<TaskInfo> getRunningTaskInfos(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        for(ActivityManager.RunningAppProcessInfo processInfo:processInfos){
            String packagename = processInfo.processName;
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.packageName = packagename;//进程名称
            Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
            long memsize = memoryInfos[0].getTotalPrivateDirty()*1024;
            taskInfo.appMemory = memsize;//程序占用的内存空间
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packagename,0);
                Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
                taskInfo.appIcon = icon;
                String appname = packageInfo.applicationInfo.loadLabel(pm).toString();
                taskInfo.appName = appname;
                if((ApplicationInfo.FLAG_SYSTEM&packageInfo.applicationInfo.flags)!=0){
                    //系统进程
                    taskInfo.isUserApp = false;
                }else{
                    //用户进程
                    taskInfo.isUserApp = true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.appName = packagename;
                taskInfo.appIcon = context.getResources().getDrawable(R.drawable.ic_default);
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }
}
