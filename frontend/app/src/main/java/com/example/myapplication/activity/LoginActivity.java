package com.example.myapplication.activity;


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
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.example.myapplication.R;
import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.request.follow.getFollowList;
import com.example.myapplication.request.user.LoginRequest;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.Global;
import com.example.myapplication.utils.Hint;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends com.example.androidapp.activity.BaseActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    private void beforeJump(String mId) {
        // 填充关注列表
        new getFollowList(this.handleFollowList, mId).send();
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