package com.example.myapplication.request.follow;
import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;


public class DeleteFromWatchRequest extends BaseGetRequest {
    public DeleteFromWatchRequest(Callback callback, String user_id, String fan_id) {
        // 设置请求URL
        this.to("/api/follow/delete_from_watch");
        // 设置请求参数
        this.put("user_id", user_id);
        this.put("fan_id", fan_id);
        // 设置回调函数
        this.call(callback);
    }
}
