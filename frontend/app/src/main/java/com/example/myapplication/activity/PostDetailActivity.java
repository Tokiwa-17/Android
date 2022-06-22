package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.MyService;
import com.example.myapplication.R;
import com.example.myapplication.adapter.GridImageAdapter;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.request.post.addPost;
import com.example.myapplication.request.post.getPost;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.Global;
import com.example.myapplication.utils.Http;
import com.example.myapplication.utils.MyImageLoader;
//import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PostDetailActivity extends AppCompatActivity {
    //private GridImageAdapter adapter;
    private RecyclerView mRecyclerView;
    private List<LocalMedia> mediaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        CircleImageView pd_avatar = findViewById(R.id.pd_avatar);
        TextView pd_nickname = findViewById(R.id.pd_nickname);
        TextView pd_title = findViewById(R.id.pd_title);
        TextView pd_content = findViewById(R.id.pd_text);
        ImageView pd_image = findViewById(R.id.pd_image);
        VideoView pd_video;
        mRecyclerView = findViewById(R.id.pd_recycler);

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");
        String image_url = intent.getStringExtra("image_url");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String nickname = intent.getStringExtra("nickname");
        //LocalMedia img = new LocalMedia(new Object(image_url[0]));
        MyImageLoader.loadImage(pd_image, image_url);
        MyImageLoader.loadImage(pd_avatar, url);
        pd_nickname.setText(nickname);
        pd_content.setText(content);
        pd_title.setText(title);
    }
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @SuppressLint("CheckResult")
        @Override
        public void onAddPicClick() {

        }
    };
}