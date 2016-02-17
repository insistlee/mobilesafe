package com.application.lee.mobilesafe.chapter07;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter07.adapter.ProcessManagerAdapter;
import com.application.lee.mobilesafe.chapter07.entity.TaskInfo;
import com.application.lee.mobilesafe.chapter07.utils.SystemInfoUtils;
import com.application.lee.mobilesafe.chapter07.utils.TaskInfoParser;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
* 版  权   ：
* 作  者   ：李晓锋
* 创建日期  ：2016\1\29
* 描  述   ：
* 修订历史  ：
*/
public class ProcessManagerActivity extends Activity implements View.OnClickListener {
    private ProcessManagerAdapter adapter;
    private TextView mRunProcessNum;
    private TextView mMemoryTV;
    private TextView mProcessNumTV;
    private int runningProcessCount;
    private long totalMem;
    private ListView mListView;
    private List<TaskInfo> runningTaskInfos;
    private List<TaskInfo> userTaskInfos = new ArrayList<TaskInfo>();
    private List<TaskInfo> sysTaskInfo = new ArrayList<TaskInfo>();
    private ActivityManager manager;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_processmanager);
        initView();
        fillData();
    }

    protected void onResume(){
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }
    private void fillData() {
        userTaskInfos.clear();
        sysTaskInfo.clear();
        new Thread(){
            public void run(){
                runningTaskInfos  = TaskInfoParser.getRunningTaskInfos(getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(TaskInfo taskInfo:runningTaskInfos){
                            if(taskInfo.isUserApp){
                                userTaskInfos.add(taskInfo);
                            }else{
                                sysTaskInfo.add(taskInfo);
                            }
                        }
                        if(adapter==null){
                            adapter = new ProcessManagerAdapter(getApplicationContext(),userTaskInfos,sysTaskInfo);
                            mListView.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        if(userTaskInfos.size()>0){
                            mProcessNumTV.setText("用户进程:"+userTaskInfos.size()+"个");
                        }else{
                            mProcessNumTV.setText("系统进程:"+sysTaskInfo.size()+"个");
                        }
                    }
                });
            }
        }.start();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_green));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        ImageView mRightImgv = (ImageView) findViewById(R.id.imgv_rightbtn);
        mRightImgv.setImageResource(R.drawable.processmanager_setting_icon);
        mRightImgv.setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_title)).setText("进程管理");
        mRunProcessNum = (TextView) findViewById(R.id.tv_runningprocess_num);
        mMemoryTV = (TextView) findViewById(R.id.tv_memory_processmanager);
        mProcessNumTV = (TextView) findViewById(R.id.tv_user_runningprocess);
        runningProcessCount = SystemInfoUtils.getRunningProcessCount(ProcessManagerActivity.this);
        mRunProcessNum.setText("运行中的进程:"+runningProcessCount+"个");
        long totalAvailMem = SystemInfoUtils.getAvailMem(this);
        totalMem = SystemInfoUtils.getTotalMem();
        mMemoryTV.setText("可用/总内存:"+ android.text.format.Formatter.formatFileSize(this, totalAvailMem)+"/"+ android.text.format.Formatter.formatFileSize(this,totalMem));
        mListView = (ListView) findViewById(R.id.lv_runningapps);
        initListener();
    }

    private void initListener() {
        findViewById(R.id.btn_selectall).setOnClickListener(this);
        findViewById(R.id.btn_select_inverse).setOnClickListener(this);
        findViewById(R.id.btn_cleanprocess).setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = mListView.getItemAtPosition(position);
                if(object!=null&object instanceof TaskInfo){
                    TaskInfo info = (TaskInfo) object;
                    if(info.packageName.equals(getPackageName())){
                        //当前点击的条目是本应用程序
                        return;
                    }
                    info.isChecked=!info.isChecked;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem>=userTaskInfos.size()+1){
                    mProcessNumTV.setText("系统进程:"+sysTaskInfo.size()+"个");
                }else{
                    mProcessNumTV.setText("用户进程:"+userTaskInfos.size()+"个");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.imgv_rightbtn:
                //跳转至进程管理设置页面
                startActivity(new Intent(this,ProcessManagerSettingActivity.class));
                break;
            case R.id.btn_selectall:
                selectAll();
                break;
            case R.id.btn_select_inverse:
                inverse();
                break;
            case R.id.btn_cleanprocess:
                cleanProcess();
                break;
        }
    }

    /**
     * 清理进程
     */
    private void cleanProcess() {
        manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int count = 0;
        long saveMemory=0;
        List<TaskInfo> killedtaskInfos = new ArrayList<TaskInfo>();
        //遍历集合时不能改变集合的大小
        for(TaskInfo info:userTaskInfos){
            if(info.isChecked){
                count++;
                saveMemory+=info.appMemory;
                manager.killBackgroundProcesses(info.packageName);
                killedtaskInfos.add(info);
            }
        }
        for(TaskInfo info:sysTaskInfo){
            if(info.isChecked){
                count++;
                saveMemory+=info.appMemory;
                manager.killBackgroundProcesses(info.packageName);
                killedtaskInfos.add(info);
            }
        }
        for(TaskInfo info:killedtaskInfos){
            if(info.isUserApp){
                userTaskInfos.remove(info);
            }else{
                sysTaskInfo.remove(info);
            }
        }
        runningProcessCount-=count;
        mRunProcessNum.setText("运行中的进程:"+runningProcessCount+"个");
        mMemoryTV.setText("可用/总内存:" + android.text.format.Formatter.formatFileSize(this, SystemInfoUtils.getAvailMem(this)) + "/" + android.text.format.Formatter.formatFileSize(this, totalMem));
        Toast.makeText(this,"清理了"+count+"个进程，释放了"+android.text.format.Formatter.formatFileSize(this,saveMemory)+"内存",Toast.LENGTH_LONG).show();
        mProcessNumTV.setText("用户进程"+userTaskInfos.size()+"个");
        adapter.notifyDataSetChanged();
    }

    /**
     * 反选
     */
    private void inverse() {
        for(TaskInfo taskInfo:userTaskInfos){
            //就是本应用程序
            if(taskInfo.packageName.equals(getPackageName())){
                continue;
            }
            boolean checked = taskInfo.isChecked;
            taskInfo.isChecked=!checked;
        }
        for(TaskInfo taskInfo:sysTaskInfo){
            boolean checked = taskInfo.isChecked;
            taskInfo.isChecked=!checked;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     *
    */
    private void selectAll() {
        for(TaskInfo taskInfo:userTaskInfos){
            //就是本应用程序
            if(taskInfo.packageName.equals(getPackageName())){
                continue;
            }
            taskInfo.isChecked=true;
        }
        for(TaskInfo taskInfo:sysTaskInfo){
            taskInfo.isChecked=true;
        }
        adapter.notifyDataSetChanged();
    }
}
