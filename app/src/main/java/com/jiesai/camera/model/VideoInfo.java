package com.jiesai.camera.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jiesai.camera.Utils.photo.ImageItem;

/**
 * Created by liangxy on 2017/5/26.
 */
public class VideoInfo implements Parcelable {



    private String path;//路径
   private String fileName;//文件名称
    private ImageItem ii;//缩略图
    private Integer state;//检测项目状态 1 合格、0 不合格

    protected VideoInfo(Parcel in) {
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
        // 2.序列化对象
        path = in.readString();
        fileName = in.readString();
    }
    public VideoInfo() {
    }
    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：
    // android.os.BadParcelableException:
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
    // 5.反序列化对象
    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
        public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideoInfo videoInfo = (VideoInfo) o;

        if (path != null ? !path.equals(videoInfo.path) : videoInfo.path != null) return false;
        return fileName != null ? fileName.equals(videoInfo.fileName) : videoInfo.fileName == null;

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ImageItem getIi() {
        return ii;
    }

    public void setIi(ImageItem ii) {
        this.ii = ii;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(fileName);
    }
}
