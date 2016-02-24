package com.application.lee.mobilesafe.chapter09;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\22
 * 描  述   ：
 * 修订历史  ：
 */
public class AdvancedToolsActivity extends Activity implements View.OnClickListener {
    protected void onCreate(Bundle savedInstancedState){
        super.onCreate(savedInstancedState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advancetools);
        initView();
    }

    /**初始化控件*/
    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("高级工具");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.advanceview_applock).setOnClickListener(this);
        findViewById(R.id.advanceview_numberlongs).setOnClickListener(this);
        findViewById(R.id.advanceview_smsbackup).setOnClickListener(this);
        findViewById(R.id.advanceview_smsreducition).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.advanceview_applock:
                //进入程序锁页面
                startActivity(AppLockActivity.class);
                break;
            case R.id.advanceview_numberlongs:
                //进入归属地查询页面
                startActivity(NumbelongtoActivity.class);
                break;
            case R.id.advanceview_smsbackup:
                //进入短信备份页面
                startActivity(SMSBackupActivity.class);
                break;
            case R.id.advanceview_smsreducition:
                //进入短信还原页面
                startActivity(SMSReducitionActivity.class);
                break;
        }
    }

    /**
     * 开启新的Activity不关闭自己
     * @param cls 新的activity的字节码
    */
    public void startActivity(Class<?> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }
}
