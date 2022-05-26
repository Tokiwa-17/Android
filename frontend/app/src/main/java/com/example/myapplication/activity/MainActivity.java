package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MainActivityPagerAdapter;
import com.example.myapplication.fragment.main.DashboardFragment;
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
//                case R.id.navigation_conversations:
//                    viewPager.setCurrentItem(2);
//                    break;
                case R.id.navigation_draft:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(3);
                    break;
            }
            return true;
        });

        viewPager = findViewById(R.id.nav_host_fragment);
        mMainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mMainActivityPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        break;
                    case 1:
                        navView.getMenu().findItem(R.id.navigation_follow).setChecked(true);
                        break;
//                    case 2:
//                        navView.getMenu().findItem(R.id.navigation_conversations).setChecked(true);
//                        break;
                    case 2:
                        navView.getMenu().findItem(R.id.navigation_draft).setChecked(true);
                        break;
                    default:
                        ((DashboardFragment) mMainActivityPagerAdapter.getRegisteredFragment(4)).changeFocus();
                        navView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
                        break;
                }
            }
        });
    }

    public void launchPostActivity(View view) {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }
}