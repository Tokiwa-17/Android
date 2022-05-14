package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import com.andreabaccega.widget.FormEditText;
import com.example.myapplication.activity.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.EditInfoPagerAdapter;
import com.example.myapplication.request.user.UpdateUserInfo;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.Global;
import com.example.myapplication.utils.Hint;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EditInfoActivity extends BaseActivity {

    @BindView(R.id.edit_name)
    FormEditText editName;

    @BindView(R.id.edit_password)
    FormEditText editPassword;

    @BindView(R.id.edit_password2)
    FormEditText editPassword2;

    @BindView(R.id.edit_signature)
    FormEditText editSignature;

    /******************************
     ************ 回调 ************
     ******************************/
    private okhttp3.Callback updateUserInfo = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            EditInfoActivity.this.runOnUiThread(() -> Hint.endActivityLoad(EditInfoActivity.this));
            try {
                if (response.code() != 200) {
                    EditInfoActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(EditInfoActivity.this, "修改失败..."));
                } else {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody != null ? responseBody.string() : "";
                    if (Global.HTTP_DEBUG_MODE)
                        Log.e("HttpResponse", responseBodyString);
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    String nickname = jsonObject.getString("nickname");
                    String password = jsonObject.getString("password");
                    String intro = jsonObject.getString("introduction");
                    BasicInfo.mName = nickname;
                    BasicInfo.mPassword = password;
                    BasicInfo.mSingnature = intro;
                }
            } catch (JSONException e) {
                EditInfoActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(EditInfoActivity.this, "修改失败..."));
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            EditInfoActivity.this.runOnUiThread(() -> Hint.endActivityLoad(EditInfoActivity.this));
            EditInfoActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(EditInfoActivity.this, "修改失败..."));
            if (Global.HTTP_DEBUG_MODE)
                Log.e("HttpError", e.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        editName.setText(BasicInfo.mName);
        editPassword.setText(BasicInfo.mPassword);
        editPassword2.setText(BasicInfo.mPassword);
        editSignature.setText(BasicInfo.mSingnature);
    }

    @OnClick(R.id.edit_info_save)
    void updateEdit() {
        String name = editName.getText().toString();
        Boolean flag = true;
        if (name.length() == 0) {
            EditInfoActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(EditInfoActivity.this, "用户名不能为空..."));
            flag = false;
        }
        String password = editPassword.getText().toString();
        if (password.length() == 0) {
            EditInfoActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(EditInfoActivity.this, "密码不能为空..."));
            flag = false;
        }
        String password2 = editPassword2.getText().toString();
        if (password2.length() == 0) {
            EditInfoActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(EditInfoActivity.this, "请确认密码..."));
            flag = false;
        }
        if(password.equals(password2) == false) {
            EditInfoActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(EditInfoActivity.this, "输入的密码不一致..."));
            flag = false;
        }
        String intro = editSignature.getText().toString();
        if (flag) {
            new UpdateUserInfo(updateUserInfo, BasicInfo.mId, name, password, intro).send();
            Hint.endActivityLoad(EditInfoActivity.this);
            Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}