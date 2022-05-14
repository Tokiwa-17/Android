package com.example.myapplication.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.activity.BaseActivity;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;
import com.rubengees.introduction.Slide;
import com.rubengees.introduction.interfaces.CustomViewBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * 加载动画、提示
 */
public class Hint {
    /******************************
     ************ Load ************
     ******************************/
    // Activity 进入加载状态
    public static void startActivityLoad(BaseActivity activity) {
        activity.loadService = LoadSir.getDefault().register(activity, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
            }
        });
    }

    // Activity 解除加载状态
    public static void endActivityLoad(BaseActivity activity) {
        if (activity.loadService != null)
            activity.loadService.showSuccess();
    }

    /******************************
     ************ Toast ***********
     ******************************/
    // 顶端显示短框提示
    public static void showShortTopToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    // 顶端显示长框提示
    public static void showLongTopToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    // 中间显示短框提示
    public static void showShortCenterToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // 中间显示长框提示
    public static void showLongCenterToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // 底端显示短框提示
    public static void showShortBottomToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    // 底端显示长框提示
    public static void showLongBottomToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    /******************************
     ************ Intro ***********
     ******************************/
    // 生成引导页面
//    public static List<Slide> generateSlides() {
//        List<Slide> result = new ArrayList<>();
//
//        result.add(new Slide()
//                .withTitle("T2S-APP")
//                .withDescription("【意向模块】\n公布意向  心明眼亮\n\n≧◉◡◉≦")
//                .withColorResource(R.color.coral)
//                .withImage(R.drawable.png_white_128px_intention)
//        );
//
//        result.add(new Slide()
//                .withTitle("T2S-APP")
//                .withDescription("【会话模块】\n即时聊天  亲密无间\n\n(✿◠‿◠)")
//                .withColorResource(R.color.dijon)
//                .withImage(R.drawable.png_white_128px_conversation)
//        );
//
//        result.add(new Slide()
//                .withTitle("T2S-APP")
//                .withDescription("【关注模块】\n立刻关注  绝不迷路\n\n（‐＾▽＾‐）")
//                .withColorResource(R.color.turquoise)
//                .withImage(R.drawable.png_white_128px_follow)
//        );
//
//        result.add(new Slide()
//                .withTitle("T2S-APP")
//                .withDescription("【推荐模块】\n精选推荐  黄金体验\n\n\\ (•◡•) /")
//                .withColorResource(R.color.larchmere)
//                .withImage(R.drawable.png_white_128px_recommend)
//        );
//        return result;
//    }
//
//    // 自定义引导页视图-1
//    public static class CustomViewBuilderImpl implements CustomViewBuilder {
//        @NonNull
//        @Override
//        public View buildView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
//            return inflater.inflate(R.layout.view_intro_1, parent, false);
//        }
//    }
//
//    // 自定义引导页视图-2
//    public static class CustomViewBuilderImp2 implements CustomViewBuilder {
//        @NonNull
//        @Override
//        public View buildView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
//            return inflater.inflate(R.layout.view_intro_2, parent, false);
//        }
//    }
//
//    // 自定义引导页视图-3
//    public static class CustomViewBuilderImp3 implements CustomViewBuilder {
//        @NonNull
//        @Override
//        public View buildView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
//            return inflater.inflate(R.layout.view_intro_3, parent, false);
//        }
//    }
}
