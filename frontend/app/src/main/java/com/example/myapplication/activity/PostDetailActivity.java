package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.MyService;
import com.example.myapplication.R;
import com.example.myapplication.utils.MyImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        CircleImageView image = findViewById(R.id.pd_avatar);
        TextView pd_nickname = findViewById(R.id.pd_nickname);
        TextView pd_content = findViewById(R.id.pd_text);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String nickname = intent.getStringExtra("nickname");
        MyImageLoader.loadImage(image, url);
        pd_nickname.setText(nickname);
        pd_content.setText(content);

    }
}