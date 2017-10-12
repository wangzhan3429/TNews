package wz.com.tnews.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;


/**
 * Created by v_wangzhan on 2017/9/1.
 */

public class News implements Parcelable {
    /*"_id":"59a8cfdc421aa901c1c0a8c7",
            "createdAt":"2017-09-01T11:11:24.81Z",
            "desc":"9-1",
            "time":"2017-09-01T12:55:52.582Z",
            "source":"chrome",
            "type":"福利",
            "url":"https://ws1.sinaimg.cn/large/610dc034ly1fj3w0emfcbj20u011iabm.jpg",
            "used":true,
            "who":"daimajia"*/

    @SerializedName("_id")
    public String id;
    public String desc;
    public String type;
    public String url;
    @SerializedName("publishedAt")
    public String time;
    public String source;
    @SerializedName("who")
    public String author;

    public String timestemp;

    public News(String desc, String url, String author, String time, String timestemp) {
        this.desc = desc;
        this.url = url;
        this.author = author;
        this.time = time;
        this.timestemp = timestemp;
    }

//    //     "time":"2017-09-01T12:55:52.582Z",  2017-10-10T12:41:34.882Z
//    public News formatTime(String timestamp) {
//        Log.i("wangzhan", "formatTime:... " + timestamp);
//        try {
//            if (timestamp != null && timestamp.contains("T")) {
//                String[] t = timestamp.split("T");
//                String[] t1 = t[1].split("\\."); // illegal escape character in string literal "yyyy-MM-dd HH:mm:ss"
//
//                String formateTime = t[0] + " " + t1[0];
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String ctime = String.valueOf(sdf.parse(formateTime).getTime() / 1000);
//                return new News(this.desc, this.url, this.author, ctime);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new News(this.desc, this.url, this.author, this.time);
//    }


    protected News(Parcel in) {
        in.readString();
        in.readString();
        in.readString();
        in.readString();
        in.readString();
        in.readString();
        in.readString();
        in.readString();
    }

    //    "推荐", "开源APP", "福利", "Android", "iOS ", " 拓展资源 ", " 前端"
    public int getType() {
        Log.i("getType", "getType: ..." + type);
        int newType = 0;
        switch (type) {
            case "推荐":
                newType = 0;
                break;
            case "App":
                newType = 1;
                break;
            case "福利":
                newType = 2;
                break;
            case "Android":
                newType = 3;
                break;
            case "iOS":
                newType = 4;
                break;
            case "拓展资源":
                newType = 5;
                break;
            case "前端":
                newType = 6;
                break;
        }
        return newType;
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(desc);
        dest.writeString(type);
        dest.writeString(url);
        dest.writeString(time);
        dest.writeString(source);
        dest.writeString(author);
        dest.writeString(timestemp);
    }

    public String toString() {
        return this.time + "," + this.url + "," + this.author + "," + this.desc + "," + this
                .type + "," + this.timestemp;
    }

    public News updateTimeStemp(String timestemp) {
        return new News(this.desc, this.url, this.author, this.time, timestemp);
    }
}
