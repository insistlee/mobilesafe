package com.application.lee.mobilesafe.chapter02;

import android.os.Bundle;
import android.widget.RadioButton;

import com.application.lee.mobilesafe.R;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\12\10
 * 描  述   ：
 * 修订历史  ：
 */
public class SetUp1Activity extends BaseSetUpActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        initView();
    }

    private void initView() {
//        设置第一个小圆点的颜色
        ((RadioButton)findViewById(R.id.rb_first)).setChecked(true);
    }

    @Override
    protected void showNext() {
        startActivityAndFinishSelf(SetUp2Activity.class);
    }

    @Override
    protected void showPre() {

    }
}
