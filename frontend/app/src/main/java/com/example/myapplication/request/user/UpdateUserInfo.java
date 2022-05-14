package com.example.myapplication.request.user;

import com.example.myapplication.request.base.BasePostRequest;

import okhttp3.Callback;

public class UpdateUserInfo extends BasePostRequest {
    public UpdateUserInfo(Callback callback, String id, String nickname, String password, String intro) {
        // 设置请求URL
        this.to("/api/user/update_user_info");
        // 设置请求参数
        this.put("id", id);
        this.put("nickname", nickname);
        this.put("password", password);
        this.put("introduction", intro);
        // 设置回调函数
        this.call(callback);
    }
}