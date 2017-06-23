package com.zhongmeban.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.zhongmeban.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chengbin He on 2016/10/27.
 */

public class ImageLoaderZMB {

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final ImageLoaderZMB INSTANCE = new ImageLoaderZMB();
    }


    //获取单例
    public static ImageLoaderZMB getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void loadImage(Context context, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            url = null;
        }
        Picasso.with(context)
            .load(url)
            .error(R.drawable.brand)
            .placeholder(R.drawable.brand)
            .into(imageView);
    }


    public void loadImage(Context context, String url, ImageView imageView, int errorResId) {
        if (TextUtils.isEmpty(url)) {
            url = null;
        }
        Picasso.with(context).load(url).placeholder(errorResId).error(errorResId).into(imageView);
    }


    /**
     * 将图片裁剪
     */
    public void loadTransImage(Context context, String url, int errorResId, Transformation transformation, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            url = null;
        }
        Picasso.with(context)
            .load(url)
            .placeholder(errorResId)
            .error(errorResId)
            .transform(transformation)
            .into(imageView);
    }


    public List loadBitMapList(Context context, String url) {
        final List<Bitmap> mBitmap = new ArrayList<>();
        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.i("hcb", "onBitmapLoaded");
                mBitmap.add(bitmap);
            }


            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("hcb", "onBitmapFailed");
            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("hcb", "onPrepareLoad");
            }
        });
        return mBitmap;
    }

}
