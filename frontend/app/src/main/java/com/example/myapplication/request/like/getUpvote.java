package com.example.myapplication.request.like;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class getUpvote extends BaseGetRequest {
    public getUpvote(Callback callback, String id) {
        // 设置请求URL
        this.to("/api/like/get_upvote");
        // 设置请求参数
        this.put("post_id_list", id);
        // 设置回调函数
        this.call(callback);
    }
}
