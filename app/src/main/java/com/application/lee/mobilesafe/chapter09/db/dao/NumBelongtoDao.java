package com.application.lee.mobilesafe.chapter09.db.dao;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\22
 * 描  述   ：
 * 修订历史  ：
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**查询号码归属地的数据库逻辑类*/
public class NumBelongtoDao {
    /**
     * 返回电话号码的归属地
     * @param phonenumber 电话号码
     * @return 归属地
     */
    public static String getLocation(String phonenumber){
        String cardtype = phonenumber;
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.application.lee.mobilesafe/files/address.db",null,SQLiteDatabase.OPEN_READONLY);
        //通过正则表达式匹配号段，13x 14x 15x 17x 18x,
        //130 131 132 133 134 135 136 137 138 139
        if(phonenumber.matches("^1[34578]\\d{9}$")){
            //手机号查询
            Cursor cursor = db.rawQuery("select cardtype from info where mobileprefix=?",new String[] {phonenumber.substring(0,7)});
            if(cursor.moveToNext()){
                cardtype = cursor.getString(0);
            }
            cursor.close();
        }else{//其他电话
            switch(phonenumber.length()){//判断电话号码的长度
                case 3://110 120 119 121 999
                    if("110".equals(phonenumber)){
                        cardtype = "匪警";
                    }else if("120".equals(phonenumber)){
                        cardtype = "急救";
                    }else{
                        cardtype = "报警号码";
                    }
                    break;
                case 4:
                    cardtype = "模拟器";
                    break;
                case 5:
                    cardtype = "客服电话";
                    break;
                case 7:
                    cardtype = "本地电话";
                    break;
                case 8:
                    cardtype = "本地电话";
                    break;
                default:
                    if(cardtype.length()>=9&&cardtype.startsWith("0")){
                        String address = null;
                        Cursor cursor = db.rawQuery("select cardtype from info where area = ?",new String[]{cardtype.substring(1,3)});
                        if(cursor.moveToNext()){
                            String str = cursor.getString(0);
                            address = str.substring(0,str.length()-2);
                        }
                        cursor.close();
                        cursor = db.rawQuery("select cardtype from info where area = ?",new String[]{cardtype.substring(1,4)});
                        if(cursor.moveToNext()){
                            String str = cursor.getString(0);
                            address = str.substring(0,str.length()-2);
                        }
                        cursor.close();
                        if(!TextUtils.isEmpty(address)){
                            cardtype = address;
                        }
                    }
                    break;
            }
        }
        db.close();
        return cardtype;
    }
}
