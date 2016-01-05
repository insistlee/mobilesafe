package com.application.lee.mobilesafe.chapter02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter02.Utils.ContactInfoParser;
import com.application.lee.mobilesafe.chapter02.adapter.ContactAdapter;
import com.application.lee.mobilesafe.chapter02.entity.ContactInfo;

import java.util.List;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\4
 * 描  述   ：
 * 修订历史  ：
 */
public class ContactSelectActivity extends Activity implements View.OnClickListener {
    private List<ContactInfo> systemContacts;
    private ListView mListView;
    private ContactAdapter adapter;

    Handler mHandler = new Handler(){
        public void HandlerMessage(android.os.Message msg){
            switch(msg.what){
                case 10 :
                    if(systemContacts!=null){
                        adapter = new ContactAdapter(systemContacts,ContactSelectActivity.this);
                        mListView.setAdapter(adapter);
                    }
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact_select);
        initView();
    }

    private void initView() {
        ((TextView)findViewById(R.id.tv_title)).setText("选择联系人");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
//        设置导航栏颜色
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
        mListView = (ListView) findViewById(R.id.lv_contact);
        new Thread(){
            public void run(){
                systemContacts = ContactInfoParser.getSystemContact(ContactSelectActivity.this);
                systemContacts.addAll(ContactInfoParser.getSystemContact(ContactSelectActivity.this));
                mHandler.sendEmptyMessage(10);
            }
        }.start();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactInfo item = (ContactInfo) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("phone",item.phone);
                setResult(0,intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
}
