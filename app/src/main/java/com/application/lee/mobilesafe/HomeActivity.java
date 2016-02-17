package com.application.lee.mobilesafe;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.application.lee.mobilesafe.chapter01.adapter.HomeAdapter;
import com.application.lee.mobilesafe.chapter02.Dialog.InterPasswordDialog;
import com.application.lee.mobilesafe.chapter02.Dialog.SetUpPasswordDialog;
import com.application.lee.mobilesafe.chapter02.LostFindActivity;
import com.application.lee.mobilesafe.chapter02.Utils.MD5Utils;
import com.application.lee.mobilesafe.chapter02.receiver.MyDeviceAdminReceiver;
import com.application.lee.mobilesafe.chapter03.SecurityPhoneActivity;
import com.application.lee.mobilesafe.chapter04.AppManagerActivity;
import com.application.lee.mobilesafe.chapter05.VirusScanActivity;
import com.application.lee.mobilesafe.chapter06.CacheClearListActivity;
import com.application.lee.mobilesafe.chapter07.ProcessManagerActivity;

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
    private SharedPreferences msharedPreferences;
    private DevicePolicyManager PolicyManager;
    private ComponentName componentName;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        初始化布局
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        msharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
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
                        if(isSetUpPassword()){
                        //弹出输入密码对话框
                        showInterPswdDialog();
                    }else{
                        //弹出设置密码对话框
                        showSetUpPswDialog();
                    }
                        break;
                    case 1://通讯卫士
                        startActivity(SecurityPhoneActivity.class);
                        break;
                    case 2://软件管家
                        startActivity(AppManagerActivity.class);
                        break;
                    case 3://手机杀毒
                        startActivity(VirusScanActivity.class);
                        break;
                    case 4://缓存清理
                        startActivity(CacheClearListActivity.class);
                        break;
                    case 5://进程管理
                        startActivity(ProcessManagerActivity.class);
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
        //1.获取设备管理员
        PolicyManager = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        //2.申请权限.MyDeviceAdminReceiver继承自DeviceAdminReceiver
        componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
        //3.如果没有权限就申请权限
        boolean active = PolicyManager.isAdminActive(componentName);
        if(!active){
            //没有管理员权限，则获取管理员的权限
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"获取超级管理员权限，用于远程锁屏和清楚数据");
            startActivity(intent);
        }
    }

    /**
     * 弹出设置密码对话框
     */
    private void showSetUpPswDialog() {
        final SetUpPasswordDialog setUpPasswordDialog = new SetUpPasswordDialog(HomeActivity.this);
        setUpPasswordDialog.setCallBack(new com.application.lee.mobilesafe.chapter02.Dialog.SetUpPasswordDialog.MyCallBack(){

            @Override
            public void ok() {
                String firstPwsd = setUpPasswordDialog.mFirstPWDET.getText().toString().trim();
                String affirmPwsd = setUpPasswordDialog.mAffirmET.getText().toString().trim();
                if(!TextUtils.isEmpty(firstPwsd)&&!TextUtils.isEmpty(affirmPwsd)){
                    if(firstPwsd.equals(affirmPwsd)){
//                        两次密码一致，则存储密码
                        savePswd(affirmPwsd);
                        setUpPasswordDialog.dismiss();
//                        显示输入密码对话框
                        showInterPswdDialog();
                    }else{
                        Toast.makeText(HomeActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
                setUpPasswordDialog.dismiss();
            }
        });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();
    }

    /**
     * 保存密码
     * @param affirmPwsd
     */
    private void savePswd(String affirmPwsd) {
        SharedPreferences.Editor edit = msharedPreferences.edit();
//        为了防止用户隐私被泄漏，因此需要加密密码
        edit.putString("PhoneAntiTheftPWD", MD5Utils.encode(affirmPwsd));
        edit.commit();
    }

    /**
     * 判断用户是否设置过手机防盗密码
     */
    private boolean isSetUpPassword() {
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if(TextUtils.isEmpty(password)){
            return false;
        }
        return true;
    }

    /**
    * 弹出输入密码对话框
    */
    private void showInterPswdDialog() {
        final String password = getPassword();
        final InterPasswordDialog mInterPswdDialog = new InterPasswordDialog(HomeActivity.this);
        mInterPswdDialog.setCallBack(new InterPasswordDialog.MyCallBack() {
            @Override
            public void confirm() {
                if(TextUtils.isEmpty(mInterPswdDialog.getPassword())){
                    Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(password.equals(MD5Utils.encode(mInterPswdDialog.getPassword()))){
//                    进入防盗主页面
                    mInterPswdDialog.dismiss();
                    startActivity(LostFindActivity.class);
                }else{
                    Toast.makeText(HomeActivity.this,"密码有误，请重新输入",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
                mInterPswdDialog.dismiss();
            }
        });
        mInterPswdDialog.setCancelable(true);
        mInterPswdDialog.show();
    }

    private String getPassword() {
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if(TextUtils.isEmpty(password)){
            return "";
        }
        return password;
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
