package com.application.lee.mobilesafe.chapter07.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\29
 * 描  述   ：
 * 修订历史  ：
 */
public class SystemInfoUtils {
    /**
     * 判断一个服务是否处于运行
     * @param context 上下文
     * @retutrn
     */
    public static boolean isServiceRunning(Context context,String className){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(200);
        for(ActivityManager.RunningServiceInfo info:infos){
            String serviceClassName = info.service.getClassName();
            if(className.equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }
    /**
     * 获取手机总内存大小单位byte
     * @return
     */
    public static long getTotalMem(){
        try {
            FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String totalInfo = br.readLine();
            StringBuffer sb = new StringBuffer();
            for(char c:totalInfo.toCharArray()){
                if(c>='0'&&c<='9'){
                    sb.append(c);
                }
            }
            long bytesize = Long.parseLong(sb.toString())*1024;
            return bytesize;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * 获取可用内存的信息
     * @param context
     * @return
     */
    public static long getAvailMem(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取内存大小
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        long availMem = outInfo.availMem;
        return availMem;
    }
    /**
     * 得到正在运行的进程数量
     * @param context
     * @return
     */
    public static int getRunningProcessCount(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
        int count = runningAppProcessInfos.size();
        return count;
    }
}
