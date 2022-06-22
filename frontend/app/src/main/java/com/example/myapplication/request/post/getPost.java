package com.example.myapplication.request.post;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class getPost extends BaseGetRequest {
    public getPost(Callback callback, String post_id) {
        // 设置请求URL
        this.to("/api/post/post_detail");
        // 设置请求参数
        this.put("post_id", post_id);
        // 设置回调函数
        this.call(callback);
    }
}