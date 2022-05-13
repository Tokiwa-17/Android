package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MainActivityPagerAdapter;
import com.example.myapplication.request.base.BaseGetRequest;
import com.example.myapplication.request.testRequest.testGet;
import com.example.myapplication.request.testRequest.testGet2;
import com.example.myapplication.request.testRequest.testPost;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    private ViewPager viewPager;
    private MainActivityPagerAdapter mMainActivityPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_follow:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_conversations:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(4);
                    break;
            }
            return true;
        });

        viewPager = findViewById(R.id.nav_host_fragment);
        mMainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mMainActivityPagerAdapter);
    }

//    public void updateMsg(View v) {
//        new testGet(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.e("error", e.toString());
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                msg = response.body().string();
//                Log.e("MSG", msg);
//            }
//        }).send();
//    }
//
//    public void updateMsg2(View v) {
//        new testGet2(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.e("error", e.toString());
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                Log.e(CALLBACK, "onResponse");
//            }
//        }).send();
//    }
//
//    public void Login(View v) {
//        String Account = myAccount.getText().toString();
//        String Password = myPassword.getText().toString();
//        new testPost(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.e("error", e.toString());
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                Log.e(CALLBACK, "onResponse");
//            }
//        }, "", Account, Password).send();
//    }
}