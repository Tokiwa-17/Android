package com.example.myapplication.entity;

public class UserInfo {
    public String nickname;
    public String avatarUrl;
    public String introduction;

    public UserInfo(String nickname, String avatarUrl, String introduction) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.introduction = introduction;
    }
}
