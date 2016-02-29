package com.application.lee.mobilesafe.chapter09;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter09.Fragment.AppLockFragment;
import com.application.lee.mobilesafe.chapter09.Fragment.AppUnLockFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\25
 * 描  述   ：
 * 修订历史  ：
 */
public class AppLockActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager mAppViewPager;
    List<Fragment> mFragments = new ArrayList<Fragment>();
    private TextView mLockTV;
    private TextView mUnlockTV;
    private View slideLockView;
    private View slideUnLockView;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_applock);
        initView();
        initListener();
    }

    private void initListener() {
        mAppViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageSelected(int i) {
                if(i==0){
                    slideUnLockView.setBackgroundResource(R.drawable.slide_view);
                    slideLockView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    //未加锁
                    mLockTV.setTextColor(getResources().getColor(R.color.black));
                    mUnlockTV.setTextColor(getResources().getColor(R.color.bright_red));
                }else{
                    slideLockView.setBackgroundResource(R.drawable.slide_view);
                    slideUnLockView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    //已加锁
                    mLockTV.setTextColor(getResources().getColor(R.color.bright_red));
                    mUnlockTV.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("程序锁");
        mLeftImgv.setOnClickListener(this);
        mAppViewPager= (ViewPager) findViewById(R.id.vp_applock);
        mLockTV= (TextView) findViewById(R.id.tv_lock);
        mUnlockTV= (TextView) findViewById(R.id.tv_unlock);
        mLockTV.setOnClickListener(this);
        mUnlockTV.setOnClickListener(this);
        slideLockView= findViewById(R.id.view_slide_lock);
        slideUnLockView= findViewById(R.id.view_slide_unlock);
        AppUnLockFragment unLock = new AppUnLockFragment();
        AppLockFragment lock = new AppLockFragment();
        mFragments.add(unLock);
        mFragments.add(lock);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.tv_lock:
                mAppViewPager.setCurrentItem(1);
                break;
            case R.id.tv_unlock:
                mAppViewPager.setCurrentItem(0);
                break;
        }
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
