package com.application.lee.mobilesafe.chapter09;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter09.db.dao.NumBelongtoDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\23
 * 描  述   ：
 * 修订历史  ：
 */
public class NumbelongtoActivity extends Activity implements View.OnClickListener {
    private EditText mNumET;
    private TextView mResultTV;
    private String dbName="address.db";
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
        };
    };

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_numbelongto);
        initView();
        copyDB(dbName);
    }

    /**
     * 复制资产目录下的数据库文件
     * @param dbName 数据库文件的名称
    */
    private void copyDB(final String dbName) {
        new Thread(){
            public void run(){
                try {
                    File file = new File(getFilesDir(),dbName);
                    if(file.exists()&&file.length()>0){
                        Log.i("NumBelongtoActiity","数据库已存在");
                        return;
                    }
                    InputStream is = getAssets().open(dbName);
                    FileOutputStream fos = openFileOutput(dbName,MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len=is.read(buffer))!=-1){
                        fos.write(buffer,0,len);
                    }
                    is.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**初始化控件*/
    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("号码归属地查询");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.btn_searchnumbelongto).setOnClickListener(this);
        mNumET = (EditText) findViewById(R.id.et_num_numberlongto);
        mResultTV = (TextView) findViewById(R.id.tv_searchresult);
        mNumET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //文本变化之后
                String string = s.toString().toString().trim();
                if(string.length()==0){
                    mResultTV.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgv_left:
                finish();
                break;
            case R.id.btn_searchnumbelongto:
                //判断edittext中的号码是否为空
                //判断数据库是否存在
                String phonenumber = mNumET.getText().toString().trim();
                if(!TextUtils.isEmpty(phonenumber)){
                    File file = new File(getFilesDir(),dbName);
                    if(!file.exists()||file.length()<=0){
                        //数据库不存在，复制数据库
                        copyDB(dbName);
                    }
                    //查询数据库
                    String location  = NumBelongtoDao.getLocation(phonenumber);
                    mResultTV.setText("归属地:"+location);
                }else{
                    Toast.makeText(this,"请输入需要查询的号码",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
