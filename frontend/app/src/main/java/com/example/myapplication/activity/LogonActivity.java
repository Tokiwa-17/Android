package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.example.myapplication.R;
import com.example.myapplication.request.user.LogonRequest;
import com.example.myapplication.utils.Global;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

public class LogonActivity extends com.example.androidapp.activity.BaseActivity {
    /*变量*/
    @BindView(R.id.logon_account)
    EditText accountEditText;

    @BindView(R.id.logon_password)
    EditText passwordEditText;

    @BindView(R.id.logon_confirm)
    EditText confirmEditText;

    @BindView(R.id.logon_button)
    Button logonButton;

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
        setContentView(R.layout.activity_logon);
        ButterKnife.bind(this);
    }



    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.logon_button)
    public void onClickLogon() {
        Log.e("OnClick", "ttttttttttttttttttt");
        if (isLogin) return;
        String account = accountEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirm = confirmEditText.getText().toString();
        if(!confirm.equals(password)){
            Toast.makeText(LogonActivity.this, "两次密码输入不相同"+password+","+confirm, Toast.LENGTH_LONG).show();
        }
        else{
            new LogonRequest(this.handleLogin, account, password).send();
            Toast.makeText(LogonActivity.this, "注册成功", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LogonActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

}