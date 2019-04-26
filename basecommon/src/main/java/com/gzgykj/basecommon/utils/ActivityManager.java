package com.gzgykj.basecommon.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :activity 任务栈管理
 */
public class ActivityManager {
    private  static  ActivityManager instance;
    private  static Stack<Activity> activityStack;

    private ActivityManager()
    {
     activityStack  = new Stack<>();
    }

    /**
     * 单例 构建
     * @return
     */
    public static  ActivityManager getInstance(){
        if(instance ==null)
        {
            synchronized (ActivityManager.class)
            {
                if(instance == null)
                {
                    instance = new ActivityManager();
                }
            }
        }
        return  instance;
    }

    /**
     * 入栈
     * @param activity
     */
    public  void add(Activity activity)
    {
        activityStack.push(activity);
    }

    /**
     * 出栈
     * @return
     */
    public  Activity pop()
    {
        if(activityStack.isEmpty())
        {
            return null;
        }
        return  activityStack.pop();
    }

    /**
     * 用于异地登录或者退出时清除activity
     */
    public void clearActivityToLogin(Class<?> loginActivity) {
        while (!activityStack.isEmpty()) {
            Activity activity = activityStack.pop();
            if (activity.getClass().getSimpleName().equals(loginActivity.getSimpleName())) {
                continue;
            } else {
                remove(activity);
                activity.finish();
            }
        }
    }

    /**
     * 移除
     * @param activity
     */
    public  void  remove(Activity activity)
    {
        if(activityStack.size() > 0 && activity == activityStack.peek())
        {
            activityStack.pop();
        }else {
            activityStack.remove(activity);
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while (!activityStack.isEmpty()) {
            activityStack.pop().finish();
        }
    }
}
