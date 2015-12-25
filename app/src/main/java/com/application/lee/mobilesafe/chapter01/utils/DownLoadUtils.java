package com.application.lee.mobilesafe.chapter01.utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 版  权   ：
 * 作  者   ：李晓锋
 * 创建日期  ：2015\12\10
 * 描  述   ：
 * 修订历史  ：
 */
public class DownLoadUtils {
    /**
     * 下载apk的方法
     *
     * @param url         下载文件的路径
     * @param targetFile  下载文件的本地路径
     * @param myCallBack  接口对象用于监听文件的下载状态
     */
    public void downapk(String url, String targetFile, final MyCallBack myCallBack) {
        //创建HttpUtils对象
        HttpUtils httpUtils = new HttpUtils();
        //调用HttpUtils下载的方法下载指定文件
        httpUtils.download(url, targetFile, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> fileResponseInfo) {
                myCallBack.onSuccess(fileResponseInfo);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                myCallBack.onFailure(e,s);
            }

            public void onloading(long total,long current,boolean isUploading){
                super.onLoading(total,current,isUploading);
                myCallBack.onloading(total,current,isUploading);
            }
        });

        }


    /**
     * 接口，用于监听下载状态的接口
     * */
    interface MyCallBack{
        /**下载成功时调用*/
        void onSuccess(ResponseInfo<File> fileResponseInfo);
        /**下载失败时调用*/
        void onFailure(HttpException e, String s);
        /**下载中调用*/
        void onloading(long total,long current,boolean isUploading);
    }

}
