package com.jiesai.camera.Utils.video;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liangxy on 2017/5/23.
 */
public class VideoUtil {
    private static final String TAG = "uploadVideoFile";
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";
    private static final int TIME_OUT = 10 * 10000000;   //超时时间
    //上传视频文件
    public static String uploadFile(String imageFilePath, String actionUrl) {
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(TIME_OUT);
            con.setConnectTimeout(TIME_OUT);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            con.setRequestMethod("POST");

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            File file = new File(imageFilePath);
            if (file != null) {
                FileInputStream fStream = new FileInputStream(file);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize]; // 创建一个数据来保存文件数据
                // 获取文件大小
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }

                fStream.close();
                ds.flush();

                InputStream is = con.getInputStream();
                int ch;
                StringBuffer b = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    b.append((char) ch);
                }
                ds.close();
                int res = con.getResponseCode();
                Log.i(TAG, "response code:" + res);
                if (res == 200) {
                    return SUCCESS;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FAILURE;
    }
    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     * Created by liangxy on 2017/5/25 16:19
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;

        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
