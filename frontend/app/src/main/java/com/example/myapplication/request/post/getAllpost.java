package com.example.myapplication.request.post;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class getAllpost extends BaseGetRequest {
    public getAllpost(Callback callback,  String num) {
        // 设置请求URL
        this.to("/api/post/get_allpost");
        // 设置请求参数
        this.put("num", num);
        // 设置回调函数
        this.call(callback);
    }
}