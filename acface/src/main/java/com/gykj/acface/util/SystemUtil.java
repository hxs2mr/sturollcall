package com.gykj.acface.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;



/**
 * Created by codeest on 2016/8/4.
 */
public class SystemUtil {


    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager)        context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }


    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.heightPixels;
    }

}
