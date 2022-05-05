package com.example.myapplication.request.base;

import java.io.File;
import java.util.HashMap;

import okhttp3.Callback;

import com.example.myapplication.utils.Valid;
import com.example.myapplication.utils.Http;

public class BasePostRequest {
    private String url = null;                                  // URL
    private HashMap<String, String> param = new HashMap<>();    // 请求参数
    private String fileKey = null;                              // 文件键值
    private File fileObject = null;                             // 文件对象
    private Callback callback;                                  // 回调函数

    /**
     * 设置请求URL
     *
     * @param url {String} 请求URL
     */
    protected void to(String url) {
        this.url = url;
    }

    /**
     * 添加请求参数
     *
     * @param key   {String} 参数 键
     * @param value {String>} 参数 值
     */
    protected void put(String key, String value) {
        if (!Valid.isBlank(key) && !Valid.isBlank(value)) {
            this.param.put(key, value);
        }
    }

    /**
     * 加载文件参数
     *
     * @param fileKey    {String} 文件 键值
     * @param fileObject {String>} 文件 对象
     */
    protected void load(String fileKey, File fileObject) {
        if (!Valid.isBlank(fileKey) && fileObject != null) {
            this.fileKey = fileKey;
            this.fileObject = fileObject;
        }
    }

    /**
     * 设置回调函数
     *
     * @param callback {Callback} 回调函数
     */
    protected void call(okhttp3.Callback callback) {
        this.callback = callback;
    }

    /**
     * 发送请求
     */
    public void send() {
        Http.sendHttpPostRequest(this.url, this.param, this.fileKey, this.fileObject, this.callback);
    }
}
