package com.example.myapplication.request.follow;
import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Callback;

public class GetFanlistRequest extends BaseGetRequest {
    public GetFanlistRequest(Callback callback, String id) {
        // 设置请求URL
        this.to("/api/follow/get_fanlist");
        // 添加参数
        this.put("user_id", id);
        // 设置回调函数
        this.call(callback);
    }
}
