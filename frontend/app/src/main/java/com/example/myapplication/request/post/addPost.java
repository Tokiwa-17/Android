package com.example.myapplication.request.post;

import com.example.myapplication.request.base.BaseGetRequest;
import okhttp3.Callback;

public class addPost extends BaseGetRequest {
    public addPost(Callback callback, String mId, String title, String text) {
        // 设置请求URL
        this.to("/api/post/add_post");
        // 设置请求参数
        this.put("id", mId);
        this.put("title", title);
        this.put("text", text);
        // 设置回调函数
        this.call(callback);
    }
}