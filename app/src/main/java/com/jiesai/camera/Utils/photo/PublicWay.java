package com.jiesai.camera.Utils.photo;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放所有的list在最后退出时一起关闭
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:50:49
 */
public class PublicWay {
	public static List<Activity> activityList = new ArrayList<Activity>();
	
	public static int num = 200;//最多可选择照片数
	public static int numShow = 16;//gridview一页最多可显示照片数
}
