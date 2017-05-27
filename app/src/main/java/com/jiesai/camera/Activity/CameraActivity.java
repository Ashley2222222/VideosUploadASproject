package com.jiesai.camera.Activity;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.jiesai.camara.R;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


import com.jiesai.camera.AsyncTask.UploadVideoFileTask;

import com.jiesai.camera.Utils.PublicUtil;
import com.jiesai.camera.Utils.StorageUtils;
import com.jiesai.camera.Utils.photo.Bimp;
import com.jiesai.camera.Utils.photo.ImageItem;
import com.jiesai.camera.Utils.photo.PublicWay;
import com.jiesai.camera.Utils.video.VideoUtil;
import com.jiesai.camera.model.VideoInfo;
import com.jiesai.camera.photo.GridAdapter;
import com.jiesai.camera.view.GridView.MyGridView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CameraActivity extends Activity implements View.OnClickListener {
    String filePath;
    Context context = this;
    public final static int VEDIO = 101;
    public final static int VEDIO_CHOSEN = 102;

    @Bind(R.id.text)
    TextView tv;
    @Bind(R.id.btn_upload)
    Button btnUpload;
    @Bind(R.id.vv)
    VideoView vv;
    @Bind(R.id.btn_del)
    Button btnDel;
//    private static List<String> names = new ArrayList<>();//存放名称
    private static List<String> paths = new ArrayList<>();//存放路径
    String path;//选中播放的视频地址
    private static String folderPath = Environment.getExternalStorageDirectory().getPath() + "/videoTest/";//存放新拍摄的视频
    public static Bitmap bimap;//在BitmapCache.java中用到
    ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();

    //签名
    @Bind(R.id.gv)
    MyGridView gridview;
    public static GridAdapter gridAdapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        btnUpload.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        vv.setMediaController(new MediaController(CameraActivity.this));
        //弹出框，自底向上弹出3个按钮用于拍照、选择图片、取消
        pop = new PopupWindow(CameraActivity.this);

        View view = getLayoutInflater().inflate(R.layout.photo_item_popupwindows, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setText("拍视频");
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//拍视频
                pop.dismiss();
                ll_popup.clearAnimation();
                takeVedio();
            }
        });
        bt2.setText("从文件夹中选视频");
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bimp.clearList();
                Intent intent = new Intent(CameraActivity.this,
                        VideoListActivity.class);
                startActivityForResult(intent,VEDIO_CHOSEN);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        parentView = getLayoutInflater().inflate(R.layout.photo_activity_selectimg, null);
        gridAdapter = new GridAdapter(this);
        gridview.setAdapter(gridAdapter);
        setGridView();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.getShowListSize() && Bimp.getListSize() < PublicWay.num) {
                    Log.i("ddddddd", "----------");
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(CameraActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 200, 200);
                } else {
                    path = paths.get(arg2);
                    File newFile = new File(path);//播放选择的视频
                    Uri uri = Uri.fromFile(newFile);
                    vv.setVideoURI(uri);
                    vv.start();//播放视频
                }
            }
        });
        StorageUtils.initApp();
    }
//本app文件夹下所又视频文件
    private void setGridView() {
    if(!PublicUtil.checkEmptyList(paths)){
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
//            String name = names.get(i);
            Bitmap bp = VideoUtil.getVideoThumbnail(path, 108, 108, MediaStore.Images.Thumbnails.MINI_KIND);
            ImageItem ii = new ImageItem();
            ii.setBitmap(bp);
            ii.setImagePath(path);
//            ii.setImageFileName(name);
            tempSelectBitmap.add(ii);
        }
        Bimp.setTempSelectBitmap(tempSelectBitmap);
        Bimp.setTempShowBitmap(tempSelectBitmap);
        gridAdapter.update();
    }}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("test", "onActivityResult() requestCode:" + requestCode
                + ",resultCode:" + resultCode + ",data:" + data);
        switch (requestCode) {
            case CameraActivity.VEDIO:
                if (null != data) {
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    paths.add(path);

                    vv.setVideoURI(uri);
                    vv.start();//播放视频
                    setGridView();
                }
                break;
            case VEDIO_CHOSEN:
                getFileDir(folderPath);//本app相关文件夹下的视频
                if (null != data) {
                    //新选择的视频
                    Bundle bundle = data.getExtras();
                    List<VideoInfo>  chosenPath = bundle.getParcelableArrayList("videoChosenInfos");
                    for(VideoInfo vi:chosenPath) {
                        paths.add(vi.getPath());
//                        names.add(vi.getFileName());
                    }
                    setGridView();
                }
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_upload:
                uploadVedios();
                break;
            case R.id.btn_del:
                delSelectedVedio();
                break;
            default:
                break;
        }
    }

    //删除当前选择播放的视频
    private void delSelectedVedio() {
        if (vv.isPlaying()) {
            vv.pause();//停止播放视频
            vv.clearAnimation();
        }
        paths.remove(path);
        ImageItem iiDel = new ImageItem();
        for (ImageItem ii : tempSelectBitmap) {
            path.equals(ii.getImagePath());
            iiDel = ii;
            break;
        }
        tempSelectBitmap.remove(iiDel);
        Bimp.removeItem(iiDel);
        Bimp.delFromTempShowBitmap(iiDel);
        gridAdapter.update();
    }

    /**
     * 遍历视频list提交所有视频
     * Created by liangxy on 2017/5/25 15:23
     */
    private void uploadVedios() {

        for (String path : paths) {
            filePath = path;
            if (filePath != null) {
                Log.d("test", filePath);
                UploadVideoFileTask uploadVideoFileTask = new UploadVideoFileTask(this, filePath, tv, paths.size());
                uploadVideoFileTask.execute(filePath);
            }
        }
    }

    private void seleteVedio() {
        // TODO 启动相册
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CameraActivity.VEDIO_CHOSEN);
    }

    private void takeVedio() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//此值在最低质量最小文件尺寸时是0，在最高质量最大文件尺寸时是１.
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3600);// 拍摄时长，以秒为单位
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024);// 设置获取视频文件的大小，以字节为单位．
        intent.putExtra(MediaStore.EXTRA_FULL_SCREEN, true);// 以字节为单位
        intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);// 默认值为true,视频播放完后自动退出播放器。
        String pathName = Environment.getExternalStorageDirectory().getPath() + "/videoTest/" + System.currentTimeMillis() + ".mp4";
        File newFile = new File(pathName);
        Uri uri = Uri.fromFile(newFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CameraActivity.VEDIO);
    }

    /**
     * 获取指定路径下的所有视频文件和其绝对路径
     * Created by liangxy on 2017/5/25 15:21
     */
    public void getFileDir(String filePath) {
        try {
//            names = new ArrayList<String>();
            paths = new ArrayList<String>();
            File f = new File(filePath);
            File[] files = f.listFiles();// 列出所有文件

            // 将所有文件存入list中
            if (files != null) {
                int count = files.length;// 文件个数
                for (int i = 0; i < count; i++) {
                    File file = files[i];
//                    names.add(file.getName());
                    paths.add(file.getPath());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



    public static Uri geturi(Context context, android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                        Log.i("urishi", uri.toString());
                    }
                }
            }
        }
        return uri;
    }
    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String str = cursor.getString(column_index);
        return str;
    }
}