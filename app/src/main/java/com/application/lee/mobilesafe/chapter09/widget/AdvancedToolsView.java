package com.application.lee.mobilesafe.chapter09.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.lee.mobilesafe.R;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2016\2\22
 * 描  述   ：
 * 修订历史  ：
 */
public class AdvancedToolsView extends RelativeLayout {
    private TextView mDesriptionTV;
    private String desc="";
    private Drawable drawable;
    private ImageView mLeftImgv;

    public AdvancedToolsView(Context context) {
        super(context);
        init(context);
    }

    /**
     * 控件初始化
     * @param context
    */
    private void init(Context context) {
        //将资源转化成view对象显示在自己身上
        View view = View.inflate(context,R.layout.ui_advancedtools_view,null);
        this.addView(view);
        mDesriptionTV = (TextView) findViewById(R.id.tv_decription);
        mLeftImgv = (ImageView) findViewById(R.id.imgv_left);
        mDesriptionTV.setText(desc);
        if(drawable!=null){
            mLeftImgv.setImageDrawable(drawable);
        }
    }

    public AdvancedToolsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取到属性对象
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.AdvancedToolsView);
        //获取到desc属性，与attrs.xml中定义的属性绑定
        desc  = mTypedArray.getString(R.styleable.AdvancedToolsView_desc);
        //获取到android:src属性，与attrs.xml中定义的属性绑定
        drawable = mTypedArray.getDrawable(R.styleable.AdvancedToolsView_android_src);
        //回收属性对象
        mTypedArray.recycle();
        init(context);
    }

    public AdvancedToolsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
}
