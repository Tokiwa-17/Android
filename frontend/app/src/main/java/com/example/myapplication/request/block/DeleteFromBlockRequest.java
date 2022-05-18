package com.example.myapplication.request.block;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;


public class DeleteFromBlockRequest extends BaseGetRequest {
    public DeleteFromBlockRequest(Callback callback, String user_id, String block_id) {
        // 设置请求URL
        this.to("/api/block/delete_from_block");
        // 设置请求参数
        this.put("user_id", user_id);
        this.put("block_id", block_id);
        // 设置回调函数
        this.call(callback);
    }
}
