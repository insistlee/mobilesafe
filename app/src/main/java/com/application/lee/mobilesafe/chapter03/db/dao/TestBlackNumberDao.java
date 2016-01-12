package com.application.lee.mobilesafe.chapter03.db.dao;

import android.content.Context;
import android.test.AndroidTestCase;

import com.application.lee.mobilesafe.chapter03.db.dao.BlackNumberDao;
import com.application.lee.mobilesafe.chapter03.entity.BlackContactInfo;

import java.util.Random;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\12
 * 描  述   ：
 * 修订历史  ：
 */
public class TestBlackNumberDao extends AndroidTestCase {
    private Context context;
    protected void setUp() throws Exception{
        context=getContext();
        super.setUp();
    }

    /**
     * 测试添加
     * @throws java.lang.Exception
     */
    public void testAdd() throws Exception{
        BlackNumberDao dao = new BlackNumberDao(context);
        Random random = new Random(8979);
        for(long i=0;i<30;i++){
            BlackContactInfo info = new BlackContactInfo();
            info.phoneNumber=1350000001+i+"";
            info.contactName="zhangsan"+i;
            info.mode=random.nextInt(3)+1;
            dao.add(info);
        }
    }
}
