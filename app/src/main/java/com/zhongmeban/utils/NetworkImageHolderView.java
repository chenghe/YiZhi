package com.zhongmeban.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.bigkoo.convenientbanner.holder.Holder;
import com.zhongmeban.R;

/**
 * Created by Chengbin He on 2016/5/6.
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, String data) {
        imageView.setImageResource(R.drawable.brand);
        //ImageLoader.getInstance().displayImage(data,imageView);

        ImageLoaderZMB.getInstance().loadImage(context,data,imageView,R.drawable.home_banner);
    }
}