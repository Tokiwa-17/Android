package com.example.myapplication.fragment.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import com.example.myapplication.activity.EditInfoActivity;
import com.example.myapplication.activity.GeneralPostAdapter;
import com.example.myapplication.activity.PostDetailActivity;
import com.example.myapplication.activity.QueryActivity;
import com.example.myapplication.adapter.MypostAdapter;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.myView.UpvoteButton;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.MyImageLoader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    ImageView imgAvatar;
    EditText searchView;
    TextView sortByTime;
    TextView sortByLike;
    TextView watchFilter;
    boolean onlyWatch;
    List<PostInfo> watchPost = new LinkedList<>();


    Unbinder unbinder;

    // fragment中添加成员变量
    int aspect_ratio_x = 0;
    int aspect_ratio_y = 0;
    private int maxSelectNum = 6;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected GeneralPostAdapter mypostAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        imgAvatar = root.findViewById(R.id.imageButton);
        MyImageLoader.loadImage(imgAvatar, BasicInfo.mAvatarUrl);
        onlyWatch = false;
        sortByTime = root.findViewById(R.id.sort_by_time);
        sortByLike = root.findViewById(R.id.sort_by_like);
        watchFilter = root.findViewById(R.id.watch_filter);
        searchView = root.findViewById(R.id.search_view);
        searchView.setFocusable(false);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QueryActivity.class);
                startActivity(intent);
            }
        });
        if(BasicInfo.mPostList != null){
            RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
//            MypostAdapter mAdapter = new MypostAdapter(getActivity(), BasicInfo.mPostList);
//            mRecyclerView.setAdapter(mAdapter);
            mypostAdapter = new GeneralPostAdapter(BasicInfo.mPostList,getContext());
            mypostAdapter.setRecyclerManager(mRecyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

            //按时间排序
            sortByTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortByTime.setTextColor(Color.BLUE);
                    sortByLike.setTextColor(Color.GRAY);
                    Comparator<PostInfo> comparator = new Comparator<PostInfo>() {
                        @Override
                        public int compare(PostInfo p1, PostInfo p2) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date date1 = format.parse(p1.time);
                                Date date2 = format.parse(p2.time);
                                if(date1.before(date2)){
                                    return 1;
                                }
                                else {
                                    return -1;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return 1;
                        }
                    };
                    if (onlyWatch){
                        Collections.sort(BasicInfo.mWatchPost, comparator);
                        mypostAdapter = new GeneralPostAdapter(BasicInfo.mWatchPost,getContext());
                        mypostAdapter.setRecyclerManager(mRecyclerView);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        mypostAdapter.setOnItemClickListener((adapter, view, position) -> {
                            visitHomePage(position);
                        });
                    }
                    else {
                        Collections.sort(BasicInfo.mPostList, comparator);
                        mypostAdapter = new GeneralPostAdapter(BasicInfo.mPostList,getContext());
                        mypostAdapter.setRecyclerManager(mRecyclerView);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        mypostAdapter.setOnItemClickListener((adapter, view, position) -> {
                            visitHomePage(position);
                        });
                    }

                }
            });

            //按点赞排序
            sortByLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortByLike.setTextColor(Color.BLUE);
                    sortByTime.setTextColor(Color.GRAY);
                    Comparator<PostInfo> comparator = new Comparator<PostInfo>() {
                        @Override
                        public int compare(PostInfo p1, PostInfo p2) {
                            return p2.like - p1.like;
                        }
                    };

                    if (onlyWatch){
                        Collections.sort(BasicInfo.mWatchPost, comparator);
                        mypostAdapter = new GeneralPostAdapter(BasicInfo.mWatchPost,getContext());
                        mypostAdapter.setRecyclerManager(mRecyclerView);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        mypostAdapter.setOnItemClickListener((adapter, view, position) -> {
                            visitHomePage(position);
                        });
                    }
                    else {
                        Collections.sort(BasicInfo.mPostList, comparator);
                        mypostAdapter = new GeneralPostAdapter(BasicInfo.mPostList,getContext());
                        mypostAdapter.setRecyclerManager(mRecyclerView);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        mypostAdapter.setOnItemClickListener((adapter, view, position) -> {
                            visitHomePage(position);
                        });
                    }

                }
            });

            //只显示已关注
            watchFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onlyWatch){
                        onlyWatch = false;
                        watchFilter.setTextColor(Color.GRAY);
                        mypostAdapter = new GeneralPostAdapter(BasicInfo.mPostList,getContext());
                        mypostAdapter.setRecyclerManager(mRecyclerView);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        mypostAdapter.setOnItemClickListener((adapter, view, position) -> {
                            visitHomePage(position);
                        });
                    }
                    else {
                        onlyWatch = true;
                        watchFilter.setTextColor(Color.BLUE);
                        mypostAdapter = new GeneralPostAdapter(BasicInfo.mWatchPost,getContext());
                        mypostAdapter.setRecyclerManager(mRecyclerView);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        mypostAdapter.setOnItemClickListener((adapter, view, position) -> {
                            visitHomePage(position);
                        });
                    }

                }
            });
        }
        mypostAdapter.setOnItemClickListener((adapter, view, position) -> {
            visitHomePage(position);
        });

        addButtonListener(mypostAdapter);

        return root;
    }

    public void visitHomePage(int position) {

        Intent intent = new Intent(getContext(), PostDetailActivity.class);
        startActivity(intent);
    }

    private void addButtonListener(GeneralPostAdapter mypostAdapter) {
        mypostAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            try {
                UpvoteButton btn = ((UpvoteButton) view);
                btn.startLoading(() -> {
                    Log.e("TTTTTTTTTTT", "TTTTTTTT");
                });
            } catch (Exception e) {

            }
        });
    }

}