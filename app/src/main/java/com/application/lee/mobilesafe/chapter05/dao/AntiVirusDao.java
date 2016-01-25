package com.application.lee.mobilesafe.chapter05.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\20
 * 描  述   ：
 * 修订历史  ：
 */

public class AntiVirusDao {
    /**
     * 检查某个md5是否是病毒
     * @param md5
     * @return null 代表扫描安全
    */

    public static String checkVirus(String md5){
        String desc = null;
        //打开病毒数据库
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.application.lee.mobilesafe/files/antivirus.db",null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select desc from datable where md5=?",new String[]{md5});
        if(cursor.moveToNext()){
            desc = cursor.getString(0);
            System.out.println(desc);
        }
        cursor.close();
        db.close();
        return desc;
    }
}
