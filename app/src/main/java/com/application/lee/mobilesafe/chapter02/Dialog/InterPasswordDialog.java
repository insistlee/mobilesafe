package com.application.lee.mobilesafe.chapter02.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;


/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\12\28
 * 描  述   ：
 * 修订历史  ：
 */
public class InterPasswordDialog extends Dialog implements android.view.View.OnClickListener{
    /**标题栏*/
    private TextView mTitleTV;
    /**输入密码编辑框*/
    private EditText mPswd;
    /**回调接口*/
    private MyCallBack myCallBack;
    public InterPasswordDialog(Context context) {
        super(context,R.style.dialog_custom);    //引入自定义的对话框
    }

    public void setCallBack(MyCallBack myCallBack){
        this.myCallBack = myCallBack;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inter_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }

    /**初始化控件*/
    private void initView() {
        mTitleTV= (TextView) findViewById(R.id.tv_interpwd_title);
        mPswd= (EditText) findViewById(R.id.et_inter_password);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);
    }


    /**
     * 设置对话框标题栏
     * @param title
     */
    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mTitleTV.setText(title);
        }
    }

    /**
     * 获取输入框密码
     */
    public String getPassword(){
        String password = mPswd.getText().toString();
        if(TextUtils.isEmpty(password)){
            return "";
        }
        return password;
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_confirm:
                myCallBack.confirm();
                break;
            case R.id.btn_dismiss:
                myCallBack.cancle();
                break;
        }
    }

    public interface MyCallBack {
        void confirm();
        void cancle();
    }
}
