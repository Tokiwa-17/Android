package com.example.myapplication.request.user;

import android.util.Log;

import com.example.myapplication.request.base.BasePostRequest;

import okhttp3.Callback;

public class LogonRequest extends BasePostRequest {
    public LogonRequest(Callback callback, String account, String password) {
        // 设置请求URL
        this.to("/api/user/logon");
        // 设置请求参数
        this.put("account", account);
        this.put("password", password);
        // 设置回调函数
        this.call(callback);
    }
}
