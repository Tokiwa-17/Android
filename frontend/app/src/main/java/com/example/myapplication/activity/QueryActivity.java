package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
//import com.example.androidapp.adapter.HistoryListAdapter;
//import com.example.androidapp.request.search.DeleteRecordRequest;
//import com.example.androidapp.request.search.SearchHotRecordRequest;
//import com.example.androidapp.request.search.SearchRecordRequest;
//import com.google.android.flexbox.FlexboxLayout;
//import com.gyf.immersionbar.ImmersionBar;
import com.example.myapplication.adapter.HistoryListAdapter;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 查询中转界面
 */
public class QueryActivity extends BaseActivity {

    @BindView(R.id.top_bar)
    Toolbar toolbar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.historyList)
    RecyclerView historyList;

    private HistoryListAdapter historyListAdapter;

    private List<String> records;

    private List<String> hot;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        ButterKnife.bind(this);

        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                Intent intent = new Intent(getApplicationContext(), QueryResultActivity.class);
//                intent.putExtra("query", s);
//                startActivity(intent);
                QueryActivity.this.finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        historyList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        records = new ArrayList<>();
        hot = new ArrayList<>();
        historyListAdapter = new HistoryListAdapter(records, this);//初始化NameAdapter
        historyListAdapter.setRecyclerManager(historyList);//设置RecyclerView特性

        historyListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            deleteSearchHistory(records.get(position));
            adapter.remove(position);

        });

        historyListAdapter.setOnItemClickListener((adapter, view, position) -> {
            searchView.setQuery((String) adapter.getData().get(position), true);
        });
        getSearchHistory();
        getHotSearch();

        toolbar.setNavigationOnClickListener(v -> this.finish());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }


    private void deleteSearchHistory(String key) {
//        new DeleteRecordRequest(new okhttp3.Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error", e.toString());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                Log.e("response", resStr);
//                try {
//                    JSONObject jsonObject = new JSONObject(resStr);
//                } catch (JSONException e) {
//                }
//            }
//        }, key).send();
    }

    private void getSearchHistory() {
        loadService = LoadSir.getDefault().register(historyList, (Callback.OnReloadListener) v -> {

        });
//        new SearchRecordRequest(new okhttp3.Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error", e.toString());
//                loadService.showSuccess();
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                Log.e("response", resStr);
//                try {
//                    JSONObject jsonObject = new JSONObject(resStr);
//                    JSONArray jsonArray;
//                    jsonArray = (JSONArray) jsonObject.get("search_record_list");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        records.add(jsonArray.getString(i));
//                    }
//                    loadService.showSuccess();
//                } catch (JSONException e) {
//                    loadService.showSuccess();
//                }
//            }
//        }).send();
    }

    private void getHotSearch() {

//        new SearchHotRecordRequest(new okhttp3.Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error", e.toString());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                Log.e("response", resStr);
//                try {
//                    JSONObject jsonObject = new JSONObject(resStr);
//                    JSONArray jsonArray;
//                    jsonArray = (JSONArray) jsonObject.get("search_record_list");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        hot.add(jsonArray.getString(i));
//                    }
//                    runOnUiThread(() -> fillFlexBox(hot));
//                } catch (JSONException e) {
//                }
//            }
//        }).send();
//    }
    }
}