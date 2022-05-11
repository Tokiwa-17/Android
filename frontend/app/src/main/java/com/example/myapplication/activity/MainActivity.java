package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.request.base.BaseGetRequest;
import com.example.myapplication.request.testRequest.testGet;
import com.example.myapplication.request.testRequest.testGet2;
import com.example.myapplication.request.testRequest.testPost;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private String CALLBACK = "callback";
    private TextView myTextView = null;
    private Button myLogin = null;
    private EditText myAccount = null;
    private EditText myPassword = null;
    private String msg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTextView = findViewById(R.id.myTextView);
        myLogin = findViewById(R.id.myLogin);
        myAccount = findViewById(R.id.myAccount);
        myPassword = findViewById(R.id.myPassword);
    }

    public void updateMsg(View v) {
        new testGet(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                msg = response.body().string();
                Log.e("MSG", msg);
            }
        }).send();
    }

    public void updateMsg2(View v) {
        new testGet2(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e(CALLBACK, "onResponse");
            }
        }).send();
    }

    public void Login(View v) {
        String Account = myAccount.getText().toString();
        String Password = myPassword.getText().toString();
        new testPost(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e(CALLBACK, "onResponse");
            }
        }, "", Account, Password).send();
    }
}