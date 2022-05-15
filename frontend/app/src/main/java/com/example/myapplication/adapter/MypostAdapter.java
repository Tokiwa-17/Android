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
import com.example.myapplication.R;
import com.example.myapplication.entity.NoticeInfo;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.MyImageLoader;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

/**
 * Shows how to implement a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
public class MypostAdapter extends
        RecyclerView.Adapter<MypostAdapter.MypostViewHolder> {

    private final List<String> mTitleList;
    private final List<String> mTextList;
    private final LayoutInflater mInflater;

    class MypostViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView nicknameItemView;
        public final TextView titleItemView;
        public final TextView textItemView;
        private final ImageView imgAvatar;
        final MypostAdapter mAdapter;

        /**
         * Creates a new custom view holder to hold the view to display in
         * the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and views
         *                for the RecyclerView.
         */
        public MypostViewHolder(View itemView, MypostAdapter adapter) {
            super(itemView);
            nicknameItemView = itemView.findViewById(R.id.nickname);
            titleItemView = itemView.findViewById(R.id.title);
            textItemView = itemView.findViewById(R.id.text);
            imgAvatar = itemView.findViewById(R.id.avatar_image);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


    }

    public MypostAdapter(Context context, List<PostInfo> postList) {
        mInflater = LayoutInflater.from(context);
        this.mTitleList = new LinkedList<>();
        this.mTextList = new LinkedList<>();
        if (postList!=null){
            for(int i = 0; i < postList.size(); i++){
                this.mTitleList.add(postList.get(i).title);
                this.mTextList.add(postList.get(i).text);
            }
        }

    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to
     * represent an item.
     *
     * This new ViewHolder should be constructed with a new View that can
     * represent the items of the given type. You can either create a new View
     * manually or inflate it from an XML layout file.
     *
     * The new ViewHolder will be used to display items of the adapter using
     * onBindViewHolder(ViewHolder, int, List). Since it will be reused to
     * display different items in the data set, it is a good idea to cache
     * references to sub views of the View to avoid unnecessary findViewById()
     * calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after
     *                 it is bound to an adapter position.
     * @param viewType The view type of the new View. @return A new ViewHolder
     *                 that holds a View of the given view type.
     */
    @Override
    public MypostAdapter.MypostViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(
                R.layout.post_item, parent, false);
        return new MypostViewHolder(mItemView, this);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the ViewHolder.itemView to
     * reflect the item at the given position.
     *
     * @param holder   The ViewHolder which should be updated to represent
     *                 the contents of the item at the given position in the
     *                 data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MypostAdapter.MypostViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
        // Retrieve the data for that position.
        String mCurrent = mTitleList.get(position);
        // Add the data to the view holder.
        holder.titleItemView.setText(mTitleList.get(position));
        holder.textItemView.setText(mTextList.get(position));
        holder.nicknameItemView.setText(BasicInfo.mName);
        MyImageLoader.loadImage(holder.imgAvatar, BasicInfo.mAvatarUrl);
//        holder.wordItemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), SecondActivity.class);
//                intent.putExtra("title",mCurrent);
//                intent.putExtra("text",mTextList.get(position));
//                view.getContext().startActivity(intent);
//            }
//        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mTitleList.size();
    }

}
