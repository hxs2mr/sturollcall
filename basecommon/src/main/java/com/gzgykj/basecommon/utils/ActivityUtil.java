package com.gzgykj.basecommon.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on : Activity 管理
 */
public class ActivityUtil {

    /**
     * 获取当前Activitgy 名字
     */
    private  static  final String TAG = ActivityUtil.class.getSimpleName();
    private  static ActivityUtil instance = null;
    private  final List<Activity> activitys =new ArrayList<>();
   private ProgressDialog dialog;


    /** 判断是否是快速点击 */
    private static long lastClickTime=System.currentTimeMillis();
    /**
     *单例获取
     */
    public static  ActivityUtil getInstance()
    {
        if(instance== null)
        {
            synchronized (ActivityUtil.class)
            {
                if(instance == null)
                {
                    instance = new ActivityUtil();
                }
            }
        }
        return  instance;
    }

    /**
     * 保存Activity
     */
    public void save(Activity act){
        if(act!=null)
        {
            activitys.add(act);
        }
    }

    /**
     * 移出并注销所有Activity
     */
    public void finishAll() {
		for(int i=activitys.size()-1;i>=0;i--) {
			// 逆序才可以边移出边注销
			Activity act = activitys.remove(i);
			if(act!=null) act.finish();
		}

    }

    /**
     * 移除Activity
     */
    public void remove(Activity act){
        activitys.remove(act);
    }

    /**
     * 显示确认对话框
     * @param context 上下文
     * @param title   标题
     * @param message 提示信息
     */
    public void showConfirmDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    /**
     * 不带bundle的Activity跳转-带有转场动画的
     */
    public void openActivity(Activity activity, Class<?> clazz) {
        if(!isFastDoubleClick())
        {
            openActivity(activity, clazz, null, SceneAnim.AnimType.TOP_IN);
        }
    }

    /**
     * 带bundle
     */

    public  void openActivity(Activity activity,Class<?> clazz,Bundle bundle)
    {
        if(!isFastDoubleClick())
        {
            openActivity(activity,clazz,bundle,SceneAnim.AnimType.RIGHT_IN);
        }

    }

    /**
     * 带bundle的Activity跳转-带有转场动画的 可指定转场动画
     *
     * @param activity
     * @param clazz
     * @param bundle
     * @param animInType
     */
    public void openActivity(Activity activity, Class<?> clazz, Bundle bundle, SceneAnim.AnimType animInType) {

        Intent intent = new Intent(activity, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
          activity.startActivity(intent);
          SceneAnim.openActivityWithAnim(activity, animInType);
    }

    /**
     * 关闭当前界面--带有转场动画的
     *
     * @param activity
     */
    public void closeActivity(Activity activity) {
        activity.finish();
        //加上动画的时候  侧滑关闭之后 还有一小段动画
        SceneAnim.closeActivityWithAnim(activity);
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;

    }

}
