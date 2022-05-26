package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MypostAdapter;
import com.example.myapplication.adapter.ShortProfileAdapter;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.request.post.getMypost;
import com.example.myapplication.request.query.getQuery;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.Global;
import com.example.myapplication.utils.MyImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class QueryResultActivity extends AppCompatActivity {
    private String query;
    private String type;
    ImageView imgAvatar;
    EditText searchView;
    protected MypostAdapter mypostAdapter;
    protected  ShortProfileAdapter shortProfileAdapter;
    ArrayList<ShortProfile> profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);
        Intent intent = getIntent();
        query = intent.getStringExtra("query");
        type = intent.getStringExtra("type");
        imgAvatar = findViewById(R.id.imageButton);
        MyImageLoader.loadImage(imgAvatar, BasicInfo.mAvatarUrl);
        searchView = findViewById(R.id.search_view);
        searchView.setText(query);
        searchView.setFocusable(false);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QueryResultActivity.this, QueryActivity.class);
                startActivity(intent);
            }
        });
        if(type.equals("用户名称")){
            Log.e("type", type);
            for (ShortProfile shortProfile : BasicInfo.RESULT_LIST) {
                boolean is_fan = false;
                for (ShortProfile shortProfile2 : BasicInfo.WATCH_LIST) {
                    if (shortProfile2.id.equals(shortProfile.id)) {
                        is_fan = true;
                    }
                }
                shortProfile.isFan = is_fan;
//                profileList.add(shortProfile);
            }
            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            shortProfileAdapter = new ShortProfileAdapter(BasicInfo.RESULT_LIST, this);
            shortProfileAdapter.setRecyclerManager(mRecyclerView);
            shortProfileAdapter.setOnItemClickListener((adapter, view, position) -> {
                visitHomePage(position);
            });

//            addButtonListener(shortProfileAdapter, profileList);
        }
        else {
            if(BasicInfo.mResultPostList != null){
                RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                mypostAdapter = new MypostAdapter(BasicInfo.mResultPostList,this);
                mypostAdapter.setRecyclerManager(mRecyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            }
        }
    }
    public void visitHomePage(int position) {
        ShortProfile shortProfile;
        shortProfile = BasicInfo.RESULT_LIST.get(position);
        new getMypost(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
//            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
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
                        JSONObject jsonObject = new JSONObject(responseBodyString);
                        JSONArray jsonArray = (JSONArray) jsonObject.get("post_list");
                        if(BasicInfo.mTargetpost != null) {
                            BasicInfo.mTargetpost.clear();
                        }
                        BasicInfo.mTargetpostNumber = jsonArray.length();
                        BasicInfo.mTargetpost = new LinkedList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
                            String title = subJsonObject.getString("title");
                            String text = subJsonObject.getString("text");
                            PostInfo post = new PostInfo(shortProfile.name,shortProfile.url,title, text);
                            BasicInfo.mTargetpost.add(post);
                        }
                    }
                }
                catch (Exception e) {
                    if (Global.HTTP_DEBUG_MODE)
                        Log.e("HttpResponse", e.toString());
                }
            }
        }, shortProfile.id).send();
        Intent intent = new Intent(this, VisitHomePageActivity.class);
        intent.putExtra("id", shortProfile.id);
        intent.putExtra("name", shortProfile.name);
        intent.putExtra("url", shortProfile.url);
        intent.putExtra("intro", shortProfile.intro);
        intent.putExtra("isFan", shortProfile.isFan);
        startActivity(intent);
    }


}
