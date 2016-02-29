package com.application.lee.mobilesafe.chapter09.Fragment;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter09.adapter.AppLockAdapter;
import com.application.lee.mobilesafe.chapter09.db.dao.AppLockDao;
import com.application.lee.mobilesafe.chapter09.entity.AppInfo;
import com.application.lee.mobilesafe.chapter09.utils.AppInfoParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\25
 * 描  述   ：
 * 修订历史  ：
 */
public class AppLockFragment extends Fragment {
    private TextView mLockTV;
    private ListView mLockLV;
    private AppLockDao dao;
    List<AppInfo> mLockApps = new ArrayList<AppInfo>();
    private AppLockAdapter adapter;
    private Uri uri = Uri.parse("content://com.application.lee.mobilesafe.applock");
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch(msg.what){
                case 10:
                    mLockApps.clear();
                    mLockApps.addAll((List<AppInfo>)msg.obj);
                    if(adapter==null){
                        adapter = new AppLockAdapter(mLockApps,getActivity());
                        mLockLV.setAdapter(adapter);
                    }else{
                        adapter.notifyDataSetChanged();
                    }
                    mLockTV.setText("加锁应用"+mLockApps.size()+"个");
                    break;
            }
        }
    };

    private List<AppInfo> appInfos;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_applock,null);
        mLockTV = (TextView) view.findViewById(R.id.tv_lock);
        mLockLV = (ListView) view.findViewById(R.id.lv_lock);
        return view;
    }

    public void onResume(){
        dao = new AppLockDao(getActivity());
        appInfos = AppInfoParser.getAppInfos(getActivity());
        fillData();
        initListener();
        getActivity().getContentResolver().registerContentObserver(uri,true,new ContentObserver(new Handler()){
            public void onChange(boolean selfChange){
                fillData();
            }
        });
        super.onResume();
    }

    private void initListener() {
        mLockLV.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //播放一个动画效果
                TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
                ta.setDuration(300);
                view.startAnimation(ta);
                new Thread(){
                    public void run(){
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //删除数据库包名
                                dao.delete(mLockApps.get(position).packageName);
                                //更新界面
                                mLockApps.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                };
            }
        });
    }

    private void fillData() {
        final List<AppInfo> aInfos = new ArrayList<AppInfo>();
        new Thread(){
            public void run(){
                for(AppInfo appInfo:appInfos){
                    if(dao.find(appInfo.packageName)){
                        //已加锁
                        appInfo.isLock = true;
                        aInfos.add(appInfo);
                    }
                }
                Message msg = new Message();
                msg.obj=aInfos;
                msg.what=10;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
}
