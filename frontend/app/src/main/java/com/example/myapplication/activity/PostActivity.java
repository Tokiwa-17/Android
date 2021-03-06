package com.example.myapplication.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.utils.BasicInfo;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.GridImageAdapter;
import com.example.myapplication.request.follow.getFollowList;
import com.example.myapplication.request.draft.deleteDraft;
import com.example.myapplication.request.draft.saveDraft;

import com.example.myapplication.request.post.addPost;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.Global;
import com.example.myapplication.utils.Hint;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostActivity extends BaseActivity {

    private int maxSelectNum = 9;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<File> files = new ArrayList<>();
    private GridImageAdapter adapter;
    private RecyclerView mRecyclerView;
    private PopupWindow pop;

    private TextView title;
    private TextView text;
    private TextView post_btn;
    private TextView delete_btn;
    private String draft_id;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this,  10 * 1000);
            saveDraft();
        }
    };


    ImageButton pictureButton, audioButton, videoButton;
    TextView confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        title = findViewById(R.id.post_title);
        text = findViewById(R.id.post_content);
        Intent intent = getIntent();
        post_btn = findViewById(R.id.confirm_post);
        delete_btn = findViewById(R.id.delete_post);
        title.setText(intent.getStringExtra("title"));
        text.setText(intent.getStringExtra("text"));
        draft_id = intent.getStringExtra("draft_id");
//        post_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new addPost(new okhttp3.Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                        if (Global.HTTP_DEBUG_MODE)
//                            Log.e("HttpError", e.toString());
//                    }
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        try {
//                            if (response.code() != 200) {
//                                //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
//                            } else {
//                                ResponseBody responseBody = response.body();
//                                String responseBodyString = responseBody != null ? responseBody.string() : "";
//                                if (Global.HTTP_DEBUG_MODE) {
//                                    Log.e("HttpResponse", responseBodyString);
//                                }
//                            }
//                        }
//                        catch (Exception e) {
//                            if (Global.HTTP_DEBUG_MODE)
//                                Log.e("HttpResponse", e.toString());
//                        }
//                    }
//                },BasicInfo.mId, title.getText().toString(), text.getText().toString(), draft_id).send();
//                PostActivity.this.finish();
//            }
//        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new deleteDraft(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (Global.HTTP_DEBUG_MODE)
                            Log.e("HttpError", e.toString());
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try {
                            if (response.code() != 200) {
                                //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                            } else {
                                ResponseBody responseBody = response.body();
                                String responseBodyString = responseBody != null ? responseBody.string() : "";
                                if (Global.HTTP_DEBUG_MODE) {
                                    Log.e("HttpResponse", responseBodyString);
                                }
                            }
                        }
                        catch (Exception e) {
                            if (Global.HTTP_DEBUG_MODE)
                                Log.e("HttpResponse", e.toString());
                        }
                    }
                }, draft_id).send();
                PostActivity.this.finish();
            }
        });

        pictureButton = findViewById(R.id.pictures);
        audioButton = findViewById(R.id.audio);
        videoButton = findViewById(R.id.video);
        confirmButton = findViewById(R.id.confirm_post);
        mRecyclerView = findViewById(R.id.recycler);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationProvider locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        Log.e("Location",locationProvider.getName());
        Toast.makeText(this,"GPS",Toast.LENGTH_SHORT).show();

        mTimeCounterRunnable.run();


        initWidget();
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callback callback = new okhttp3.Callback(){
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException { }
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i("TAG", "failure");
                    }
                };
                Log.e("title", title.getText().toString());
                Log.e("title2", title.toString());
                new addPost(callback, BasicInfo.mId, title.getText().toString(),text.getText().toString(), draft_id, selectList).send();
                PostActivity.this.finish();
            }
        };
        confirmButton.setOnClickListener(clickListener);
    }
    private void showPop() {
        View bottomView = View.inflate(PostActivity.this, R.layout.dialog_picturefrom, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        PictureSelector.create(PostActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        PictureSelector.create(PostActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }
    private void initWidget() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(adapter);
        View.OnClickListener buttonListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch(v.getId()){
                    case R.id.pictures:
                        showPop();
                        break;
                    case R.id.audio:
                        PictureSelector.create(PostActivity.this)
                                .openGallery(PictureMimeType.ofAudio())
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        return;
                    case R.id.video:
                        PictureSelector.create(PostActivity.this)
                                .openGallery(PictureMimeType.ofVideo())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        return;
                }
            }
        };
        audioButton.setOnClickListener(buttonListener);
        videoButton.setOnClickListener(buttonListener);
        pictureButton.setOnClickListener(buttonListener);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            PictureSelector.create(PostActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            PictureSelector.create(PostActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            PictureSelector.create(PostActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @SuppressLint("CheckResult")
        @Override
        public void onAddPicClick() {
            //获取写的权限
//            RxPermissions rxPermission = new RxPermissions(PostActivity.this);
//            rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .subscribe(new Consumer<Permission>() {
//                        @Override
//                        public void accept(Permission permission) {
//                            if (permission.granted) {// 用户已经同意该权限
//                                //第一种方式，弹出选择和拍照的dialog
////                                showPop();
//
//                                //第二种方式，直接进入相册，但是 是有拍照得按钮的
//                                showAlbum();
//                            } else {
//                                Toast.makeText(PostActivity.this, "拒绝", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
        }
    };


    private void showAlbum() {
        Log.e("???", "llll");
        PictureSelector.create(PostActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .rotateEnabled(false) // 裁剪是否可旋转图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }



    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调

                images = PictureSelector.obtainMultipleResult(data);
                //files.addAll(images);
                selectList.addAll(images);
                Log.e("eee",images.toString());
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }
        }
    }
    public void openLocation() {


    }

    private synchronized void saveDraft() {
        new saveDraft(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (Global.HTTP_DEBUG_MODE)
                    Log.e("HttpError", e.toString());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.code() != 200) {
                        //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
                    } else {
                        ResponseBody responseBody = response.body();
                        String responseBodyString = responseBody != null ? responseBody.string() : "";
                        JSONObject jsonObject = new JSONObject(responseBodyString);
                        String jsonString = (String) jsonObject.get("draft_id");
                        PostActivity.this.draft_id = jsonString;
                        if (Global.HTTP_DEBUG_MODE) {
                            Log.e("HttpResponse", responseBodyString);
                        }
                    }
                }
                catch (Exception e) {
                    if (Global.HTTP_DEBUG_MODE)
                        Log.e("HttpResponse", e.toString());
                }
            }
        },BasicInfo.mId, title.getText().toString(), text.getText().toString(), draft_id).send();
    }



}
