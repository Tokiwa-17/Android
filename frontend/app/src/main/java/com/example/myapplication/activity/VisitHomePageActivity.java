package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MypostAdapter;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.MyImageLoader;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.btn_edit)
    Button editBtn;

//    @BindView((R.id.btn_))

    public String mName;
    public String mUrl;
    public String mIntro;
    public String mId;

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
        MyImageLoader.loadImage(imgAvatar, mUrl);

        //加载动态信息
        if(BasicInfo.mTargetpost != null){
            RecyclerView mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerview);
            MypostAdapter mAdapter = new MypostAdapter(this, BasicInfo.mTargetpost);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        }

        numFocus.setText(Integer.toString(BasicInfo.mFollowerNumber));
        numFocused.setText(Integer.toString(BasicInfo.mFollowedNumber));
    }
}