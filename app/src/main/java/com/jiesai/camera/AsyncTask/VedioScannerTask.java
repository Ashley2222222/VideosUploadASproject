package com.jiesai.camera.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

import com.jiesai.camera.Adapter.VideoListAdapter;
import com.jiesai.camera.Utils.photo.ImageItem;
import com.jiesai.camera.Utils.video.VideoUtil;
import com.jiesai.camera.model.VideoInfo;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 遍历列出所有视频文件
 * Created by liangxy on 2017/5/26.
 */
public class VedioScannerTask extends AsyncTask<Void, Integer, List<VideoInfo>> {
    private List<VideoInfo> videoInfos = new ArrayList<VideoInfo>();
    private Activity context = null;
    private ListView lv;
    private VideoListAdapter adapter= null;
    // 加载框
    ProgressDialog progressDialog;
    public VedioScannerTask(Activity ctx,List<VideoInfo> videoInfos, ListView lv, VideoListAdapter adapter, ProgressDialog progressDialog) {
        this.context = ctx;
        this.videoInfos = videoInfos;
        this.lv = lv;
        this.adapter = adapter;
        this.progressDialog = progressDialog;
    }

    @Override
    protected List<VideoInfo> doInBackground(Void... params) {
        videoInfos = getVideoFile(videoInfos, Environment.getExternalStorageDirectory());
        videoInfos = filterVideo(videoInfos);
        Log.i("VideoSize", "视频数：" + videoInfos.size());
        return videoInfos;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<VideoInfo> videoInfos) {
        super.onPostExecute(videoInfos);
        adapter = new VideoListAdapter(context, videoInfos);
        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);
        progressDialog.dismiss();
    }

    /**
     * 获取视频文件
     *
     * @param list
     * @param file
     * @return
     */
    private List<VideoInfo> getVideoFile(final List<VideoInfo> list, File file) {

        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {

                String name = file.getName();

                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4")
                            || name.equalsIgnoreCase(".3gp")
                            || name.equalsIgnoreCase(".wmv")
                            || name.equalsIgnoreCase(".ts")
                            || name.equalsIgnoreCase(".rmvb")
                            || name.equalsIgnoreCase(".mov")
                            || name.equalsIgnoreCase(".m4v")
                            || name.equalsIgnoreCase(".avi")
                            || name.equalsIgnoreCase(".m3u8")
                            || name.equalsIgnoreCase(".3gpp")
                            || name.equalsIgnoreCase(".3gpp2")
                            || name.equalsIgnoreCase(".mkv")
                            || name.equalsIgnoreCase(".flv")
                            || name.equalsIgnoreCase(".divx")
                            || name.equalsIgnoreCase(".f4v")
                            || name.equalsIgnoreCase(".rm")
                            || name.equalsIgnoreCase(".asf")
                            || name.equalsIgnoreCase(".ram")
                            || name.equalsIgnoreCase(".mpg")
                            || name.equalsIgnoreCase(".v8")
                            || name.equalsIgnoreCase(".swf")
                            || name.equalsIgnoreCase(".m2v")
                            || name.equalsIgnoreCase(".asx")
                            || name.equalsIgnoreCase(".ra")
                            || name.equalsIgnoreCase(".ndivx")
                            || name.equalsIgnoreCase(".xvid")) {
                        VideoInfo video = new VideoInfo();
                        file.getUsableSpace();
                        video.setFileName(file.getName());
                        video.setPath(file.getAbsolutePath());
                        Bitmap bp = VideoUtil.getVideoThumbnail(file.getAbsolutePath(), 108, 108, MediaStore.Images.Thumbnails.MINI_KIND);
                        ImageItem ii = new ImageItem();
                        ii.setBitmap(bp);
                        video.setIi(ii);
                        video.setState(1);//默认选中
                        Log.i("tga", "name" + video.getPath());
                        list.add(video);
                        return true;
                    }
                    //判断是不是目录
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        });

        return list;
    }

    /**
     * 10M=10485760 b,小于10m的过滤掉
     * 过滤视频文件
     *
     * @param videoInfos
     * @return
     */
    private List<VideoInfo> filterVideo(List<VideoInfo> videoInfos) {
        List<VideoInfo> newVideos = new ArrayList<VideoInfo>();
        for (VideoInfo videoInfo : videoInfos) {
            File f = new File(videoInfo.getPath());
            if (f.exists() && f.isFile() && f.length() > 1048) {
                newVideos.add(videoInfo);
                Log.i("TAG", "文件大小" + f.length());
            } else {
                Log.i("TAG", "文件太小或者不存在");
            }
        }
        return newVideos;
    }

}
