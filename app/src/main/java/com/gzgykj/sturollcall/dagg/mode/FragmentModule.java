package com.gzgykj.sturollcall.dagg.mode;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.gzgykj.sturollcall.dagg.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * **********************
 * 功 能:为Fragment提供Module
 * *********************
 */
@Module
public class FragmentModule {
    private Fragment mFragment;
    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }
    @Provides
    @FragmentScope
    public Activity provideActivity(){
        return mFragment.getActivity();
    }
}
