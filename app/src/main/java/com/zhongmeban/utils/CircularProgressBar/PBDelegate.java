package com.zhongmeban.utils.CircularProgressBar;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Chengbin He on 2016/4/19.
 */
public interface PBDelegate {
    void draw(Canvas canvas, Paint paint);

    void start();

    void stop();

    void progressiveStop(CircularProgressDrawable.OnEndListener listener);
}
