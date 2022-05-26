package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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


//public class MypostAdapter extends
//        RecyclerView.Adapter<MypostAdapter.MypostViewHolder> {
//
//    private final List<String> mTitleList;
//    private final List<String> mTextList;
//    private final LayoutInflater mInflater;
//    private final List<String> mNameList;
//    private final List<String> mAvatarUrlList;
//
//    class MypostViewHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener {
//        private final TextView nicknameItemView;
//        public final TextView titleItemView;
//        public final TextView textItemView;
//        private final ImageView imgAvatar;
//        final MypostAdapter mAdapter;
//
//        public MypostViewHolder(View itemView, MypostAdapter adapter) {
//            super(itemView);
//            nicknameItemView = itemView.findViewById(R.id.nickname);
//            titleItemView = itemView.findViewById(R.id.title);
//            textItemView = itemView.findViewById(R.id.text);
//            imgAvatar = itemView.findViewById(R.id.avatar_image);
//            this.mAdapter = adapter;
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//
//        }
//
//
//    }
//
//    public MypostAdapter(Context context, List<PostInfo> postList) {
//        mInflater = LayoutInflater.from(context);
//        this.mTitleList = new LinkedList<>();
//        this.mTextList = new LinkedList<>();
//        this.mNameList = new LinkedList<>();
//        this.mAvatarUrlList = new LinkedList<>();
//        if (postList!=null){
//            for(int i = 0; i < postList.size(); i++){
//                this.mTitleList.add(postList.get(i).title);
//                this.mTextList.add(postList.get(i).text);
//                this.mNameList.add(postList.get(i).nickname);
//                this.mAvatarUrlList.add(postList.get(i).avatarUrl);
//            }
//        }
//
//    }
//
//    @Override
//    public MypostAdapter.MypostViewHolder onCreateViewHolder(ViewGroup parent,
//                                                                         int viewType) {
//        // Inflate an item view.
//        View mItemView = mInflater.inflate(
//                R.layout.post_item, parent, false);
//        return new MypostViewHolder(mItemView, this);
//    }
//
//    @Override
//    public void onBindViewHolder(MypostAdapter.MypostViewHolder holder,
//                                 @SuppressLint("RecyclerView") int position) {
//        // Retrieve the data for that position.
//        String mCurrent = mTitleList.get(position);
//        // Add the data to the view holder.
//        holder.titleItemView.setText(mTitleList.get(position));
//        holder.textItemView.setText(mTextList.get(position));
//        holder.nicknameItemView.setText(mNameList.get(position));
//        MyImageLoader.loadImage(holder.imgAvatar, mAvatarUrlList.get(position));
////        holder.wordItemView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent intent = new Intent(view.getContext(), SecondActivity.class);
////                intent.putExtra("title",mCurrent);
////                intent.putExtra("text",mTextList.get(position));
////                view.getContext().startActivity(intent);
////            }
////        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mTitleList.size();
//    }
//
//}
public class MypostAdapter<T> extends MyBaseListAdapter {
    private CircleImageView mHead;
    private ImageButton mButton;

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
        mButton = viewHolder.getView(R.id.upvote_btn);
    }

    @Override
    protected  void setListener(BaseViewHolder viewHolder, Object o) {
    }
}