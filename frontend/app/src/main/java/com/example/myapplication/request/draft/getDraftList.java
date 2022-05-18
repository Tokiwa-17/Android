package com.example.myapplication.request.draft;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class getDraftList extends BaseGetRequest {
    public getDraftList(Callback callback, String id) {
        // 设置请求URL
        this.to("/api/draft/get_draft");
        // 设置请求参数
        this.put("user_id", id);
        // 设置回调函数
        this.call(callback);
    }
}