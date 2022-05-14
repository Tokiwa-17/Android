package com.example.myapplication.adapter;


import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.myView.FocusButton;
import com.example.myapplication.utils.MyImageLoader;
import com.example.myapplication.utils.StringCutter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 列表适配器
 */
public class ShortProfileAdapter<T> extends MyBaseListAdapter {
    private CircleImageView mHead;
    private TextView mName;
    private TextView mAffiliation;
    private FocusButton mWatchBtn;

    public ShortProfileAdapter(List<T> data, Context context) {
        super(R.layout.layout_profile_row, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) { }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        ShortProfile data = (ShortProfile) o;
        viewHolder.setText(R.id.profile_user_name, data.name)
                  .setText(R.id.info, StringCutter.cutter(data.intro, 15))
                  .setText(R.id.fan, "0 人关注");
        try {
            mHead = viewHolder.getView(R.id.profile_image);
            MyImageLoader.loadImage(mHead, data.url);
        } catch (Exception e) {

        }
        mWatchBtn = viewHolder.getView(R.id.watch_btn);
        mWatchBtn.setPressed_(data.isFan);
    }

    @Override
    protected  void setListener(BaseViewHolder viewHolder, Object o) {
        viewHolder.addOnClickListener(R.id.watch_btn);
    }
}
