package com.gzgykj.sturollcall.widget;

import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Data on :2019/3/22 0022
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class TextureVideoViewOutlineProvider extends ViewOutlineProvider {
    private float mRadius;

    private int leftMargin;
    private int topMargin;
    public TextureVideoViewOutlineProvider(float radius,int leftMargin,int topMargin) {
        this.mRadius = radius;
        this.topMargin = topMargin;
        this.leftMargin = leftMargin;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
        outline.setRoundRect(selfRect, mRadius);
    }
}
