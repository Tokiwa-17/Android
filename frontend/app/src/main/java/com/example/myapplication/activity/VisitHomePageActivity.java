package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MypostAdapter;
import com.example.myapplication.adapter.ShortProfileAdapter;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.myView.BlockButton;
import com.example.myapplication.myView.FocusButton;
import com.example.myapplication.request.block.AddToBlockRequest;
import com.example.myapplication.request.block.DeleteFromBlockRequest;
import com.example.myapplication.request.follow.AddToWatchRequest;
import com.example.myapplication.request.follow.DeleteFromWatchRequest;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.MyImageLoader;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class VisitHomePageActivity extends AppCompatActivity {

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    @BindView(R.id.homepage_name)
    TextView name;

    @BindView(R.id.signature)
    TextView signature;

    @BindView(R.id.num_focus)
    TextView numFocus;

    @BindView(R.id.num_focused)
    TextView numFocused;

    @BindView(R.id.focus_btn)
    FocusButton focusButton;

    @BindView(R.id.block_btn)
    BlockButton blockButton;

//    @BindView((R.id.btn_))

    public String mName;
    public String mUrl;
    public String mIntro;
    public String mId;
    public boolean isFan;
    public boolean isBlock;
    protected MypostAdapter mypostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_home_page);
        ButterKnife.bind(this);

        Intent intent = getIntent();


        // 获取用户信息并设置
        mId = intent.getStringExtra("id");
        mName = intent.getStringExtra("name");
        name.setText(mName);
        mIntro = intent.getStringExtra("intro");
        signature.setText(mIntro);
        mUrl = intent.getStringExtra("url");
        isFan = intent.getBooleanExtra("isFan",false);
        isBlock = intent.getBooleanExtra("isBlock", false);

        int i = 0;
        for (ShortProfile shortProfile : BasicInfo.WATCH_LIST) {
            if (shortProfile.id.equals(mId)) break;
            i++;
        }
        if (i < BasicInfo.WATCH_LIST.size()){
            isFan = true;
        }

        i = 0;
        for (ShortProfile shortProfile : BasicInfo.BLOCK_LIST) {
            if (shortProfile.id.equals(mId)) break;
            i++;
        }
        if (i < BasicInfo.BLOCK_LIST.size()){
            isBlock = true;
        }


        MyImageLoader.loadImage(imgAvatar, mUrl);
        Log.e("Focus", BasicInfo.WATCH_LIST.toString());
        focusButton.setPressed_(isFan);
        blockButton.setPressed_(isBlock);

        //加载动态信息
        if(BasicInfo.mTargetpost != null){
            RecyclerView mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerview);
//            MypostAdapter mAdapter = new MypostAdapter(this, BasicInfo.mTargetpost);
//            mRecyclerView.setAdapter(mAdapter);
            mypostAdapter = new MypostAdapter(BasicInfo.mTargetpost,this);
            mypostAdapter.setRecyclerManager(mRecyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        }

        numFocus.setText(Integer.toString(BasicInfo.mFollowerNumber));
        numFocused.setText(Integer.toString(BasicInfo.mFollowedNumber));

        addFocusButtonListener();
        addBlockButtonListener();

    }

    private void addFocusButtonListener() {
        focusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.e("AAAAAA", "AAAAAAAA");
                    focusButton.startLoading(() -> {
                        if (isFan) {
                            String user_id = mId;
                            String fan_id = BasicInfo.mId;
                            new DeleteFromWatchRequest(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Log.e("error1", e.toString());
                                    runOnUiThread(focusButton::clickFail);
                                }
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String resStr = response.body().string();
                                    try {
                                        //JSONObject jsonObject = new JSONObject(resStr);
                                        isFan = false;
                                        BasicInfo.removeFromWatchList(mId);
                                        runOnUiThread(focusButton::clickSuccess);
                                    } catch (Exception e) {
                                        Log.e("error2", e.toString());
                                        runOnUiThread(focusButton::clickFail);
                                    }

                                }


                            }, user_id, fan_id).send();

                        } else {
                            String fan_id = BasicInfo.mId;
                            String user_id = mId;
                            new AddToWatchRequest(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    Log.e("error1", e.toString());
                                    runOnUiThread(focusButton::clickFail);
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    String resStr = response.body().string();
                                    Log.e("response", resStr);
                                    try {
                                        isFan = true;
                                        BasicInfo.addToWatchList(new ShortProfile(mId, mName, mUrl, mIntro, isFan, isBlock));
                                        runOnUiThread(focusButton::clickSuccess);
                                    } catch (Exception e) {
                                        Log.e("error2", e.toString());
                                        runOnUiThread(focusButton::clickFail);
                                    }
                                }
                            }, user_id, fan_id).send();
                        }
                    });
                } catch (Exception e) {
                }
            }
        });
    }

    private void addBlockButtonListener() {
        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    blockButton.startLoading(() -> {
                        if (isBlock) {
                            String user_id = BasicInfo.mId;
                            String block_id = mId;
                            new DeleteFromBlockRequest(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Log.e("error1", e.toString());
                                    runOnUiThread(blockButton::clickFail);
                                }
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String resStr = response.body().string();
                                    try {
                                        //JSONObject jsonObject = new JSONObject(resStr);
                                        isBlock = false;
                                        BasicInfo.removeFromBlockList(mId);
                                        runOnUiThread(blockButton::clickSuccess);
                                    } catch (Exception e) {
                                        Log.e("error2", e.toString());
                                        runOnUiThread(blockButton::clickFail);
                                    }

                                }


                            }, user_id, block_id).send();

                        } else {
                            String block_id = mId;
                            String user_id = BasicInfo.mId;
                            new AddToBlockRequest(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    Log.e("error1", e.toString());
                                    runOnUiThread(blockButton::clickFail);
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    String resStr = response.body().string();
                                    Log.e("response", resStr);
                                    try {
                                        isBlock = true;
                                        BasicInfo.addToBlockList(new ShortProfile(mId, mName, mUrl, mIntro, isFan, isBlock));
                                        runOnUiThread(blockButton::clickSuccess);
                                    } catch (Exception e) {
                                        Log.e("error2", e.toString());
                                        runOnUiThread(blockButton::clickFail);
                                    }
                                }
                            }, user_id, block_id).send();
                        }
                    });
                } catch (Exception e) {
                }
            }
        });
    }
}