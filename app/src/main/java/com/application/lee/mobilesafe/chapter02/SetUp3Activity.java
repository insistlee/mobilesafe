package com.application.lee.mobilesafe.chapter02;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.application.lee.mobilesafe.R;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\1\4
 * 描  述   ：
 * 修订历史  ：
 */
public class SetUp3Activity extends BaseSetUpActivity implements View.OnClickListener {
    private EditText mInputPhone;

    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initView();
    }

    private void initView() {
        ((RadioButton)findViewById(R.id.rb_third)).setChecked(true);
        findViewById(R.id.btn_addcontact).setOnClickListener(this);
        mInputPhone = (EditText) findViewById(R.id.et_inputphone);
        String safephone = sp.getString("safephone",null);
        if(!TextUtils.isEmpty(safephone)){
            mInputPhone.setText(safephone);
        }
    }

    @Override
    protected void showNext() {
//        判断文本框中是否有电话号码
        String safephone = mInputPhone.getText().toString().trim();
        if(TextUtils.isEmpty(safephone)){
            Toast.makeText(this,"请输入安全号码",Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("safephone",safephone);
        edit.commit();
        startActivityAndFinishSelf(SetUp4Activity.class);
    }

    @Override
    protected void showPre() {
        startActivityAndFinishSelf(SetUp2Activity.class);
    }

    @Override
    public void onClick(View v) {

    }
}
