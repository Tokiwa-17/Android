package com.example.myapplication.entity;

public class PostInfo {
    public String nickname;
    public String avatarUrl;
    public String title;
    public String text;

    public PostInfo(String nickname, String avatarUrl, String title, String text) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.title = title;
        this.text = text;
    }
}
