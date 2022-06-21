package com.example.myapplication.request.draft;

import com.example.myapplication.request.base.BaseGetRequest;
import okhttp3.Callback;

public class deleteDraft extends BaseGetRequest {
    public deleteDraft(Callback callback,String draft_id) {
        // 设置请求URL
        this.to("/api/draft/delete_draft");
        // 设置请求参数
        this.put("draft_id", draft_id);
        // 设置回调函数
        this.call(callback);
    }
}