package com.application.lee.mobilesafe.chapter02;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public class LostFindActivity extends Activity implements View.OnClickListener{
    private SharedPreferences msharedPreferencrs;
    private TextView mSafePhoneTV;
    private ToggleButton mToggleButton;
    private RelativeLayout mInterSetupRL;
    private TextView mProtectStatusTV;

    protected void onCreate(android.os.Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lostfind);
        msharedPreferencrs = getSharedPreferences("config",MODE_PRIVATE);
        if(!isSetUp()){
//            如果没有进入过设置向导，则进入
            startSetp1Activity();
        }
        initView();
    }

    private void initView() {
        TextView mTitleTV = (TextView) findViewById(R.id.tv_title);
        mTitleTV.setText("手机防盗");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
        mSafePhoneTV=(TextView)findViewById(R.id.tv_safephone);
        mSafePhoneTV.setText(msharedPreferencrs.getString("safephone",""));
        mToggleButton = (ToggleButton)findViewById(R.id.togglebtn_lostfind);
        mInterSetupRL = (RelativeLayout)findViewById(R.id.rl_inter_setup_wizard);
        mInterSetupRL.setOnClickListener(this);
        mProtectStatusTV = (TextView)findViewById(R.id.tv_lostfind_protectstatus);
//        查询手机防盗是否开启
        boolean protecting = msharedPreferencrs.getBoolean("protecting",true);
        if(protecting){
            mProtectStatusTV.setText("防盗保护已经开启");
            mToggleButton.setChecked(true);
        }else{
            mProtectStatusTV.setText("防盗保护没有开启");
            mToggleButton.setChecked(false);
        }
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mProtectStatusTV.setText("防盗保护已经开启");
                }else{
                    mProtectStatusTV.setText("防盗保护没有开启");
                }
                SharedPreferences.Editor editor = msharedPreferencrs.edit();
                editor.putBoolean("protecting",isChecked);
                editor.commit();
            }
        });
    }

    private void startSetp1Activity() {
        Intent intent = new Intent(LostFindActivity.this,SetUp1Activity.class);
        startActivity(intent);
        finish();
    }

    private boolean isSetUp() {
        return msharedPreferencrs.getBoolean("isSetUp",false);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rl_inter_setup_wizard:
                startSetp1Activity();
                break;
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
}
