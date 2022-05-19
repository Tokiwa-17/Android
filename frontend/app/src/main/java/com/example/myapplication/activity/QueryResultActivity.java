package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MypostAdapter;
import com.example.myapplication.entity.PostInfo;
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
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class QueryResultActivity extends AppCompatActivity {
    private String query;
    ImageView imgAvatar;
    EditText searchView;
    protected MypostAdapter mypostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);
        Intent intent = getIntent();
        query = intent.getStringExtra("query");
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

        if(BasicInfo.mResultPostList != null){
            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            mypostAdapter = new MypostAdapter(BasicInfo.mResultPostList,this);
            mypostAdapter.setRecyclerManager(mRecyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        }

    }

}