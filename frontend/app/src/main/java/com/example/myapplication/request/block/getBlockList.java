package com.example.myapplication.request.block;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class getBlockList extends BaseGetRequest {
    public getBlockList(Callback callback, String id) {
        // 设置请求URL
        this.to("/api/block/get_blocklist");
        // 添加参数
        this.put("user_id", id);
        // 设置回调函数
        this.call(callback);
    }
}
