package com.example.myapplication.utils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/***************
 * [class] Http通用函数
 ***************/
public class Http {
    private static final String server_url = Global.SERVER_URL;                                             // 服务端 URL
    /**
     * HTTP 回调实例
     */
    public static Callback callbackExample_0 = new Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                // 打印返回结果
                Log.e("HttpResponse", response.toString());
                ResponseBody responseBody = response.body();
                String responseBodyString = responseBody != null ? responseBody.string() : "";
                Log.e("HttpResponse", responseBodyString);
                JSONObject jsonObject = new JSONObject(responseBodyString);
                boolean status = (Boolean) jsonObject.get("status");
                Log.e("HttpResponse", status ? "√√√√√√√√√√√√√√√√√√√√√√√√√√" : "××××××××××××××××××××××××××");
            } catch (JSONException e) {
                Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("HttpError", e.toString());
        }
    };
    private static HashMap<String, List<Cookie>> cookieStore = new HashMap<>();    // Cookie 存储
    private static CookieJar cookieJar = new CookieJar() {                         // CookieJar 实例
        @Override
        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
            if (Global.HTTP_DEBUG_MODE)
                Log.e("CookieSave", list.toString());
            cookieStore.put(httpUrl.host(), list);
        }

        @NotNull
        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
            if (Global.HTTP_DEBUG_MODE)
                Log.e("CookieLoad", cookies != null ? cookies.toString() : "No Cookie");
            return cookies != null ? cookies : new ArrayList<>();
        }
    };
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();     // 客户端 实例
    public static Callback callbackExample_1 = new Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                // 打印返回结果
                Log.e("HttpResponse", response.toString());
                ResponseBody responseBody = response.body();
                String responseBodyString = responseBody != null ? responseBody.string() : "";
                Log.e("HttpResponse", responseBodyString);
                JSONObject jsonObject = new JSONObject(responseBodyString);
                boolean status = (Boolean) jsonObject.get("status");
                Log.e("HttpResponse", status ? "√√√√√√√√√√√√√√√√√√√√√√√√√√" : "××××××××××××××××××××××××××");

                //new UpdateInfoPictureRequest(callbackExample_0, null).send();
            } catch (JSONException e) {
                Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("HttpError", e.toString());
        }
    };
    public static Callback callbackExample_2 = new Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try {
                // 打印返回结果
                Log.e("HttpResponse", response.toString());
                ResponseBody responseBody = response.body();
                String responseBodyString = responseBody != null ? responseBody.string() : "";
                Log.e("HttpResponse", responseBodyString);
                JSONObject jsonObject = new JSONObject(responseBodyString);
                boolean status = (Boolean) jsonObject.get("status");
                Log.e("HttpResponse", status ? "√√√√√√√√√√√√√√√√√√√√√√√√√√" : "××××××××××××××××××××××××××");

                //new GetInfoRequest(callbackExample_0, "I", null, null).send();
            } catch (JSONException e) {
                Log.e("HttpResponse", e.toString());
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("HttpError", e.toString());
        }
    };

    /**
     * 发送 GET 请求（异步）
     *
     * @param url      {String} 请求URL 例如 /api/get
     * @param query    {HashMap<String, String>} 请求参数
     * @param callback {Callback} 回调函数
     */
    public static void sendHttpGetRequest(String url, HashMap<String, String> query, okhttp3.Callback callback) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(server_url + url)).newBuilder();
        for (Map.Entry<String, String> entry : query.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        Request.Builder builder = new Request.Builder().url(urlBuilder.build());
        Request request = builder.get().build();
        okHttpClient.newCall(request).enqueue(callback);
        if (Global.HTTP_DEBUG_MODE)
            Log.e("HttpRequest", request.toString());
    }

    /**
     * 发起 POST 请求（异步）
     *
     * @param url        {String} 请求URL 例如 /api/post
     * @param param      {HashMap<String, String>} 参数
     * @param fileKey    {String} 文件关键字
     * @param fileObject {File} 文件对象
     * @param callback   {Callback} 回调函数
     */
    public static void sendHttpPostRequest(String url, HashMap<String, String> param, String fileKey, File fileObject, okhttp3.Callback callback) {
        if (param.isEmpty() && (fileKey == null || fileObject == null))
            param.put("whatever", "whatever");
        MultipartBody.Builder mulBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : param.entrySet()) {
            mulBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        if (fileKey != null && fileObject != null) {
            mulBuilder.addFormDataPart(fileKey, fileObject.getName(), RequestBody.create(fileObject, MediaType.parse("image/jpeg")));
        }
        Request request = new Request.Builder().url(server_url + url).post(mulBuilder.build()).build();
        okHttpClient.newCall(request).enqueue(callback);
        if (Global.HTTP_DEBUG_MODE)
            Log.e("HttpRequest", request.toString());
    }

    /**
     * HTTP 测试脚本
     */
    public static void testRequest() {
        Log.e("Test", "*****************");
        //new ClearAllIntentionRequest(Http.callbackExample_0).send();
    }

}

