package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.MyService;
import com.example.myapplication.R;
import com.example.myapplication.adapter.MainActivityPagerAdapter;
import com.example.myapplication.entity.NoticeInfo;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.fragment.main.DashboardFragment;
import com.example.myapplication.request.base.BaseGetRequest;
import com.example.myapplication.request.block.getBlockList;
import com.example.myapplication.request.draft.getDraftList;
import com.example.myapplication.request.follow.GetFanlistRequest;
import com.example.myapplication.request.follow.GetWatchlistRequest;
import com.example.myapplication.request.follow.getFollowList;
import com.example.myapplication.request.follow.getFollowedList;
import com.example.myapplication.request.like.getPostListID;
import com.example.myapplication.request.notification.getNoticeList;
import com.example.myapplication.request.post.getAllpost;
import com.example.myapplication.request.post.getMypost;
import com.example.myapplication.request.post.getWatchpost;
import com.example.myapplication.request.testRequest.testGet;
import com.example.myapplication.request.testRequest.testGet2;
import com.example.myapplication.request.testRequest.testPost;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.Global;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    private ViewPager viewPager;
    private MainActivityPagerAdapter mMainActivityPagerAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("消息列表轮询", "+1");
            mHandler.postDelayed(this,  10 * 1000);
            refreshData();
        }
    };
    int count = 0;
    private static final int ID = 1;
    private static final String CHANNELID ="1";
    private static final String CHANNELNAME = "channel1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTimeCounterRunnable.run();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_follow:
                    viewPager.setCurrentItem(1);
                    break;
//                case R.id.navigation_conversations:
//                    viewPager.setCurrentItem(2);
//                    break;
                case R.id.navigation_draft:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(3);
                    break;
            }
            return true;
        });

        viewPager = findViewById(R.id.nav_host_fragment);
        mMainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mMainActivityPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        break;
                    case 1:
                        navView.getMenu().findItem(R.id.navigation_follow).setChecked(true);
                        break;
//                    case 2:
//                        navView.getMenu().findItem(R.id.navigation_conversations).setChecked(true);
//                        break;
                    case 2:
                        navView.getMenu().findItem(R.id.navigation_draft).setChecked(true);
                        break;
                    default:
                        ((DashboardFragment) mMainActivityPagerAdapter.getRegisteredFragment(4)).changeFocus();
                        navView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
                        break;
                }
            }
        });
    }

    public void launchPostActivity(View view) {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }
    private synchronized void addCounter() {
        count++;

    }
    private okhttp3.Callback handleFollowedList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE) {
                        Log.e("HttpResponse", responseBodyString);
                    }
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("followed_list");
                    if(BasicInfo.mFollowedList != null) {
                        BasicInfo.mFollowedList.clear();
                    }
                    BasicInfo.mFollowedNumber = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
                        String nickname = subJsonObject.getString("nickname");
                        String avatar = subJsonObject.getString("avatar");
                        String intro = subJsonObject.getString("introduction");
                        UserInfo user = new UserInfo(nickname, avatar, intro);
                        BasicInfo.mFollowedList.add(user);
                    }
                }
            } catch (Exception e) {
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleFollowList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE) {
                        Log.e("HttpResponse", responseBodyString);
                    }
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("follow_list");
                    if(BasicInfo.mFollowList != null) {
                        BasicInfo.mFollowList.clear();
                    }
                    BasicInfo.mFollowerNumber = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
                        String nickname = subJsonObject.getString("nickname");
                        String avatar = subJsonObject.getString("avatar");
                        String intro = subJsonObject.getString("introduction");
                        UserInfo user = new UserInfo(nickname, avatar, intro);
                        BasicInfo.mFollowList.add(user);
                    }
                }
            } catch (Exception e) {
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback getWatchListCallback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("error1", e.toString());
            addCounter();
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String resStr = response.body().string();
            Log.e("response", resStr);
            try {
                JSONObject jsonObject = new JSONObject(resStr);
                JSONArray jsonArray;
                jsonArray = (JSONArray) jsonObject.get("watchlist");
                if(BasicInfo.WATCH_LIST.isEmpty() == false) {
                    BasicInfo.WATCH_LIST.clear();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i));
                    BasicInfo.WATCH_LIST.add(shortProfile);
                }
                addCounter();
            } catch (JSONException e) {
                addCounter();
                Log.e("error2", e.toString());
            }

        }
    };

    private okhttp3.Callback getFollowListCallback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("error1", e.toString());
            addCounter();
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String resStr = response.body().string();
            Log.e("response", resStr);
            try {
                JSONObject jsonObject = new JSONObject(resStr);
                JSONArray jsonArray;
                jsonArray = (JSONArray) jsonObject.get("fanlist");
                if(BasicInfo.FAN_LIST.isEmpty() == false) {
                    BasicInfo.FAN_LIST.clear();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i));
                    BasicInfo.FAN_LIST.add(shortProfile);
                }
                addCounter();
            } catch (JSONException e) {
                addCounter();
                Log.e("error2", e.toString());
            }

        }
    };

    private okhttp3.Callback getBlockListCallback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("error1", e.toString());
            addCounter();
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String resStr = response.body().string();
            Log.e("response", resStr);
            try {
                JSONObject jsonObject = new JSONObject(resStr);
                JSONArray jsonArray;
                jsonArray = (JSONArray) jsonObject.get("blocklist");
                if(BasicInfo.BLOCK_LIST.isEmpty() == false) {
                    BasicInfo.BLOCK_LIST.clear();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i));
                    BasicInfo.BLOCK_LIST.add(shortProfile);
                }
                addCounter();
            } catch (JSONException e) {
                addCounter();
                Log.e("error2", e.toString());
            }

        }
    };

    private okhttp3.Callback handleNoticeList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE) {
                        Log.e("HttpResponse", responseBodyString);
                    }
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("notice_list");
                    if(BasicInfo.mNoticeList != null) {
                        BasicInfo.mNoticeList.clear();
                    }
                    BasicInfo.mNoticeNumber = jsonArray.length();
                    BasicInfo.mNoticeList = new LinkedList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
                        String type = subJsonObject.getString("type");
                        String text = subJsonObject.getString("text");
                        NoticeInfo notice = new NoticeInfo(type, text);
                        BasicInfo.mNoticeList.add(notice);
                    }
                }
            } catch (Exception e) {
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleMypostList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE) {
                        Log.e("HttpResponse", responseBodyString);
                    }
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("post_list");
                    if(BasicInfo.mMypost != null) {
                        BasicInfo.mMypost.clear();
                    }
                    BasicInfo.mMypostNumber = jsonArray.length();
                    BasicInfo.mMypost = new LinkedList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
                        String title = subJsonObject.getString("title");
                        String text = subJsonObject.getString("text");
                        String name = subJsonObject.getString("name");
                        String avatar_url = subJsonObject.getString("avatar_url");
                        int like = subJsonObject.getInt("like");
                        String time = subJsonObject.getString("time");
                        String postId = subJsonObject.getString("postId");
                        String userId = subJsonObject.getString("userId");
                        PostInfo post = new PostInfo(postId, userId, name,avatar_url,title, text,like,time);
                        BasicInfo.mMypost.add(post);
                    }
                }
            } catch (Exception e) {
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleAllpostList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE) {
                        Log.e("HttpResponse", responseBodyString);
                    }
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("all_post_list");
                    if(BasicInfo.mPostList != null) {
                        BasicInfo.mPostList.clear();
                    }
                    BasicInfo.mPostNumber = jsonArray.length();
                    BasicInfo.mPostList = new LinkedList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
                        String title = subJsonObject.getString("title");
                        String text = subJsonObject.getString("text");
                        String name = subJsonObject.getString("name");
                        String avatar_url = subJsonObject.getString("avatar_url");
                        int like = subJsonObject.getInt("like");
                        String time = subJsonObject.getString("time");
                        String postId = subJsonObject.getString("postId");
                        String userId = subJsonObject.getString("userId");
                        PostInfo post = new PostInfo(postId, userId, name,avatar_url,title, text,like,time);
                        BasicInfo.mPostList.add(post);
                    }
                }
            } catch (Exception e) {
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleWatchpostList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE) {
                        Log.e("HttpResponse", responseBodyString);
                    }
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("watch_post_list");
                    if(BasicInfo.mWatchPost != null) {
                        BasicInfo.mWatchPost.clear();
                    }
                    BasicInfo.mWatchPostNumber = jsonArray.length();
                    BasicInfo.mWatchPost = new LinkedList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
                        String title = subJsonObject.getString("title");
                        String text = subJsonObject.getString("text");
                        String name = subJsonObject.getString("name");
                        String avatar_url = subJsonObject.getString("avatar_url");
                        int like = subJsonObject.getInt("like");
                        String time = subJsonObject.getString("time");
                        String postId = subJsonObject.getString("postId");
                        String userId = subJsonObject.getString("userId");
                        PostInfo post = new PostInfo(postId, userId, name,avatar_url,title, text,like,time);
                        BasicInfo.mWatchPost.add(post);
                    }
                }
            } catch (Exception e) {
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleDraftList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE) {
                        Log.e("HttpResponse", responseBodyString);
                    }
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("draft_list");
                    if(BasicInfo.mDraftlist != null) {
                        BasicInfo.mDraftlist.clear();
                    }
                    BasicInfo.mDraftNumber = jsonArray.length();
                    BasicInfo.mDraftlist = new LinkedList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
                        String title = subJsonObject.getString("title");
                        String text = subJsonObject.getString("text");
                        String name = subJsonObject.getString("name");
                        String avatar_url = subJsonObject.getString("avatar_url");
                        int like = subJsonObject.getInt("like");
                        String time = subJsonObject.getString("time");
                        String postId = subJsonObject.getString("postId");
                        String userId = subJsonObject.getString("userId");
                        PostInfo post = new PostInfo(postId, userId, name,avatar_url,title, text,like,time);
                        BasicInfo.mDraftlist.add(post);

                    }
                }
            } catch (Exception e) {
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleUpvoteList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE) {
                        Log.e("HttpResponse", responseBodyString);
                    }
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("upvote_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
                        String postId = subJsonObject.getString("post_id");
                        String userName = subJsonObject.getString("user_name");
                        for (int j = 0; j < BasicInfo.mPostList.size(); j++) {

                        }
                    }
//                    if(BasicInfo.mDraftlist != null) {
//                        BasicInfo.mDraftlist.clear();
//                    }
//                    BasicInfo.mDraftNumber = jsonArray.length();
//                    BasicInfo.mDraftlist = new LinkedList<>();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
//                        String title = subJsonObject.getString("title");
//                        String text = subJsonObject.getString("text");
//                        PostInfo post = new PostInfo(BasicInfo.mName,BasicInfo.mAvatarUrl,title, text);
//                        BasicInfo.mDraftlist.add(post);
//                    }
                }
            } catch (Exception e) {
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handlePostListID = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE) {
                        Log.e("HttpResponse", responseBodyString);
                    }
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("upvote_list");
                    for (int j = 0; j < BasicInfo.mPostList.size(); j++) {
                        BasicInfo.mPostList.get(j).likeName = "";
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
                        String postId = subJsonObject.getString("post_id");
                        String userName = subJsonObject.getString("user_name");
                        for (int j = 0; j < BasicInfo.mPostList.size(); j++) {
                            if (postId.equals(BasicInfo.mPostList.get(j).postId)) {
                                BasicInfo.mPostList.get(j).likeName += (userName + " ");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    public void createNotice(String title, String text){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // manager.cancel(1);
        //安卓8.0以上弹出通知需要添加渠道NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建渠道
            /**
             * importance:用于表示渠道的重要程度。这可以控制发布到此频道的中断通知的方式。
             * 有以下6种重要性，是NotificationManager的静态常量，依次递增:
             * IMPORTANCE_UNSPECIFIED（值为-1）意味着用户没有表达重要性的价值。此值用于保留偏好设置，不应与实际通知关联。
             * IMPORTANCE_NONE（值为0）不重要的通知：不会在阴影中显示。
             * IMPORTANCE_MIN（值为1）最低通知重要性：只显示在阴影下，低于折叠。这不应该与Service.startForeground一起使用，因为前台服务应该是用户关心的事情，所以它没有语义意义来将其通知标记为最低重要性。如果您从Android版本O开始执行此操作，系统将显示有关您的应用在后台运行的更高优先级通知。
             * IMPORTANCE_LOW（值为2）低通知重要性：无处不在，但不侵入视觉。
             * IMPORTANCE_DEFAULT （值为3）：默认通知重要性：随处显示，产生噪音，但不会在视觉上侵入。
             * IMPORTANCE_HIGH（值为4）更高的通知重要性：随处显示，造成噪音和窥视。可以使用全屏的Intent。
             */
            NotificationChannel channel = new NotificationChannel(CHANNELID, CHANNELNAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);//开启渠道
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNELID);
            builder.setContentTitle(title)//通知标题
                    .setContentText(text)//通知内容
                    .setWhen(System.currentTimeMillis())//通知显示时间
                    .setSmallIcon(R.drawable.ic_baseline_add_24)
                    .setAutoCancel(true)//点击通知取消
                    //.setSound()
                    //第一个参数为手机静止时间，第二个参数为手机震动时间，周而复始
                    .setVibrate(new long[]{0, 1000, 1000, 1000})//手机震动
                    /**表示通知的重要程度
                     * RIORITY_DEFAULT
                     * RIORITY_MIN
                     * RIORITY_LOW
                     * RIORITY_HIGE
                     * RIORITY_MAX
                     **/
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            manager.notify(1, builder.build());
        } else {
            Notification notification = new NotificationCompat.Builder(MainActivity.this)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_baseline_add_24)
                    .build();
            manager.notify(1, notification);
        }
    }
    private synchronized void refreshData() {
//        Intent intent = new Intent(getContext(), MyService.class);
//        getActivity().startService(intent);
        // 填充关注列表
        new getFollowList(this.handleFollowList, BasicInfo.mId).send();
        // 填充被关注列表
        new getFollowedList(this.handleFollowedList, BasicInfo.mId).send();
        // 填充消息列表
        new getNoticeList(this.handleNoticeList,BasicInfo.mId).send();
        // 填充动态列表
        new getAllpost(this.handleAllpostList,BasicInfo.mId,"20").send();
        // 填充草稿箱
        new getDraftList(this.handleDraftList,BasicInfo.mId).send();
        // 填充我的动态列表
        new getMypost(this.handleMypostList,BasicInfo.mId).send();
        // 填充关注动态列表
        new getWatchpost(this.handleWatchpostList, BasicInfo.mId, "20").send();
        // 填充被屏蔽用户列表
        new getBlockList(getBlockListCallback,BasicInfo.mId).send();
        // 填充点赞列表
        new getPostListID(handlePostListID, "20").send();
        //new getUpvote(this.handleUpvoteList, BasicInfo.mPostListID).send();


        int count = 0;
        new GetFanlistRequest(getFollowListCallback, BasicInfo.mId).send();
        new GetWatchlistRequest(getWatchListCallback, BasicInfo.mId).send();

    }
}