package com.jiesai.camera.photo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jiesai.camara.R;

import com.jiesai.camera.Utils.photo.Bimp;
import com.jiesai.camera.Utils.photo.PublicWay;
import com.jiesai.camera.view.GridView.MyGridView;


/**
 * Created by Administrator on 2017/2/15.
 */


public class GridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int selectedPosition = -1;
    private boolean shape;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public GridAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void update() {
        loading();
    }

    public int getCount() {
        if (Bimp.getShowListSize() == PublicWay.numShow) {
            return PublicWay.numShow;
        }
        return (Bimp.getShowListSize() + 1);
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.photo_item_published_grida,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (((MyGridView) parent).isOnMeasure) {
            //如果是onMeasure调用的就立即返回
            return convertView;
        }
        //如果不是onMeasure调用的就可以正常操作了
        //赋值操作
        if (position == Bimp.getShowListSize()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    inflater.getContext().getResources(), R.drawable.photo_icon_addpic_unfocused));
            ViewGroup.LayoutParams lp;
            lp= holder.image.getLayoutParams();
            lp.width=135;
            lp.height=135;
            holder.image.setLayoutParams(lp);
            if (Bimp.getShowListSize() == PublicWay.num) {//达到最大图片数，不允许再加图片
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(Bimp.getShowItem(position).getBitmap());
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while(true){
                    //将max的值更新
                    if (Bimp.maxShow == Bimp.getShowListSize()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        Bimp.maxShow += 1;
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }

                }}
        }).start();
    }
}
