package com.gzgykj.sturollcall.dagg.component;

import android.app.Activity;

import com.gzgykj.sturollcall.dagg.mode.ActivityModel;
import com.gzgykj.sturollcall.dagg.scope.ActivityScope;
import com.gzgykj.sturollcall.mvp.ui.face.FaceActivity;
import com.gzgykj.sturollcall.mvp.ui.jxcall.JXActivity;
import com.gzgykj.sturollcall.mvp.ui.login.LoginActivity;
import com.gzgykj.sturollcall.mvp.ui.loss.lossdetail.LossDetailActivity;
import com.gzgykj.sturollcall.mvp.ui.main.MainActivity;
import com.gzgykj.sturollcall.mvp.ui.notice.noticedetail.NoticeDetailActivity;
import com.gzgykj.sturollcall.mvp.ui.setting.face.FaceAddActivity;
import com.gzgykj.sturollcall.mvp.ui.setting.face.FaceSettingActivity;
import com.gzgykj.sturollcall.mvp.ui.video.VideoActivity;

import dagger.Component;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on : Activity 依赖管理
 */
@ActivityScope
@Component(dependencies = {AppComponent.class},modules = ActivityModel.class)
public interface ActivityComponent {
    Activity getActivity();
    void inject(LoginActivity activity);
    //首页
    void inject(MainActivity activity);
    //指静脉识别
    void  inject(JXActivity activity);
    //人脸识别
    void inject(FaceActivity activity);

    //通知详情
    void inject(NoticeDetailActivity activity);

    //通知详情
    void  inject(LossDetailActivity activity);

    //添加人脸识别
    void inject(FaceSettingActivity activity);

    //语音通话
    void inject(VideoActivity activity);
    //添加面部特征
    void  inject(FaceAddActivity activity);
}
