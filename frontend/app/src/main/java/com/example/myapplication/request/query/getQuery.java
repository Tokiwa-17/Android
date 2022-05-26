package com.example.myapplication.request.query;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class getQuery extends BaseGetRequest {
    public getQuery(Callback callback, String type, String query) {
        // 设置请求URL
        this.to("/api/post/get_query");
        // 设置请求参数
        this.put("query", query);
        this.put("type", type);
        // 设置回调函数
        this.call(callback);
    }
}