package com.example.myapplication.request.testRequest;

import com.example.myapplication.request.base.BasePostRequest;

import okhttp3.Callback;

public class testPost extends BasePostRequest{
    public testPost(Callback callback, String type, String account, String password) {
        // 设置请求URL
        this.to("/test/post");
        // 设置请求参数
        this.put("type", type);
        this.put("account", account);
        this.put("password", password);
        // 设置回调函数
        this.call(callback);
    }
}
