package com.example.myapplication.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapplication.R;

import java.util.List;

/**
 * 历史搜索记录列表适配器
 */
public class HistoryListAdapter<T> extends MyBaseListAdapter {

    public HistoryListAdapter(List<T> data, Context context) {
        super(R.layout.layout_history_list, data, context);
    }

    @Override
    public void remove(int position) {
        getData().remove(position);
        notifyItemRemoved(position);
    }


    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {


    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        String data = (String) o;
        viewHolder.setText(R.id.textView, data);
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        viewHolder.addOnClickListener(R.id.button);
    }
}

