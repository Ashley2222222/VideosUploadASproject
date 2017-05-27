package com.jiesai.camera.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jiesai.camara.R;
import com.jiesai.camera.Adapter.VideoListAdapter;
import com.jiesai.camera.AsyncTask.VedioScannerTask;
import com.jiesai.camera.model.VideoInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liangxy on 2017/5/26.
 */
public class VideoListActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.lv)
    ListView lv;
    VideoListAdapter adapter;
    @Bind(R.id.btnConfirm)
    Button btnConfirm;
    @Bind(R.id.ll_confirm)
    LinearLayout llConfirm;
    private List<VideoInfo> videoInfos = new ArrayList<VideoInfo>();
    private List<VideoInfo> videoChosenInfos = new ArrayList<VideoInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list);
        ButterKnife.bind(this);
        btnConfirm.setOnClickListener(this);
        llConfirm.setOnClickListener(this);
        initInfo();
    }

    private void initInfo() {
        VedioScannerTask scanTask = new VedioScannerTask(VideoListActivity.this, videoInfos, lv, adapter);
        scanTask.execute();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_confirm:
            case R.id.btnConfirm:
                getChosenVideos();
                break;
            default:
                break;
        }

    }

    private void getChosenVideos() {

        for(VideoInfo vi:videoInfos)
        {
            if(vi.getState()==1)
                videoChosenInfos.add(vi);
        }
        Intent intent = new Intent(VideoListActivity.this, CameraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("videoChosenInfos", (ArrayList<? extends Parcelable>) videoChosenInfos);
        intent.putExtras(bundle);
        setResult(1, intent);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }

        return super.onKeyDown(keyCode, event);

    }
}
