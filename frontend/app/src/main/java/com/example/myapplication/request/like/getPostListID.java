package com.example.myapplication.request.like;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class getPostListID extends BaseGetRequest {
    public getPostListID(Callback callback, String num) {
        // 设置请求URL
        this.to("/api/post/get_post_list_id");
        // 设置请求参数
        this.put("num", num);
        // 设置回调函数
        this.call(callback);
    }
}
