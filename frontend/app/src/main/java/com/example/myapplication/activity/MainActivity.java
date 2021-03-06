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
import android.widget.Toast;

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
import com.example.myapplication.utils.Hint;
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
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????????????????..."));
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
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleFollowList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????????????????..."));
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
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????..."));
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
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????????????????..."));
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
                        String postId = subJsonObject.getString("postId");
                        boolean read = subJsonObject.getBoolean("read");
                        if(!read){
                            MainActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(MainActivity.this, "??????..."));
                        }
                        NoticeInfo notice = new NoticeInfo(type, text, postId, read);
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
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleMypostList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????????????????..."));
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
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleAllpostList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????????????????..."));
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
                        String image_url = subJsonObject.getString("image_url");
//                        Log.e("dad3", imageUrl);
//                        String videoUrl = subJsonObject.getString("video_url");
//                        String audioUrl = subJsonObject.getString("audio_url");
                        PostInfo post = new PostInfo(postId, userId, name,avatar_url,title, text,like,time);
                        BasicInfo.mPostList.add(post);
                        BasicInfo.mPostList.get(BasicInfo.mPostList.size() - 1).setImageUrl(image_url);
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
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleWatchpostList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????????????????..."));
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
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleDraftList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????????????????..."));
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
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handleUpvoteList = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????????????????..."));
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
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    private okhttp3.Callback handlePostListID = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                if (response.code() != 200) {
                    //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????????????????..."));
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
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "????????????..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    public void createNotice(String title, String text){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // manager.cancel(1);
        //??????8.0????????????????????????????????????NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //????????????
            NotificationChannel channel = new NotificationChannel(CHANNELID, CHANNELNAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);//????????????
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNELID);
            builder.setContentTitle(title)//????????????
                    .setContentText(text)//????????????
                    .setWhen(System.currentTimeMillis())//??????????????????
                    .setSmallIcon(R.drawable.ic_baseline_add_24)
                    .setAutoCancel(true)//??????????????????
                    .setVibrate(new long[]{0, 1000, 1000, 1000})//????????????
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
        // ??????????????????
        new getFollowList(this.handleFollowList, BasicInfo.mId).send();
        // ?????????????????????
        new getFollowedList(this.handleFollowedList, BasicInfo.mId).send();
        // ??????????????????
        new getNoticeList(this.handleNoticeList,BasicInfo.mId).send();
        // ??????????????????
        new getAllpost(this.handleAllpostList,BasicInfo.mId,"20").send();
        // ???????????????
        new getDraftList(this.handleDraftList,BasicInfo.mId).send();
        // ????????????????????????
        new getMypost(this.handleMypostList,BasicInfo.mId).send();
        // ????????????????????????
        new getWatchpost(this.handleWatchpostList, BasicInfo.mId, "20").send();
        // ???????????????????????????
        new getBlockList(getBlockListCallback,BasicInfo.mId).send();
        // ??????????????????
        new getPostListID(handlePostListID, "20").send();
        //new getUpvote(this.handleUpvoteList, BasicInfo.mPostListID).send();


        int count = 0;
        new GetFanlistRequest(getFollowListCallback, BasicInfo.mId).send();
        new GetWatchlistRequest(getWatchListCallback, BasicInfo.mId).send();

        for(NoticeInfo notice : BasicInfo.mNoticeList){
            if(!notice.read){
                createNotice(notice.type, notice.text);
            }
        }


    }
}