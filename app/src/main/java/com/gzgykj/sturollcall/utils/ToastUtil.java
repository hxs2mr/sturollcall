package com.gzgykj.sturollcall.utils;

import android.graphics.Color;
import android.widget.Toast;


import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.app.App;

import es.dmoral.toasty.Toasty;


/**
 ***********************
 * 功 能:基于TastyToast 的toast再次封装
 * *********************
 */
public class ToastUtil {

    public static void Success(String msg){
        Toasty.success(App.getInstance(),msg,Toast.LENGTH_SHORT,true).show();
    }
    public static void Error(String msg){
        Toasty.error(App.getInstance(),msg, Toast.LENGTH_SHORT,true).show();
    }
    public static void Info(String msg){
        //TastyToast.makeText(App.getInstance(),msg,1,TastyToast.INFO).show();
        Toasty.info(App.getInstance(),msg,Toast.LENGTH_SHORT,true).show();
    }
    public static void Warning(String msg){
        Toasty.warning(App.getInstance(),msg,Toast.LENGTH_SHORT,true).show();
    }
    public static void Normal(String msg){
        Toasty.normal(App.getInstance(),msg).show();
    }
    /**
     * 带有图标的常用Toast：
     */
    public static void Icon(String msg) {
        Toasty.normal(App.getInstance(),msg, R.mipmap.ic_launcher).show();
    }
    /**
     * 自定义Toast
     */
    public static  void  cusom(String msg){
        //Toasty.custom(App.getInstance(),msg,R.mipmap.ic_launcher, Color.BLUE,Color.RED,Toast.LENGTH_SHORT,true,true).show();
    }
}
