package com.application.lee.mobilesafe.chapter02;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.application.lee.mobilesafe.R;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\12\29
 * 描  述   ：
 * 修订历史  ：
 */
public class SetUp2Activity extends BaseSetUpActivity implements View.OnClickListener {
    private TelephonyManager mTelephonyManager;
    private Button mBindSIMBtn;
//    private SharedPreferences sp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        initView();
    }

    private void initView() {
//        设置第二个小圆点的颜色
        ((RadioButton) findViewById(R.id.second)).setChecked(true);
        mBindSIMBtn = (Button) findViewById(R.id.btn_bind_sim);
        mBindSIMBtn.setOnClickListener(this);
        if (isBind()) {
            mBindSIMBtn.setEnabled(false);
        } else {
            mBindSIMBtn.setEnabled(true);
        }
    }

    private boolean isBind() {
        String simString = sp.getString("sim", null);
        if (TextUtils.isEmpty(simString)) {
            return false;
        }
        return true;
    }

    @Override
    protected void showNext() {
        if(!isBind()){
            Toast.makeText(this,"您还没有绑定sim卡",Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityAndFinishSelf(SetUp3Activity.class);
    }

    @Override
    protected void showPre() {
        startActivityAndFinishSelf(SetUp1Activity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind_sim:
//                绑定sim卡
                bindSIM();
                break;
        }
    }

    /*
    * 绑定sim卡
    */
    private void bindSIM() {
        if (!isBind()) {
            String simSerialNumber = mTelephonyManager.getSimSerialNumber();
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("sim", simSerialNumber);
            edit.commit();
            Toast.makeText(this, "sim卡绑定成功", Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);
        } else {
//            已经绑定，提醒用户
            Toast.makeText(this, "sim卡已经绑定!", Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);
        }
    }
}
