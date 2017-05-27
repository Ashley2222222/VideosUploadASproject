package com.jiesai.camera.Utils.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jiesai.camera.Utils.PublicUtil;

public class Bimp {
    public static int max = 0;
    public static int maxShow = 0;


    private static ArrayList<ImageItem> tempLastChosenBitmap = new ArrayList<ImageItem>();   //上一次选择了的所有图片的
    private static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();   //选择的图片的临时列表
    private static ArrayList<String> tempSelectBitmapName = new ArrayList<String>();   //选择的图片的名字临时列表
    private static ArrayList<ImageItem> tempShowBitmap = new ArrayList<ImageItem>();   //gridview一页显示的图片的临时列表

    //图片
    public static ArrayList<ImageItem> getTempSelectBitmap() {
        return tempSelectBitmap;
    }

    public static void setTempSelectBitmap(ArrayList<ImageItem> tempSelectBitmap) {
        if (!PublicUtil.checkEmptyList(tempSelectBitmap)) {
            Bimp.tempSelectBitmap = tempSelectBitmap;
            max = tempSelectBitmap.size();
        }
    }

    public static int getListSize() {
        return tempSelectBitmap.size();
    }

    public static void clearList() {
        if (!PublicUtil.checkEmptyList(tempLastChosenBitmap))
            tempLastChosenBitmap.clear();
        if (!PublicUtil.checkEmptyList(tempSelectBitmap)) {
            tempSelectBitmap.clear();
            max = 0;
        }
        if (!PublicUtil.checkEmptyList(tempShowBitmap)) {
            tempShowBitmap.clear();
            maxShow = 0;
        }
    }

    public static void clearTempList() {
        tempSelectBitmap.clear();
        max = 0;
    }

    public static void addItem(ImageItem item, String name) {
        tempSelectBitmap.add(item);
        tempSelectBitmapName.add(name);
//        tempShowBitmap.add(item);
        max++;
    }

    public static void addItem(ImageItem item) {
        tempSelectBitmap.add(item);
        max++;
    }

    public static void removeItem(ImageItem item) {
        tempSelectBitmap.remove(item);
        max--;
    }

    public static void removeItem(int location) {
        tempSelectBitmap.remove(location);
        max--;
    }

    public static ImageItem getItem(int location) {
        return tempSelectBitmap.get(location);
    }

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    //图片名字存取，加载图片时用
    public static ArrayList<String> getTempSelectBitmapName() {
        return tempSelectBitmapName;
    }

    public static void setTempSelectBitmapName(ArrayList<String> tempSelectBitmapName) {
        Bimp.tempSelectBitmapName = tempSelectBitmapName;
    }

    //gridview当前页要显示的图片
    public static ArrayList<ImageItem> getTempShowBitmap() {
        return tempShowBitmap;
    }

    public static void setTempShowBitmap(ArrayList<ImageItem> tempShowBitmap) {
        if (!PublicUtil.checkEmptyList(tempShowBitmap)) {
            Bimp.tempShowBitmap = tempShowBitmap;
            maxShow = tempShowBitmap.size();
        }
    }
    public static void delFromTempShowBitmap(ImageItem del) {
        if (!PublicUtil.checkEmptyList(tempShowBitmap)) {
            tempShowBitmap.remove(del);
            maxShow = tempShowBitmap.size();
        }
    }
    public static int getShowListSize() {
        return tempShowBitmap.size();
    }

    public static ImageItem getShowItem(int location) {
        return tempShowBitmap.get(location);
    }

    //    public static void addTempListItem(ImageItem iiNew) {
//        boolean has = false;
//        for (ImageItem ii : tempSelectBitmap) {
//            String nameNew = iiNew.getImageFileName();
//            String nameIn = ii.getImageFileName();
//            if (nameNew.contains(nameIn)||nameIn.contains(nameNew)) {
//                has = true;
//                break;
//            }
//        }
//        if (!has) {
//            tempSelectBitmap.add(iiNew);
//            max++;
//        }
//    }
    public static void addShowItem(ImageItem item) {
        tempShowBitmap.add(item);
        maxShow++;
    }

    public static void addItemsToAll(List<ImageItem> iiList) {
        for (ImageItem ii : iiList) {
            addItemToAll(ii);
        }
    }

    public static void addItemToAll(ImageItem iiNew) {
        boolean has = false;
        for (ImageItem ii : tempLastChosenBitmap) {
            String nameNew = iiNew.getImageFileName();
            String nameIn = ii.getImageFileName();
            if (nameNew.contains(nameIn) || nameIn.contains(nameNew)||nameNew.equals(nameIn)) {
                has = true;
                break;
            }
        }
        if (!has) {
            tempLastChosenBitmap.add(iiNew);
        }
    }
    public static void delItemFromAll(ImageItem iiDel) {
        boolean del = false;
        for (ImageItem ii : tempLastChosenBitmap) {
            String nameNew = iiDel.getImageFileName();
            String nameIn = ii.getImageFileName();
            if (nameNew.contains(nameIn) || nameIn.contains(nameNew)) {
                del = true;
                break;
            }
        }
        if (del) {
            tempLastChosenBitmap.remove(iiDel);
        }
    }
    //根据名称删除指定图片
    public static void delItemFromAll(String delName) {
        boolean del = false;
        int delINdex =-1;
        for (ImageItem ii : tempLastChosenBitmap) {
            String nameNew = delName;
            String nameIn = ii.getImageFileName();
            if (nameNew.contains(nameIn) || nameIn.contains(nameNew)) {
                del = true;
                delINdex = tempLastChosenBitmap.indexOf(ii);
                break;
            }
        }
        if (del) {
            tempLastChosenBitmap.remove(delINdex);
        }
    }
    public static int getTempLastChosenBitmapSize() {
        return tempLastChosenBitmap.size();
    }

    public static ArrayList<ImageItem> getTempLastChosenBitmap() {
        return tempLastChosenBitmap;
    }

    public static void setTempLastChosenBitmap(ArrayList<ImageItem> tempLastChosenBitmap) {
        Bimp.tempLastChosenBitmap = tempLastChosenBitmap;
    }

    public static int getLastChosenListSize() {
        return tempLastChosenBitmap.size();
    }


    public static void clearTempShowBitmap() {
        tempShowBitmap=new ArrayList<ImageItem>();
    }

}
