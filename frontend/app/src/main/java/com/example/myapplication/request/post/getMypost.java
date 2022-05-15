package com.example.myapplication.request.post;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class getMypost extends BaseGetRequest {
    public getMypost(Callback callback, String id) {
        // 设置请求URL
        this.to("/api/post/get_mypost");
        // 设置请求参数
        this.put("user_id", id);
        // 设置回调函数
        this.call(callback);
    }
}