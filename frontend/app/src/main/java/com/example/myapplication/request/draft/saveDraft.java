package com.example.myapplication.request.draft;

import com.example.myapplication.request.base.BaseGetRequest;
import okhttp3.Callback;

public class saveDraft extends BaseGetRequest {
    public saveDraft(Callback callback, String user_id, String title, String text, String draft_id) {
        // 设置请求URL
        this.to("/api/draft/add_draft");
        // 设置请求参数
        this.put("user_id", user_id);
        this.put("title", title);
        this.put("text", text);
        this.put("draft_id", draft_id);
        // 设置回调函数
        this.call(callback);
    }
}
