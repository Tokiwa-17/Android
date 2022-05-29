package com.example.myapplication.entity;

public class NoticeInfo {
    public String type;
    public String text;
    public boolean read;
    public String postId;

    public NoticeInfo(String type, String text, String postId, Boolean read){
        this.type = type;
        this.text = text;
        this.postId = postId;
        this.read = read;
    }
}
