package com.example.myapplication;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class testHttpGetRequest extends BaseGetRequest{
    public testHttpGetRequest(Callback callback, String msg) {
        // 设置请求URL
        this.to("/api/testHttpGetRequest");
        // 设置文件参数
        this.put("msg", msg);
        // 设置回调函数
        this.call(callback);
    }
}
