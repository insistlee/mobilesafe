package com.application.lee.mobilesafe.chapter04;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter04.adapter.AppManagerAdapter;
import com.application.lee.mobilesafe.chapter04.entity.AppInfo;
import com.application.lee.mobilesafe.chapter04.utils.AppInfoParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\19
 * 描  述   ：
 * 修订历史  ：
 */
public class AppManagerActivity extends Activity implements View.OnClickListener {
    /**接收应用程序卸载成功的广播*/
    private UninstallReceiver receiver;
    private TextView mPhoneMemoryTV;
    private TextView mSDMemoryTV;
    private TextView mAppNumTV;
    private ListView mListView;
    private AppManagerAdapter adapter;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
    private List<AppInfo> systemAppInfos = new ArrayList<AppInfo>();
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch(msg.what){
                case 10:
                    if(adapter==null){
                        adapter = new AppManagerAdapter(userAppInfos,systemAppInfos,AppManagerActivity.this);
                    }
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 15:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_manager);
        //注册广播
        receiver = new UninstallReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(receiver,intentFilter);
        initView();
    }

    /**初始化控件*/
    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_yellow));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("软件管家");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mPhoneMemoryTV= (TextView) findViewById(R.id.tv_phonememory_appmanager);
        mSDMemoryTV= (TextView) findViewById(R.id.tv_sdmemory_appmanager);
        mAppNumTV= (TextView) findViewById(R.id.tv_appnumber);
        mListView= (ListView) findViewById(R.id.lv_appmanager);
        //取得手机剩余内存和SD卡剩余内存
        getMemoryFromPhone();
        initData();
        initListener();
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(adapter!=null){
                    new Thread(){
                        public void run(){
                            AppInfo mappInfo = (AppInfo)adapter.getItem(position);
                            //记住当前条目的状态
                            boolean flag = mappInfo.isSelected;
                            //先将集合中所有条目的AppInfo变为未选中状态
                            for(AppInfo appInfo:userAppInfos){
                                appInfo.isSelected = false;
                            }
                            for(AppInfo appInfo:systemAppInfos){
                                appInfo.isSelected = false;
                            }
                            if(mappInfo!=null){
                                //如果已经选中，则变为未选中
                                if(flag){
                                    mappInfo.isSelected = false;
                                }else{
                                    mappInfo.isSelected = true;
                                }
                                mHandler.sendEmptyMessage(15);
                            }
                        };
                    }.start();
                }
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem>=userAppInfos.size()+1){
                    mAppNumTV.setText("系统程序"+systemAppInfos.size()+"个");
                }else{
                    mAppNumTV.setText("用户程序"+userAppInfos.size()+"个");
                }
            }
        });
    }

    private void initData() {
        appInfos = new ArrayList<AppInfo>();
        new Thread(){
            public void run(){
                appInfos.clear();
                systemAppInfos.clear();
                userAppInfos.clear();
                appInfos.addAll(AppInfoParser.getAppInfos(AppManagerActivity.this));
                for(AppInfo appInfo:appInfos){
                    //如果是用户App
                    if(appInfo.isUserApp){
                        userAppInfos.add(appInfo);
                    }else{
                        systemAppInfos.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(10);
            };
        }.start();
    }

    /**拿到sdk和手机剩余内存*/
    private void getMemoryFromPhone() {
        long avail_sd = Environment.getExternalStorageDirectory().getFreeSpace();
        long avail_rom = Environment.getDataDirectory().getFreeSpace();
        //格式化内存
        String str_avail_sd = Formatter.formatFileSize(this,avail_sd);
        String str_avail_rom = Formatter.formatFileSize(this,avail_rom);
        mPhoneMemoryTV.setText("剩余手机内存"+str_avail_rom);
        mSDMemoryTV.setText("剩余SD卡内存"+str_avail_sd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }

    /**
     * 接收应用程序卸载的广播
     * @author Lee
    */
    class UninstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播了
            initData();
        }
    }

    protected void onDestroy(){
        unregisterReceiver(receiver);
        receiver = null;
        super.onDestroy();
    }
}
