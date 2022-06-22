package com.example.myapplication.request.post;

import android.util.Log;

import com.example.myapplication.activity.PostActivity;
import com.example.myapplication.request.base.BaseGetRequest;
import com.example.myapplication.request.base.BasePostRequest;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import okhttp3.Callback;


public class addPost extends BaseGetRequest {
    public addPost(Callback callback, String mId, String title, String text, String draft_id, List<LocalMedia> files) {

        // 设置请求URL
        this.to("/api/post/add_post");
        //  设置请求参数
        this.put("user_id", user_id);
        this.put("title", title);
        this.put("text",text);
        int img_num = 0, aud_num = 0, vid_num = 0;
        for (int i = 0; i < files.size(); i++) {
            int mediaType = PictureMimeType.pictureToVideo(files.get(i).getPictureType());
            switch (mediaType) {
                case 1:
                    this.load("image", new File(files.get(i).getPath()));
                    Log.i("mediatype", "image");
                    break;
                case 2:
                    this.load("video", new File(files.get(i).getPath()));
                    Log.i("mediatype", "video");
                    break;
                case 3:
                    this.load("audio", new File(files.get(i).getPath()));
                    Log.i("mediatype", "audio");
                    break;
            }
        }

        //this.put("files", files.getPath());
//        for (int i = 0; i < files.size(); i++) {
//            this.load(String.valueOf(i),files.get(i));
//        }

        this.put("draft_id", draft_id);
        // 设置回调函数
        this.call(callback);
        //Log.e("SSS", this.param.get("title"));
    }
}