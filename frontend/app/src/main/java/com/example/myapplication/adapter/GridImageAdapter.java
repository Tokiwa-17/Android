package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.pictureselector.adapter
 * email：893855882@qq.com
 * data：16/7/27
 */
public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder> {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List<LocalMedia> list = new ArrayList<>();
    private int selectMax = 9;
    private Context context;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;

    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    public GridImageAdapter(Context context, onAddPicClickListener mOnAddPicClickListener) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public void setList(List<LocalMedia> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        LinearLayout ll_del;
        TextView tv_duration;

        ViewHolder(View view) {
            super(view);
            mImg = view.findViewById(R.id.fiv);
            ll_del = view.findViewById(R.id.ll_del);
            tv_duration = view.findViewById(R.id.tv_duration);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.image_view, viewGroup, false);
        return new ViewHolder(view);
    }

    private boolean isShowAddItem(int position) {
        int size = list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(R.drawable.ic_baseline_add_24);
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddPicClickListener.onAddPicClick();
                }
            });
            viewHolder.ll_del.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.ll_del.setVisibility(View.VISIBLE);
            viewHolder.ll_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = viewHolder.getAdapterPosition();
                    // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                    // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                    if (index != RecyclerView.NO_POSITION) {
                        list.remove(index);
                        notifyItemRemoved(index);
                        notifyItemRangeChanged(index, list.size());
                        Log.i("delete position:", index + "--->remove after:" + list.size());
                    }
                }
            });
            LocalMedia media = list.get(position);
            int mimeType = media.getMimeType();
            String path;
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }
            // 图片
            if (media.isCompressed()) {
                Log.i("compress image result:", new File(media.getCompressPath()).length() / 1024 + "k");
                Log.i("压缩地址::", media.getCompressPath());
            }

            Log.i("原图地址::", media.getPath());
            int pictureType = PictureMimeType.isPictureType(media.getPictureType());
            if (media.isCut()) {
                Log.i("裁剪地址::", media.getCutPath());
            }
            long duration = media.getDuration();
            viewHolder.tv_duration.setVisibility(pictureType == PictureConfig.TYPE_VIDEO
                    ? View.VISIBLE : View.GONE);
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.tv_duration.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.picture_audio);
                StringUtils.modifyTextViewDrawable(viewHolder.tv_duration, drawable, 0);
            } else {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.video_icon);
                StringUtils.modifyTextViewDrawable(viewHolder.tv_duration, drawable, 0);
            }
            viewHolder.tv_duration.setText(DateUtils.timeParse(duration));
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.mImg.setImageResource(R.drawable.audio_placeholder);
            } else {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.color_4d)           //changes
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(viewHolder.itemView.getContext())
                        .load(path)
                        .apply(options)
                        .into(viewHolder.mImg);
            }
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = viewHolder.getAdapterPosition();
                        mItemClickListener.onItemClick(adapterPosition, v);
                    }
                });
            }
        }
    }

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
//package com.example.myapplication.adapter;
//
//import android.content.Context;
//import android.net.Uri;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.luck.picture.lib.config.SelectMimeType;
//import com.luck.picture.lib.entity.LocalMedia;
//import com.luck.picture.lib.utils.DateUtils;
//import com.example.myapplication.R;
//////import com.luck.pictureselector.listener.OnItemLongClickListener;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * @author：luck
// * @date：2016-7-27 23:02
// * @describe：GridImageAdapter
// */
//public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder> {
//    public static final String TAG = "PictureSelector";
//    public static final int TYPE_CAMERA = 1;
//    public static final int TYPE_PICTURE = 2;
//    private final LayoutInflater mInflater;
//    private final ArrayList<LocalMedia> list = new ArrayList<>();
//    private int selectMax = 9;
//
//    /**
//     * 删除
//     */
//    public void delete(int position) {
//        try {
//            if (position != RecyclerView.NO_POSITION && list.size() > position) {
//                list.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, list.size());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public GridImageAdapter(Context context, List<LocalMedia> result) {
//        this.mInflater = LayoutInflater.from(context);
//        this.list.addAll(result);
//    }
//
//    public void setSelectMax(int selectMax) {
//        this.selectMax = selectMax;
//    }
//
//    public int getSelectMax() {
//        return selectMax;
//    }
//
//    public ArrayList<LocalMedia> getData() {
//        return list;
//    }
//
//    public void remove(int position) {
//        if (position < list.size()) {
//            list.remove(position);
//        }
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView mImg;
//        ImageView mIvDel;
//        TextView tvDuration;
//
//        public ViewHolder(View view) {
//            super(view);
//            mImg = view.findViewById(R.id.fiv);
//            mIvDel = view.findViewById(R.id.iv_del);
//            tvDuration = view.findViewById(R.id.tv_duration);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        if (list.size() < selectMax) {
//            return list.size() + 1;
//        } else {
//            return list.size();
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (isShowAddItem(position)) {
//            return TYPE_CAMERA;
//        } else {
//            return TYPE_PICTURE;
//        }
//    }
//
//    /**
//     * 创建ViewHolder
//     */
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = mInflater.inflate(R.layout.gv_filter_image, viewGroup, false);
//        return new ViewHolder(view);
//    }
//
//    private boolean isShowAddItem(int position) {
//        int size = list.size();
//        return position == size;
//    }
//
//    /**
//     * 设置值
//     */
//    @Override
//    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
//        //少于MaxSize张，显示继续添加的图标
//        if (getItemViewType(position) == TYPE_CAMERA) {
//            viewHolder.mImg.setImageResource(R.drawable.ic_add_image);
//            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mItemClickListener != null) {
//                        mItemClickListener.openPicture();
//                    }
//                }
//            });
//            viewHolder.mIvDel.setVisibility(View.INVISIBLE);
//        } else {
//            viewHolder.mIvDel.setVisibility(View.VISIBLE);
//            viewHolder.mIvDel.setOnClickListener(view -> {
//                int index = viewHolder.getAbsoluteAdapterPosition();
//                if (index != RecyclerView.NO_POSITION && list.size() > index) {
//                    list.remove(index);
//                    notifyItemRemoved(index);
//                    notifyItemRangeChanged(index, list.size());
//                }
//            });
//            LocalMedia media = list.get(position);
//            int chooseModel = media.getChooseModel();
//            String path = media.getAvailablePath();
//
//
//            long duration = media.getDuration();
//            viewHolder.tvDuration.setVisibility(PictureMimeType.isHasVideo(media.getMimeType())
//                    ? View.VISIBLE : View.GONE);
//            if (chooseModel == SelectMimeType.ofAudio()) {
//                viewHolder.tvDuration.setVisibility(View.VISIBLE);
//                viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds
//                        (R.drawable.ps_ic_audio, 0, 0, 0);
//
//            } else {
//                viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds
//                        (R.drawable.ps_ic_video, 0, 0, 0);
//            }
//            viewHolder.tvDuration.setText(DateUtils.formatDurationTime(duration));
//            if (chooseModel == SelectMimeType.ofAudio()) {
//                viewHolder.mImg.setImageResource(R.drawable.ps_audio_placeholder);
//            } else {
////                Glide.with(viewHolder.itemView.getContext())
////                        .load(PictureMimeType.isContent(path) && !media.isCut() && !media.isCompressed() ? Uri.parse(path)
////                                : path)
//////                        .centerCrop()
////                        .placeholder(R.color.app_color_f6)
////                        .diskCacheStrategy(DiskCacheStrategy.ALL)
////                        .into(viewHolder.mImg);
//            }
//            //itemView 的点击事件
//            if (mItemClickListener != null) {
//                viewHolder.itemView.setOnClickListener(v -> {
//                    int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
//                    mItemClickListener.onItemClick(v, adapterPosition);
//                });
//            }
//
////            if (mItemLongClickListener != null) {
////                viewHolder.itemView.setOnLongClickListener(v -> {
////                    int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
////                    mItemLongClickListener.onItemLongClick(viewHolder, adapterPosition, v);
////                    return true;
////                });
////            }
//        }
//    }
//
//    private OnItemClickListener mItemClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener l) {
//        this.mItemClickListener = l;
//    }
//
//    public interface OnItemClickListener {
//        /**
//         * Item click event
//         *
//         * @param v
//         * @param position
//         */
//        void onItemClick(View v, int position);
//
//        /**
//         * Open PictureSelector
//         */
//        void openPicture();
//    }
////
////    private OnItemLongClickListener mItemLongClickListener;
////
////    public void setItemLongClickListener(OnItemLongClickListener l) {
////        this.mItemLongClickListener = l;
////    }
//}
