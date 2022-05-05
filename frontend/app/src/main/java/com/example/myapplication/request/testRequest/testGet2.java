package com.example.myapplication.request.testRequest;

import okhttp3.Callback;
import com.example.myapplication.request.base.BaseGetRequest;

public class testGet2 extends BaseGetRequest {
    // 从后端获取字符串数据
    public testGet2(Callback callback) {
        // 设置请求URL
        this.to("/test2");
        // 设置请求参数
        this.put("id", "123");
        // 设置回调函数
        this.call(callback);
    }
}
