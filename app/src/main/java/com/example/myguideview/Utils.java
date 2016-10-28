package com.example.myguideview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class Utils {

    /**
     * ALPHA_8就是Alpha由8位组成
     ARGB_4444就是由4个4位组成即16位，
     ARGB_8888就是由4个8位组成即32位，
     RGB_565就是R为5位，G为6位，B为5位共16位
     *
     */
    public static Bitmap getBitmapForImgResourse(Context mContext, int imgId) {
        InputStream is = mContext.getResources().openRawResource(imgId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 2;
        Bitmap btp = BitmapFactory.decodeStream(is, null, options);
//        mImageView.setImageBitmap(btp);
//    btp.recycle();
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return btp;
    }
}
