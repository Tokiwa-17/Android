package com.example.myapplication.fragment.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.R;
import android.util.Log;



import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import com.example.myapplication.activity.EditInfoActivity;
import com.example.myapplication.activity.GeneralPostAdapter;
import com.example.myapplication.activity.PostDetailActivity;
import com.example.myapplication.activity.QueryActivity;
import com.example.myapplication.adapter.MypostAdapter;
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
        searchView = root.findViewById(R.id.search_view);
        searchView.setFocusable(false);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QueryActivity.class);
                startActivity(intent);
            }
        });
        if(BasicInfo.mMypost != null){
            RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
//            MypostAdapter mAdapter = new MypostAdapter(getActivity(), BasicInfo.mPostList);
//            mRecyclerView.setAdapter(mAdapter);
            mypostAdapter = new GeneralPostAdapter(BasicInfo.mPostList,getContext());
            mypostAdapter.setRecyclerManager(mRecyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
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