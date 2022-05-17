package com.example.myapplication.utils;

import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.entity.NoticeInfo;
import com.example.myapplication.entity.UserInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    public static List<PostInfo> mTargetpost;
    public static int mTargetpostNumber;

    public static Lock lock = new ReentrantLock();

    public static void removeFromWatchList(String id) {
        lock.lock();
        int i = 0;
        for (ShortProfile shortProfile : WATCH_LIST) {
            if (shortProfile.id.equals(id)) break;
            i++;
        }
        if (i < WATCH_LIST.size()) WATCH_LIST.remove(i);
        lock.unlock();
    }

    public static void addToWatchList(ShortProfile shortProfile) {
        shortProfile.isFan = true;
        WATCH_LIST.add(shortProfile);
    }
}
