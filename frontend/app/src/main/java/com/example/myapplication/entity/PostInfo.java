package com.example.myapplication.entity;

public class PostInfo {
    public String postId;
    public String userId;
    public String nickname;
    public String avatarUrl;
    public String title;
    public String text;
    public int like;
    public String time;
    public String likeName;

    public String imageUrl;

    public PostInfo(String nickname, String avatarUrl, String title, String text) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.title = title;
        this.text = text;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PostInfo(String nickname, String avatarUrl, String title, String text, String imageUrl) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public PostInfo(String postId, String userId, String nickname, String avatarUrl, String title, String text, int like, String time) {
        this.postId = postId;
        this.userId = userId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.title = title;
        this.text = text;
        this.like = like;
        this.time = time;
    }

    public PostInfo(String postId, String userId, String nickname, String avatarUrl, String title, String text, int like, String time, String imageUrl) {
        this.postId = postId;
        this.userId = userId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.title = title;
        this.text = text;
        this.like = like;
        this.time = time;
        this.imageUrl = imageUrl;
    }

}
