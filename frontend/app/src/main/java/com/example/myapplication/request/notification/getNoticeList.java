package com.example.myapplication.request.notification;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class getNoticeList extends BaseGetRequest {
    public getNoticeList(Callback callback, String id) {
        // 设置请求URL
        this.to("/api/notice/get_notice_list");
        // 设置请求参数
        this.put("user_id", id);
        // 设置回调函数
        this.call(callback);
    }
}