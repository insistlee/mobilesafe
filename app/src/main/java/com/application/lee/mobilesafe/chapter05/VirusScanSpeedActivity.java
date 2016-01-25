package com.application.lee.mobilesafe.chapter05;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter02.ContactSelectActivity;
import com.application.lee.mobilesafe.chapter02.adapter.ContactAdapter;
import com.application.lee.mobilesafe.chapter05.adapter.ScanVirusAdapter;
import com.application.lee.mobilesafe.chapter05.dao.AntiVirusDao;
import com.application.lee.mobilesafe.chapter05.entity.ScanAppInfo;
import com.application.lee.mobilesafe.chapter05.utils.MD5Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\22
 * 描  述   ：
 * 修订历史  ：
 */
public class VirusScanSpeedActivity extends Activity implements View.OnClickListener {
    private PackageManager pm;
    private SharedPreferences mSP;
    private boolean flag;
    private boolean isStop;
    private int process;
    private List<ScanAppInfo> mScanAppInfos = new ArrayList<ScanAppInfo>();
    private static final int SCAN_BENGIN=100;
    private int total;
    private static final int SCANNING=101;
    private static final int SCAN_FINISH=102;
    private TextView mProcessTV;
    private TextView mScanAppTV;
    private TextView mCancleBtn;
    private ListView mScanListView;
    private ScanVirusAdapter adapter;
    private ImageView mScanningIcon;
    private RotateAnimation rani;
    public static final String TAG = "VirusScanSpeedActivity.class";

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
           switch(msg.what){
               case SCAN_BENGIN:
                   mScanAppTV.setText("初始化杀毒引擎中...");
                   break;
               case SCANNING:
                   Log.i(TAG,"101是在扫描啊！");
                   Log.i(TAG,"101是在扫描啊！");
                   Log.i(TAG,"101是在扫描啊！");
                   Log.i(TAG,"101是在扫描啊！");
                   Log.i(TAG,"101是在扫描啊！");
                   Log.i(TAG,"101是在扫描啊！");
                   Log.i(TAG,"101是在扫描啊！");
                   Log.i(TAG,"101是在扫描啊！");
                   Log.i(TAG,"101是在扫描啊！");
                   Log.i(TAG,"101是在扫描啊！");
                   Log.i(TAG,"101是在扫描啊！");
                   ScanAppInfo info = (ScanAppInfo)msg.obj;
                   mScanAppTV.setText("正在扫描:"+info.appName);
                   int speed = msg.arg1;
                   mProcessTV.setText((speed*100/total)+"%");
                   mScanAppInfos.add(info);
                   adapter.notifyDataSetChanged();
                   mScanListView.setSelection(mScanAppInfos.size());
                   break;
               case SCAN_FINISH:
                   mScanAppTV.setText("扫描完成!");
                   mScanningIcon.clearAnimation();
                   mCancleBtn.setBackgroundResource(R.drawable.scan_complete);
                   saveScanTime();
                   break;
           }
        }

        private void saveScanTime() {
            SharedPreferences.Editor edit =mSP.edit();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = sdf.format(new Date());
            currentTime = "上次查杀:"+ currentTime;
            edit.putString("lastVirusScan",currentTime);
            edit.commit();
        }
    };
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virusscanspeed);
        pm=getPackageManager();
        mSP=getSharedPreferences("config",MODE_PRIVATE);
        initView();
        scanVirus();
    }

    /**
     * 扫描病毒
    */
    private void scanVirus() {
        flag = true;
        isStop = false;
        process = 0;
        mScanAppInfos.clear();
        new Thread(){
            public void run(){
                Message msg = Message.obtain();
                msg.what=SCAN_BENGIN;
                mHandler.sendMessage(msg);
                List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
                total = installedPackages.size();
                for(PackageInfo info:installedPackages){
                    if(!flag){
                        isStop=true;
                        return;
                    }
                    msg = Message.obtain();
                    String apkpath=info.applicationInfo.sourceDir;
                    //检查获取这个文件的特征码
                    String md5info = MD5Utils.getFileMd5(apkpath);
                    String result = AntiVirusDao.checkVirus(md5info);
                    msg.what=SCANNING;
                    ScanAppInfo scanInfo = new ScanAppInfo();
                    if(result==null){
                        scanInfo.description="扫描安全";
                        scanInfo.isVirus=false;
                    }else{
                        scanInfo.description=result;
                        scanInfo.isVirus=true;
                    }
                    process++;
                    scanInfo.packagename=info.packageName;
                    scanInfo.appName=info.applicationInfo.loadLabel(pm).toString();
                    scanInfo.appicon=info.applicationInfo.loadIcon(pm);
                    msg.obj=scanInfo;

                    msg.arg1=process;
                    mHandler.sendMessage(msg);
                   /* try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
                msg=Message.obtain();
                msg.what=SCAN_FINISH;
                mHandler.sendMessage(msg);
            };
        }.start();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        ImageView mLeftImgv= (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("病毒查杀进度");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mProcessTV= (TextView) findViewById(R.id.tv_scanprocess);
        mScanAppTV= (TextView) findViewById(R.id.tv_scansapp);
        mCancleBtn= (TextView) findViewById(R.id.btn_canclescan);
        mCancleBtn.setOnClickListener(this);
        mScanListView= (ListView) findViewById(R.id.lv_scanapps);
        adapter = new ScanVirusAdapter(mScanAppInfos,this);
        mScanListView.setAdapter(adapter);
        mScanningIcon = (ImageView) findViewById(R.id.imgv_scanningicon);
        startAnim();
    }

    private void startAnim() {
        if(rani==null){
            rani=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        }
        rani.setRepeatCount(Animation.INFINITE);
        rani.setDuration(2000);
        mScanningIcon.startAnimation(rani);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
            case R.id.btn_canclescan:
                if(process==total&process>0){
                    //扫描已完成
                    finish();
                }else if(process>0 & process<total & isStop==false){
                    mScanningIcon.clearAnimation();
                    //取消扫描
                    flag=false;
                    //更换背景图片
                    mCancleBtn.setBackgroundResource(R.drawable.restart_scan_btn);
                }else if(isStop){
                    startAnim();
                    //重新扫描
                    scanVirus();
                    //更换背景图片
                    mCancleBtn.setBackgroundResource(R.drawable.cancle_scan_btn_selector);
                }
                break;
        }
    }
    protected void onDestroy(){
        flag=false;
        super.onDestroy();
    }
}
