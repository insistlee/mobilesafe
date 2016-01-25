package com.application.lee.mobilesafe.chapter04.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.lee.mobilesafe.App;
import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter04.AppManagerActivity;
import com.application.lee.mobilesafe.chapter04.entity.AppInfo;
import com.application.lee.mobilesafe.chapter04.utils.DensityUtil;
import com.application.lee.mobilesafe.chapter04.utils.EngineUtils;

import java.util.List;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\19
 * 描  述   ：
 * 修订历史  ：
 */
public class AppManagerAdapter extends BaseAdapter {
    private List<AppInfo> UserAppInfos;
    private List<AppInfo> SystemAppInfos;
    private Context context;
    public AppManagerAdapter(List<AppInfo> userAppInfos, List<AppInfo> systemAppInfos, Context context) {
        UserAppInfos = userAppInfos;
        SystemAppInfos = systemAppInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        //因为有两个条目要显示用户进程，因此系统进程需要加2
        return UserAppInfos.size()+SystemAppInfos.size()+2;
    }

    @Override
    public Object getItem(int position) {
        if(position==0){
            //第0个文职显示的应该是用户程序个数的标签
            return null;
        }else if(position==(UserAppInfos.size()+1)){
            return null;
        }
        AppInfo appInfo;
        if(position<(UserAppInfos.size()+1)){
            //用户程序
            appInfo = UserAppInfos.get(position-1);
        }else{
            int location = position - UserAppInfos.size() - 2;
            appInfo = SystemAppInfos.get(position);
        }
        return appInfo;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //如果position为0，则为TextView
        if(position==0){
            TextView tv = getTextView();
            tv.setText("用户程序:"+UserAppInfos.size()+"个");
            return tv;
        }else if(position==(UserAppInfos.size()+1)){//系统应用
            TextView tv = getTextView();
            tv.setText("系统程序:"+SystemAppInfos.size()+"个");
            return tv;
        }
        //获取当前app对象
        AppInfo appInfo;
        if(position<(UserAppInfos.size()+1)){
            //position 0 为TextView
            appInfo = UserAppInfos.get(position-1);
        }else{
            //系统应用
            appInfo = SystemAppInfos.get(position - UserAppInfos.size() - 2);
        }
        ViewHolder viewHolder = null;
        if(convertView!=null & convertView instanceof LinearLayout){
            viewHolder=(ViewHolder)convertView.getTag();
        }else{
            viewHolder=new ViewHolder();
            convertView = View.inflate(context,R.layout.item_appmanager_list,null);
            viewHolder.mAppIconImgv = (ImageView) convertView.findViewById(R.id.imgv_appicon);
            viewHolder.mAppLocationTV = (TextView) convertView.findViewById(R.id.tv_appisroom);
            viewHolder.mAppSizeTV = (TextView) convertView.findViewById(R.id.tv_appsize);
            viewHolder.mAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname);
            viewHolder.mLaunchAppTv = (TextView) convertView.findViewById(R.id.tv_launch_app);
            viewHolder.mSettinAppTV = (TextView) convertView.findViewById(R.id.tv_setting_app);
            viewHolder.mShareAppTV = (TextView) convertView.findViewById(R.id.tv_share_app);
            viewHolder.mUninatallTV = (TextView) convertView.findViewById(R.id.tv_uninstall_app);
            viewHolder.mAppOptionLL = (LinearLayout) convertView.findViewById(R.id.ll_option_app);
            convertView.setTag(viewHolder);
        }
        if(appInfo!=null){
            viewHolder.mAppLocationTV.setText(appInfo.getAppLocation(appInfo.isInRoom));
            viewHolder.mAppIconImgv.setImageDrawable(appInfo.icon);
            viewHolder.mAppSizeTV.setText(Formatter.formatFileSize(context,appInfo.appSize));
            viewHolder.mAppNameTV.setText(appInfo.appName);
            if(appInfo.isSelected){
                viewHolder.mAppOptionLL.setVisibility(View.VISIBLE);
            }else{
                viewHolder.mAppOptionLL.setVisibility(View.GONE);
            }
        }
        MyClickListener listener = new MyClickListener(appInfo);
        viewHolder.mLaunchAppTv.setOnClickListener(listener);
        viewHolder.mUninatallTV.setOnClickListener(listener);
        viewHolder.mShareAppTV.setOnClickListener(listener);
        viewHolder.mSettinAppTV.setOnClickListener(listener);
        return convertView;
    }

    /**
     * 创建一个TextView
     * @return
     */
    private TextView getTextView() {
        TextView tv = new TextView(context);
        tv.setBackgroundColor(context.getResources().getColor(R.color.graye5));
        tv.setPadding(DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5));
        tv.setTextColor(context.getResources().getColor(R.color.black));
        return tv;
    }

    static class ViewHolder {
        /**启动app*/
        TextView mLaunchAppTv;
        /**卸载app*/
        TextView mUninatallTV;
        /**分享app*/
        TextView mShareAppTV;
        /**设置app*/
        TextView mSettinAppTV;
        /**app图标*/
        ImageView mAppIconImgv;
        /**app位置*/
        TextView mAppLocationTV;
        /**app大小*/
        TextView mAppSizeTV;
        /**app名称*/
        TextView mAppNameTV;
        /**操作App的线性布局*/
        LinearLayout mAppOptionLL;
    }

    class MyClickListener implements View.OnClickListener{
        private AppInfo appInfo;
        public MyClickListener(AppInfo appInfo){
            super();
            this.appInfo = appInfo;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_launch_app:
                    //启动应用
                    EngineUtils.startApplication(context,appInfo);
                    break;
                case R.id.tv_share_app:
                    //分享应用
                    EngineUtils.startApplication(context, appInfo);
                    break;
                case R.id.tv_setting_app:
                    //设置应用
                    EngineUtils.SettingAppDetail(context,appInfo);
                    break;
                case R.id.tv_uninstall_app:
                    //卸载应用，需要注册广播接收者
                    if(appInfo.packageName.equals(context.getPackageName())){
                        Toast.makeText(context,"您没有权限卸载此应用",Toast.LENGTH_LONG).show();
                        return;
                    }
                    EngineUtils.uninstallApplication(context,appInfo);
                    break;
            }
        }
    }
}