package com.example.myapplication.utils;

import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.entity.NoticeInfo;
import com.example.myapplication.entity.UserInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicInfo {

    public static String mId;
    public static String mName;
    public static String mSingnature;
    public static String mAccount;
    public static String mPassword;
    public static String mAvatarUrl;
    public static List<UserInfo> mFollowList;
    public static int mFollowerNumber;
    public static List<UserInfo> mFollowedList;
    public static int mFollowedNumber;

    public static List<ShortProfile> WATCH_LIST = Collections.synchronizedList(new ArrayList<>());
    public static List<ShortProfile> FAN_LIST = Collections.synchronizedList(new ArrayList<>());

    public static List<NoticeInfo> mNoticeList;
    public static int mNoticeNumber;
    public static List<PostInfo> mMypost;
    public static int mMypostNumber;
}
