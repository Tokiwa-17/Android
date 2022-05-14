package com.example.myapplication.request.follow;

import com.example.myapplication.request.base.BaseGetRequest;

import okhttp3.Call;
import okhttp3.Callback;

public class GetWatchlistRequest extends BaseGetRequest {
    public GetWatchlistRequest(Callback callback, String id) {
        // 设置请求URL
        this.to("/api/follow/get_watchlist");
        // 添加参数
        this.put("user_id", id);
        // 设置回调函数
        this.call(callback);
    }
}
