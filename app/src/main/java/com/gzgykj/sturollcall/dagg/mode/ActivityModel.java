package com.gzgykj.sturollcall.dagg.mode;

import android.app.Activity;

import com.gzgykj.sturollcall.dagg.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
@Module
public class ActivityModel {
    private Activity mActivity;

    public ActivityModel(Activity activity)
    {
        mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity()
    {
        return mActivity;
    }

}
