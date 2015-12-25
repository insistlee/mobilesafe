package com.application.lee.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.application.lee.mobilesafe.chapter01.adapter.HomeAdapter;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\12\10
 * 描  述   ：
 * 修订历史  ：
 */
public class HomeActivity extends Activity {
//    申明GridView，该控件类似ListView
    private GridView gv_home;
    private long mExitTime;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        初始化布局
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
//        初始化GridView
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
//        设置条目的点击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            parent代表GridView，view代表每个条目的view对象，position代表每个条目的位置
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://手机防盗
                       /* if(isSetUpPassword()){
                            //弹出输入密码对话框
                            showInterPswDialog();
                        }else{
                            //弹出设置密码对话框
                            showSetUpPswDialog();
                        }*/
                        break;
                    case 1://通讯卫士
                        /*startActivity(SecurityPhoneActivity.class);*/
                        break;
                    case 2://软件管家
                        /*startActivity(AppManagerActivity.class);*/
                        break;
                    case 3://手机杀毒
                        /*startActivity(VirusScanActivity.class);*/
                        break;
                    case 4://缓存清理
                        /*startActivity(CacheClearListActivity.class);*/
                        break;
                    case 5://进程管理
                        /*startActivity(ProcessManagerActivity.class);*/
                        break;
                    case 6://流量统计
                        /*startActivity(TrafficMonitoringActivity.class);*/
                        break;
                    case 7://高级工具
                        /*startActivity(AdvancedToolsActivity.class);*/
                        break;
                    case 8://设置中心
                        /*startActivity(SettingsActivity.class);*/
                        break;
                }
            }
        });
    }

    /**
     * 开启新的Activity不关闭自己
     * @param cls,新的Activity的字节码
     */
    public void startActivity(Class<?> cls){
        Intent intent = new Intent(HomeActivity.this,cls);
        startActivity(intent);
    }

    /**
     * 按两次返回键退出程序
     */
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis()-mExitTime)<2000){
                System.exit(0);
            }else{
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
 }
