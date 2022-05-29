package com.example.myapplication.fragment.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.activity.GeneralPostAdapter;
import com.example.myapplication.activity.PostActivity;
import com.example.myapplication.activity.VisitHomePageActivity;
import com.example.myapplication.adapter.MypostAdapter;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.myView.UpvoteButton;
import com.example.myapplication.request.post.getMypost;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.Global;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DraftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DraftFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected MypostAdapter mypostAdapter;

    public DraftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DraftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DraftFragment newInstance(String param1, String param2) {
        DraftFragment fragment = new DraftFragment();
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
        View root = inflater.inflate(R.layout.fragment_draft, container, false);
        if(BasicInfo.mDraftlist != null){
            RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
//            MypostAdapter mAdapter = new MypostAdapter(getActivity(), BasicInfo.mDraftlist);
//            mRecyclerView.setAdapter(mAdapter);
            mypostAdapter = new MypostAdapter(BasicInfo.mDraftlist,getContext());
            mypostAdapter.setRecyclerManager(mRecyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            mypostAdapter.setOnItemClickListener((adapter, view, position) -> {
                visitEditPage(position);
            });
        }

        return root;
    }

    public void visitEditPage(int position) {
        PostInfo drafPost;
        drafPost = BasicInfo.mDraftlist.get(position);
//        new getTargetpost(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
////            LoginActivity.this.runOnUiThread(() -> Hint.endActivityLoad(LoginActivity.this));
////            LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "登录失败..."));
//                if (Global.HTTP_DEBUG_MODE)
//                    Log.e("HttpError", e.toString());
//            }
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                try {
//                    if (response.code() != 200) {
//                        //LoginActivity.this.runOnUiThread(() -> Hint.showLongCenterToast(LoginActivity.this, "获取关注列表失败..."));
//                    } else {
//                        ResponseBody responseBody = response.body();
//                        String responseBodyString = responseBody != null ? responseBody.string() : "";
//                        if (Global.HTTP_DEBUG_MODE) {
//                            Log.e("HttpResponse", responseBodyString);
//                        }
//                        JSONObject jsonObject = new JSONObject(responseBodyString);
//                        JSONArray jsonArray = (JSONArray) jsonObject.get("post_list");
//                        if(BasicInfo.mTargetpost != null) {
//                            BasicInfo.mTargetpost.clear();
//                        }
//                        BasicInfo.mTargetpostNumber = jsonArray.length();
//                        BasicInfo.mTargetpost = new LinkedList<>();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject subJsonObject = jsonArray.getJSONObject(i) ;
//                            String title = subJsonObject.getString("title");
//                            String text = subJsonObject.getString("text");
//                            PostInfo post = new PostInfo(shortProfile.name,shortProfile.url,title, text);
//                            BasicInfo.mTargetpost.add(post);
//                        }
//                    }
//                }
//                catch (Exception e) {
//                    if (Global.HTTP_DEBUG_MODE)
//                        Log.e("HttpResponse", e.toString());
//                }
//            }
//        }, draftPost.id).send();
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra("id", drafPost.postId);
        intent.putExtra("title", drafPost.title);
        intent.putExtra("text", drafPost.text);
        startActivity(intent);
    }
}