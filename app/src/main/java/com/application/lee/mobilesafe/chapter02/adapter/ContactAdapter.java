package com.application.lee.mobilesafe.chapter02.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;
import com.application.lee.mobilesafe.chapter02.entity.ContactInfo;

import java.util.List;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\1\4
 * 描  述   ：
 * 修订历史  ：
 */
public class ContactAdapter extends BaseAdapter{
    private List<ContactInfo> contactInfos;
    private Context context;
    public ContactAdapter(List<ContactInfo> contactInfos,Context context){
        super();
        this.contactInfos=contactInfos;
        this.context=context;
    }
    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = View.inflate(context,R.layout.item_list_contact_select,null);
            holder = new ViewHolder();
            holder.mNameTV = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mPhoneTV = (TextView) convertView.findViewById(R.id.tv_phone);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mNameTV.setText(contactInfos.get(position).name);
        holder.mPhoneTV.setText(contactInfos.get(position).phone);
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        TextView mNameTV;
        TextView mPhoneTV;
    }

}
