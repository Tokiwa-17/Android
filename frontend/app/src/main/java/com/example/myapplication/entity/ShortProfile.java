package com.example.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.utils.Global;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户的简要信息
 */
public class ShortProfile implements Parcelable {
    public static final Creator<ShortProfile> CREATOR = new Creator<ShortProfile>() {
        @Override
        public ShortProfile createFromParcel(Parcel in) {
            return new ShortProfile(in);
        }

        @Override
        public ShortProfile[] newArray(int size) {
            return new ShortProfile[size];
        }
    };
    public String id;
    public String name;
    public String url;
    public String intro;
    public boolean isFan;

    public ShortProfile() {

    }

    public ShortProfile(String id, String name, String url, String intro, boolean isFan) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.intro = intro;
        this.isFan = isFan;
    }

    public ShortProfile(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readString();
        intro = in.readString();
        isFan = in.readByte() != 0;
    }

    public ShortProfile(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("id");
        this.name = jsonObject.getString("name");
        this.url = Global.SERVER_URL + jsonObject.getString("url");
        this.intro = jsonObject.getString("introduction");
    }

    /*利用writeToParcel方法写入对象*/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(intro);
        dest.writeByte((byte) (isFan ? 1 : 0));
    }

    @Override
    public int describeContents() { return 0; }
}
