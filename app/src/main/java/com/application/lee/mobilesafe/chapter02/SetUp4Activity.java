package com.application.lee.mobilesafe.chapter02;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.application.lee.mobilesafe.R;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\12\10
 * 描  述   ：
 * 修订历史  ：
 */
public class SetUp4Activity extends BaseSetUpActivity{
    private TextView mStatusTV;
    private ToggleButton mToggleButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initView();
    }

    private void initView() {
        ((RadioButton)findViewById(R.id.rb_forth)).setChecked(true);
        mStatusTV = (TextView)findViewById(R.id.tv_setup4_status);
        mToggleButton = (ToggleButton)findViewById(R.id.togglebtn_securityfunction);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mStatusTV.setText("防盗保护已经开启");
                }else{
                    mStatusTV.setText("防盗保护已经关闭");
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("protecting",isChecked);
                editor.commit();
            }
        });
        boolean protecting = sp.getBoolean("protecting",true);
        if(protecting){
            mStatusTV.setText("防盗保护已经开启");
            mToggleButton.setChecked(true);
        }else{
            mStatusTV.setText("防盗保护已经关闭");
            mToggleButton.setChecked(false);
        }
    }

    @Override
    protected void showNext() {
//        跳转至防盗保护页面
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isSetUp",true);
        editor.commit();
        startActivityAndFinishSelf(LostFindActivity.class);
    }

    @Override
    protected void showPre() {
        startActivityAndFinishSelf(SetUp3Activity.class);
    }
}
