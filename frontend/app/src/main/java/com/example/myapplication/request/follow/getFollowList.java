package com.example.myapplication.request.follow;
import com.example.myapplication.request.base.BaseGetRequest;
import com.example.myapplication.request.base.BasePostRequest;

import okhttp3.Callback;

public class getFollowList extends BaseGetRequest {
    public getFollowList(Callback callback, String id) {
        // 设置请求URL
        this.to("/api/follow/get_follow_list");
        // 设置请求参数
        this.put("user_id", id);
        // 设置回调函数
        this.call(callback);
    }
}
