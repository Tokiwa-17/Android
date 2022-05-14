package com.example.myapplication.activity;

import com.example.myapplication.activity.BaseActivity;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.request.follow.GetFanlistRequest;
import com.example.myapplication.request.follow.GetWatchlistRequest;
import com.example.myapplication.request.follow.getFollowList;
import com.example.myapplication.request.follow.getFollowedList;
import com.example.myapplication.request.user.LoginRequest;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.Global;
import com.example.myapplication.utils.Hint;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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