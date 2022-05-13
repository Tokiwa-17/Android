package com.example.myapplication.utils;
import android.widget.ImageView;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

public class MyImageLoader {
    public static void loadImage(ImageView view, String url) {
        url = Global.SERVER_URL + url;
        Picasso.get().load(url).
                placeholder(R.drawable.ic_avatarholder).into(view);
    }

//    public static void loadImage(ImageView view) {
//        String url;
//        if (BasicInfo.TYPE.equals("S"))
//            url = new GetInfoPictureRequest("S", null, String.valueOf(BasicInfo.ID)).getWholeUrl();
//        else
//            url = new GetInfoPictureRequest("T", String.valueOf(BasicInfo.ID), null).getWholeUrl();
//        Picasso.get().load(url).placeholder(R.drawable.ic_avatarholder).into(view);
//    }

    public static void invalidate(String url) {
        Picasso.get().invalidate(url);
    }

//    public static void invalidate() {
//        String url;
//        if (BasicInfo.TYPE.equals("S"))
//            url = new GetInfoPictureRequest("S", null, String.valueOf(BasicInfo.ID)).getWholeUrl();
//        else
//            url = new GetInfoPictureRequest("T", String.valueOf(BasicInfo.ID), null).getWholeUrl();
//        Picasso.get().invalidate(url);
//    }
}
