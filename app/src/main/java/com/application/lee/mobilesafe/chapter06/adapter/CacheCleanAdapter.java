package com.application.lee.mobilesafe.chapter06.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter06.entity.CacheInfo;

import java.util.List;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\26
 * 描  述   ：
 * 修订历史  ：
 */
public class CacheCleanAdapter extends BaseAdapter {
    private Context context;
    private List<CacheInfo> cacheInfos;
    public CacheCleanAdapter(Context context,List<CacheInfo> cacheInfos){
        super();
        this.context = context;
        this.cacheInfos = cacheInfos;
    }
    @Override
    public int getCount() {
        return cacheInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return cacheInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_cacheclean_list,null);
            holder.mAppIconImgv = (ImageView) convertView.findViewById(R.id.imgv_appicon_cacheclean);
            holder.mAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname_cacheclean);
            holder.mCacheSizeTV = (TextView) convertView.findViewById(R.id.tv_appsize_cacheclean);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        CacheInfo cacheInfo = cacheInfos.get(position);
        holder.mAppIconImgv.setImageDrawable(cacheInfo.appIcon);
        holder.mAppNameTV.setText(cacheInfo.appName);
        holder.mCacheSizeTV.setText(Formatter.formatFileSize(context,cacheInfo.cacheSize));
        return convertView;
    }

    static class ViewHolder{
        ImageView mAppIconImgv;
        TextView mAppNameTV;
        TextView mCacheSizeTV;
    }
}
