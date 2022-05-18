package com.example.myapplication.activity;

import com.example.myapplication.activity.BaseActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.example.myapplication.R;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.adapter.NotificationAdapter;
import com.example.myapplication.entity.NoticeInfo;

import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.request.block.getBlockList;
import com.example.myapplication.request.draft.getDraftList;
import com.example.myapplication.request.follow.GetFanlistRequest;
import com.example.myapplication.request.follow.GetWatchlistRequest;
import com.example.myapplication.request.follow.getFollowList;
import com.example.myapplication.request.follow.getFollowedList;
import com.example.myapplication.request.notification.getNoticeList;
import com.example.myapplication.request.post.getMypost;
import com.example.myapplication.request.user.LoginRequest;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.Global;
import com.example.myapplication.utils.Hint;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

public class LoginActivity extends BaseActivity {
    /*变量*/
    @BindView(R.id.login_account)
    EditText accountEditText;

    @BindView(R.id.login_password)
    EditText passwordEditText;

    @BindView(R.id.login_button)
    Button loginButton;

    @BindView(R.id.logon_button)
    Button logonButton;

    boolean isLogin = false;
    int count = 0;

    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleLogin = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
            try {
                if (response.code() != 200) {
                    LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE) {
                        Log.e("HttpResponse", responseBodyString);
                    }
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    BasicInfo.mId = jsonObject.getString("id");
                    BasicInfo.mName = jsonObject.getString("nickname");
                    BasicInfo.mAccount = jsonObject.getString("account");
                    BasicInfo.mPassword = jsonObject.getString("password");
                    BasicInfo.mAvatarUrl = jsonObject.getString("avatar");
                    BasicInfo.mSingnature = jsonObject.getString("introduction");
                    LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录成功..."));
                    beforeJump(jsonObject.getString("id"));
                    onJumpToMain();
                }
            } catch (Exception e) {
                LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
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
                        PostInfo post = new PostInfo(BasicInfo.mName,BasicInfo.mAvatarUrl,title, text);
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
                        PostInfo post = new PostInfo(BasicInfo.mName,BasicInfo.mAvatarUrl,title, text);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        accountEditText.setText("ylf");
        passwordEditText.setText("123456");
    }

    private void beforeJump(String mId) {
        // 填充关注列表
        new getFollowList(this.handleFollowList, mId).send();
        // 填充被关注列表
        new getFollowedList(this.handleFollowedList, mId).send();
        // 填充消息列表
        new getNoticeList(this.handleNoticeList,mId).send();
        // 填充草稿箱
        new getDraftList(this.handleDraftList,mId).send();
        // 填充我的动态列表
        new getMypost(this.handleMypostList,mId).send();
        // 填充被屏蔽用户列表
        new getBlockList(getBlockListCallback,mId).send();
        
        int count = 0;
        new GetFanlistRequest(getFollowListCallback, mId).send();
        new GetWatchlistRequest(getWatchListCallback, mId).send();

    }

    private synchronized void addCounter() {
        count++;

    }

    private void onJumpToMain() {
        Hint.endActivityLoad(LoginActivity.this);
        isLogin = true;
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.login_button)
    public void onClickLogin() {
        Log.e("OnClick", "ttttttttttttttttttt");
        if (isLogin) return;
        String account = accountEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        new LoginRequest(this.handleLogin, account, password).send();
    }

    @OnClick(R.id.logon_button)
    public void onClickLogon() {
        Log.e("OnClick", "Switch");
        Intent intent = new Intent(LoginActivity.this, LogonActivity.class);
        startActivity(intent);
    }

}