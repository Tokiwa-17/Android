package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.entity.NoticeInfo;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.MyImageLoader;
import com.example.myapplication.utils.StringCutter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Shows how to implement a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
//public class NotificationAdapter extends
//        RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
//
//    private final List<String> mTypeList;
//    private final List<String> mTextList;
//    private final LayoutInflater mInflater;
//
//    class NotificationViewHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener {
//        public final TextView typeItemView;
//        public final TextView textItemView;
//        final NotificationAdapter mAdapter;
//
//        /**
//         * Creates a new custom view holder to hold the view to display in
//         * the RecyclerView.
//         *
//         * @param itemView The view in which to display the data.
//         * @param adapter The adapter that manages the the data and views
//         *                for the RecyclerView.
//         */
//        public NotificationViewHolder(View itemView, NotificationAdapter adapter) {
//            super(itemView);
//            typeItemView = itemView.findViewById(R.id.type);
//            textItemView = itemView.findViewById(R.id.text);
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
//    public NotificationAdapter(Context context, List<NoticeInfo> noticeList) {
//        mInflater = LayoutInflater.from(context);
//        this.mTypeList = new LinkedList<>();
//        this.mTextList = new LinkedList<>();
//        if (noticeList!=null){
//            for(int i = 0; i < noticeList.size(); i++){
//                this.mTypeList.add(noticeList.get(i).type);
//                this.mTextList.add(noticeList.get(i).text);
//            }
//        }
//
//    }
//
//    /**
//     * Called when RecyclerView needs a new ViewHolder of the given type to
//     * represent an item.
//     *
//     * This new ViewHolder should be constructed with a new View that can
//     * represent the items of the given type. You can either create a new View
//     * manually or inflate it from an XML layout file.
//     *
//     * The new ViewHolder will be used to display items of the adapter using
//     * onBindViewHolder(ViewHolder, int, List). Since it will be reused to
//     * display different items in the data set, it is a good idea to cache
//     * references to sub views of the View to avoid unnecessary findViewById()
//     * calls.
//     *
//     * @param parent   The ViewGroup into which the new View will be added after
//     *                 it is bound to an adapter position.
//     * @param viewType The view type of the new View. @return A new ViewHolder
//     *                 that holds a View of the given view type.
//     */
//    @Override
//    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent,
//                                                             int viewType) {
//        // Inflate an item view.
//        View mItemView = mInflater.inflate(
//                R.layout.notice_item, parent, false);
//        return new NotificationViewHolder(mItemView, this);
//    }
//
//    /**
//     * Called by RecyclerView to display the data at the specified position.
//     * This method should update the contents of the ViewHolder.itemView to
//     * reflect the item at the given position.
//     *
//     * @param holder   The ViewHolder which should be updated to represent
//     *                 the contents of the item at the given position in the
//     *                 data set.
//     * @param position The position of the item within the adapter's data set.
//     */
//    @Override
//    public void onBindViewHolder(NotificationAdapter.NotificationViewHolder holder,
//                                 @SuppressLint("RecyclerView") int position) {
//        // Retrieve the data for that position.
//        String mCurrent = mTypeList.get(position);
//        // Add the data to the view holder.
//        holder.typeItemView.setText(mTypeList.get(position));
//        holder.textItemView.setText(mTextList.get(position));
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
//    /**
//     * Returns the total number of items in the data set held by the adapter.
//     *
//     * @return The total number of items in this adapter.
//     */
//    @Override
//    public int getItemCount() {
//        return mTypeList.size();
//    }
//
//}
public class NotificationAdapter<T> extends MyBaseListAdapter {
    private CircleImageView mHead;

    public NotificationAdapter(List<T> data, Context context) {
        super(R.layout.notice_item, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) { }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        NoticeInfo data = (NoticeInfo) o;
        viewHolder.setText(R.id.type, data.type)
                .setText(R.id.text, StringCutter.cutter(data.text, 15));

    }

    @Override
    protected  void setListener(BaseViewHolder viewHolder, Object o) {
    }
}