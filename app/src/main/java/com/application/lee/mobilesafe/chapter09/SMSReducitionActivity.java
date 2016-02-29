package com.application.lee.mobilesafe.chapter09;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter09.utils.SmsReducitionUtils;
import com.application.lee.mobilesafe.chapter09.utils.UIUtils;
import com.application.lee.mobilesafe.chapter09.widget.MyCircleProgress;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\25
 * 描  述   ：
 * 修订历史  ：
 */
public class SMSReducitionActivity extends Activity implements View.OnClickListener {
    private MyCircleProgress mProgressButton;
    private boolean flag = false;
    private SmsReducitionUtils smsReducitionUtils;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(R.layout.activity_reducition);
        initView();
        smsReducitionUtils = new SmsReducitionUtils();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("短信还原");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mProgressButton = (MyCircleProgress) findViewById(R.id.mcp_reducition);
        mProgressButton.setOnClickListener(this);
    }

    protected void onDestroy(){
        flag = false;
        smsReducitionUtils.setFlag(flag);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.mcp_reducition:
                if(flag){
                    flag=false;
                    mProgressButton.setText("一键还原");
                }else{
                    flag=true;
                    mProgressButton.setText("取消还原");
                }
                smsReducitionUtils.setFlag(flag);
                new Thread(){
                    public void run(){
                        try {
                            smsReducitionUtils.reducitionSms(SMSReducitionActivity.this,new SmsReducitionUtils.SmsReducitionCallBack(){
                                @Override
                                public void beforeSmsReducition(int size) {
                                    mProgressButton.setMax(size);
                                }

                                @Override
                                public void onSmsReducition(int process) {
                                    mProgressButton.setProcess(process);
                                }
                            });
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                            UIUtils.showToast(SMSReducitionActivity.this,"文件格式错误");
                        } catch (IOException e) {
                            e.printStackTrace();
                            UIUtils.showToast(SMSReducitionActivity.this,"读写错误");
                        }
                    }
                }.start();
                break;
        }
    }
}
