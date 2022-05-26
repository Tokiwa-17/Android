package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.entity.NoticeInfo;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.myView.FocusButton;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.MyImageLoader;
import com.example.myapplication.utils.StringCutter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MypostAdapter<T> extends MyBaseListAdapter {
    private CircleImageView mHead;

    public MypostAdapter(List<T> data, Context context) {
        super(R.layout.post_item, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) { }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        PostInfo data = (PostInfo) o;
        viewHolder.setText(R.id.nickname, data.nickname)
                .setText(R.id.title, StringCutter.cutter(data.title, 15))
                .setText(R.id.text, data.text);
        try {
            mHead = viewHolder.getView(R.id.avatar_image);
            MyImageLoader.loadImage(mHead, data.avatarUrl);
        } catch (Exception e) {

        }
    }

    @Override
    protected  void setListener(BaseViewHolder viewHolder, Object o) {
    }
}