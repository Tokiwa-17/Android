package com.example.myapplication.activity;

import android.content.Context;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.adapter.MyBaseListAdapter;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.utils.MyImageLoader;
import com.example.myapplication.utils.StringCutter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralPostAdapter<T> extends MyBaseListAdapter {
    private CircleImageView mHead;
    private ImageButton mButton;

    public GeneralPostAdapter(List<T> data, Context context) {
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
        mButton = viewHolder.getView(R.id.upvote_btn);
    }

    @Override
    protected  void setListener(BaseViewHolder viewHolder, Object o) {
    }
}