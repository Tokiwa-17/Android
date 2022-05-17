package com.example.myapplication.fragment.follow;

import com.example.myapplication.activity.VisitHomePageActivity;
import com.example.myapplication.adapter.ShortProfileAdapter;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.entity.PostInfo;
import com.example.myapplication.entity.ShortProfile;
import com.example.myapplication.myView.FocusButton;
import com.example.myapplication.request.follow.AddToWatchRequest;
import com.example.myapplication.request.follow.DeleteFromWatchRequest;
import com.example.myapplication.request.post.getMypost;
import com.example.myapplication.utils.BasicInfo;
import com.example.myapplication.utils.Global;
import com.kingja.loadsir.core.LoadService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
                shortProfile.isFan = true;
                profileList.add(shortProfile);
            }
        } else {
            for (ShortProfile shortProfile : BasicInfo.FAN_LIST) {
                boolean is_fan = false;
                for (ShortProfile shortProfile2 : BasicInfo.WATCH_LIST) {
                    if (shortProfile2.id.equals(shortProfile.id)) {
                        is_fan = true;
                    }
                }
                shortProfile.isFan = is_fan;
                profileList.add(shortProfile);
            }
        }
        shortProfileAdapter = new ShortProfileAdapter(profileList, getContext());
        shortProfileAdapter.setRecyclerManager(recyclerView);
        shortProfileAdapter.setOnItemClickListener((adapter, view, position) -> {
            visitHomePage(position);
        });

        addButtonListener(shortProfileAdapter, profileList);

        return root;
    }

    public void visitHomePage(int position) {
        ShortProfile shortProfile;
        shortProfile = profileList.get(position);
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
        Intent intent = new Intent(getContext(), VisitHomePageActivity.class);
        intent.putExtra("id", shortProfile.id);
        intent.putExtra("name", shortProfile.name);
        intent.putExtra("url", shortProfile.url);
        intent.putExtra("intro", shortProfile.intro);
        startActivity(intent);
    }

    private void addButtonListener(ShortProfileAdapter shortProfileAdapter, ArrayList<ShortProfile> shortProfileArrayList) {
        shortProfileAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            try {
                FocusButton btn = ((FocusButton) view);
                Log.e("AAAAAA", "AAAAAAAA");
                btn.startLoading(() -> {
                    ShortProfile profile = shortProfileArrayList.get(position);
                    if (profile.isFan) {
                        String user_id = profile.id;
                        String fan_id = BasicInfo.mId;
                        new DeleteFromWatchRequest(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.e("error1", e.toString());
                                getActivity().runOnUiThread(btn::clickFail);
                            }
                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String resStr = response.body().string();
                                try {
                                    //JSONObject jsonObject = new JSONObject(resStr);
                                    profile.isFan = false;
                                    BasicInfo.removeFromWatchList(profile.id);
                                    getActivity().runOnUiThread(btn::clickSuccess);
                                } catch (Exception e) {
                                    Log.e("error2", e.toString());
                                    getActivity().runOnUiThread(btn::clickFail);
                                }

                            }


                        }, user_id, fan_id).send();

                    } else {
                        String fan_id = BasicInfo.mId;
                        String user_id = profile.id;
                        new AddToWatchRequest(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e("error1", e.toString());
                                getActivity().runOnUiThread(btn::clickFail);
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String resStr = response.body().string();
                                Log.e("response", resStr);
                                try {
                                    profile.isFan = true;
                                    BasicInfo.addToWatchList(profile);
                                    getActivity().runOnUiThread(btn::clickSuccess);
                                } catch (Exception e) {
                                    Log.e("error2", e.toString());
                                    getActivity().runOnUiThread(btn::clickFail);
                                }
                            }
                        }, user_id, fan_id).send();
                    }
                });
            } catch (Exception e) {

            }
        });
    }
}