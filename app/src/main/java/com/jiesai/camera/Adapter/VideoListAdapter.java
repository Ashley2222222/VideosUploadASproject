/**
 *
 */
package com.jiesai.camera.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.jiesai.camara.R;
import com.jiesai.camera.Utils.UIUtil;
import com.jiesai.camera.model.VideoInfo;

import java.util.List;


/**
 * @Fields FieldListAdapter : 场所列表适配器
 *@author  liangxueyi
 * @since 2016/12/23 15:37
 */
public class VideoListAdapter extends BaseAdapter {
	public static double FONTSIZE;
	private List<VideoInfo> items = null;
	private Context context;

	public VideoListAdapter(Context context, List<VideoInfo> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int index) {
		return items.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final ViewHolder viewHolder;
		LayoutInflater inflater = null;
		final int currPosition = position;
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_video, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.iv = (ImageView) convertView.findViewById(R.id.thumb);
			viewHolder.cbState = (CheckBox) convertView.findViewById(R.id.cbState);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.cbState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {//合格
						buttonView.setButtonDrawable(R.drawable.check_on);
						items.get(currPosition).setState(1);
					}else{
						buttonView.setButtonDrawable(R.drawable.check);
						items.get(currPosition).setState(0);
					}

			}});
		viewHolder.tv_name.setText(items.get(currPosition).getFileName()==null?"":items.get(currPosition).getFileName());
		viewHolder.iv.setImageBitmap(items.get(currPosition).getIi().getBitmap());

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					UIUtil.setCheckBox(viewHolder.cbState);
			}
		});
		this.notifyDataSetChanged();
		return convertView;
	}
	final class ViewHolder {
		private TextView tv_name;
		private ImageView iv;
		private CheckBox cbState;
	}

}
