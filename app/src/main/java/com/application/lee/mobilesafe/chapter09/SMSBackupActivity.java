package com.application.lee.mobilesafe.chapter09;

import android.app.Activity;
import android.view.View;

import com.application.lee.mobilesafe.chapter09.utils.SmsBackUpUtils;
import com.application.lee.mobilesafe.chapter09.widget.MyCircleProgress;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\24
 * 描  述   ：
 * 修订历史  ：
 */
public class SMSBackupActivity extends Activity implements View.OnClickListener {
    private MyCircleProgress mProgressButton;
    /**标识符，用来标识备份状态的*/
    private boolean flag=false;
    private SmsBackUpUtils smsBackUpUtils;
    private static final int CHANGE_BUTTON_TEXT = 100;

    @Override
    public void onClick(View v) {

    }
}
