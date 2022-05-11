package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.example.myapplication.R;
import com.example.myapplication.request.user.LoginRequest;
import com.example.myapplication.utils.Global;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

public class LoginActivity extends com.example.androidapp.activity.BaseActivity {
    /*变量*/
    @BindView(R.id.login_account)
    EditText accountEditText;

    @BindView(R.id.login_password)
    EditText passwordEditText;

    @BindView(R.id.login_button)
    Button loginButton;

    boolean isLogin = false;

    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback handleLogin = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            //loginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            try {
//                if (response.code() != 200) {
////                    LoginCache.removeCache(getApplicationContext());
////                    LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
//                } else {
//                    ResponseBody responseBody = response.body();
//                    String responseBodyString = responseBody != null ? responseBody.string() : "";
//                    if (Global.HTTP_DEBUG_MODE)
//                        Log.e("HttpResponse", responseBodyString);
//                    JSONObject jsonObject = new JSONObject(responseBodyString);
//                    boolean status = (Boolean) jsonObject.get("status");
//                    String info = (String) jsonObject.get("info");
//                    if (status) {
//
//                        // 保存密码以加入shared...
////                        BasicInfo.PASSWORD = passwordEditText.getText().toString();
////                        LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, info));
////                        beforeJump1();
//                    } else {
////                        LoginCache.removeCache(getApplicationContext());
////                        LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, info));
//                    }
//                }
//            } catch (JSONException e) {
////                LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
//                if (Global.HTTP_DEBUG_MODE)
//                    Log.e("HttpResponse", e.toString());
//            }
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

}