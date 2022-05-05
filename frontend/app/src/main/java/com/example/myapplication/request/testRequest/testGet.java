package com.example.myapplication.request.testRequest;

import com.example.myapplication.request.base.BaseGetRequest;
import com.example.myapplication.utils.Global;

import okhttp3.Callback;

public class testGet extends BaseGetRequest {
    // 从后端获取字符串数据
    public testGet(Callback callback) {
        // 设置请求URL
        this.to("/test");
        // 设置请求参数
        this.put("param", "123");
        // 设置回调函数
        this.call(callback);
    }
}
