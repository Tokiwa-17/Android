package com.example.myapplication.request.post;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class getWatchpost extends BaseGetRequest {
    public getWatchpost(Callback callback, String mId, String num) {
        // 设置请求URL
        this.to("/api/post/get_watchpost");
        // 设置请求参数
        this.put("num", num);
        this.put("id", mId);
        // 设置回调函数
        this.call(callback);
    }
}
