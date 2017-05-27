package com.jiesai.camera.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.jiesai.camera.Utils.video.VideoUtil;

/**
 * @author liangxueyi
 * @Fields UploadPhotoFileTask :异步上传图片
 * @since 2017/2/27 13:29
 */

public class UploadVideoFileTask extends AsyncTask<String, Void, String> {
    public static final String requestURL = "http://192.168.22.68:8080/test1/servlet/Videoservlet";
    //            CommUrl.SERVICE_URL+ CommUrl.UPLOAD_VIDEO;
    private String filepath = "";
    private Activity context = null;
    private ProgressDialog pdialog;
    private TextView tv;
    private int size = 0;
    private static int i = 0;

    /**
     * 可变长的输入参数，与AsyncTask.exucute()对应
     */
    public UploadVideoFileTask(Activity ctx, String filepath, TextView tv, int size) {
        this.context = ctx;
        this.filepath = filepath;
        this.tv = tv;
        this.size = size;
        pdialog = ProgressDialog.show(context, "正在加载...", "系统正在处理您的请求");
    }

    @Override
    protected void onPostExecute(String result) {

        // 返回HTML页面的内容
        pdialog.dismiss();
        i++;
        if (i == size) {
            i = 0;
            if (VideoUtil.SUCCESS.equalsIgnoreCase(result)) {
                Toast.makeText(context, "上传成功!", Toast.LENGTH_SHORT).show();
                tv.setText("上传成功!");
            }
        }
        if (VideoUtil.FAILURE.equalsIgnoreCase(result)) {
            Toast.makeText(context, "上传失败!", Toast.LENGTH_SHORT).show();
            tv.setText("上传失败!");
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pdialog.dismiss();
        tv.setText("上传取消!");
    }

    @Override
    protected String doInBackground(String... params) {
        String str = "0";
        str = VideoUtil.uploadFile(filepath, requestURL);//执行上传
        return str;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

}
