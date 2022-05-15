package com.example.myapplication.fragment.follow;

import com.example.myapplication.activity.VisitHomePageActivity;
import com.example.myapplication.adapter.ShortProfileAdapter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.utils.BasicInfo;
import com.kingja.loadsir.core.LoadService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 关注/粉丝
 */
public class FollowListFragment extends Fragment {


    boolean isWatchList;
    Unbinder unbinder;
    ArrayList<ShortProfile> profileList;
    LoadService loadService;
    protected  ShortProfileAdapter shortProfileAdapter;

    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.follow_list)
    TextView textView;


    public FollowListFragment(boolean isWatchList) { // true是关注列表
        this.isWatchList = isWatchList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_follow_list, container, false);
        unbinder = ButterKnife.bind(this, root);

        profileList = new ArrayList<>();
        if (isWatchList) { //关注列表
            for (ShortProfile shortProfile : BasicInfo.WATCH_LIST) {
                profileList.add(shortProfile);
            }
        } else {
            for (ShortProfile shortProfile : BasicInfo.FAN_LIST) {
                profileList.add(shortProfile);
            }
        }
        shortProfileAdapter = new ShortProfileAdapter(profileList, getContext());
        shortProfileAdapter.setRecyclerManager(recyclerView);
        shortProfileAdapter.setOnItemClickListener((adapter, view, position) -> {
            visitHomePage(position);
        });



        return root;
    }

    public void visitHomePage(int position) {
        Intent intent = new Intent(getContext(), VisitHomePageActivity.class);
        ShortProfile shortProfile;
        shortProfile = profileList.get(position);
        intent.putExtra("id", shortProfile.id);
        intent.putExtra("name", shortProfile.name);
        intent.putExtra("url", shortProfile.url);
        intent.putExtra("intro", shortProfile.intro);
        startActivity(intent);
    }
}