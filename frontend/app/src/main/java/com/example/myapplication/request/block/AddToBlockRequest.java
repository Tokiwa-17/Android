package com.example.myapplication.request.block;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class AddToBlockRequest extends BaseGetRequest {
    public AddToBlockRequest(Callback callback, String user_id, String block_id) {
        // 设置请求URL
        this.to("/api/block/add_to_block");
        // 设置请求参数
        this.put("user_id", user_id);
        this.put("block_id", block_id);
        // 设置回调函数
        this.call(callback);
    }
}
