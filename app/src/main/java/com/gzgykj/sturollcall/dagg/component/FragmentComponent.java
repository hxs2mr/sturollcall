package com.gzgykj.sturollcall.dagg.component;

import android.app.Activity;

import com.gzgykj.sturollcall.dagg.mode.FragmentModule;
import com.gzgykj.sturollcall.dagg.scope.FragmentScope;
import com.gzgykj.sturollcall.mvp.ui.login.face.FaceLoginFragment;
import com.gzgykj.sturollcall.mvp.ui.login.pass.PasswordFragment;
import com.gzgykj.sturollcall.mvp.ui.loss.LossFragment;
import com.gzgykj.sturollcall.mvp.ui.notice.NoticeFragment;
import com.gzgykj.sturollcall.mvp.ui.notice.noticedetail.NoticeDetailActivity;
import com.gzgykj.sturollcall.mvp.ui.police.PoliceFragment;
import com.gzgykj.sturollcall.mvp.ui.rollcall.RollCallFragment;
import com.gzgykj.sturollcall.mvp.ui.setting.SettingFragment;
import com.gzgykj.sturollcall.mvp.ui.setting.face.FaceFragment;
import com.gzgykj.sturollcall.mvp.ui.sign.SignFragment;

import dagger.Component;

/**
 * Data on :2019/3/20 0020
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :fragment依赖
 */
@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();
    void inject(RollCallFragment fragment);//点名
    void inject(NoticeFragment fragment);
    void inject(LossFragment fragment);
    void inject(SignFragment fragment);
    void inject(SettingFragment fragment);
    void inject(NoticeDetailActivity fragment);
    void inject(FaceFragment faceFragment);//添加面部资料
    void inject(FaceLoginFragment faceLoginFragment);//人脸识别登录
    void inject(PasswordFragment fragment);//用户密码登录
    void inject(PoliceFragment fragment);
}
