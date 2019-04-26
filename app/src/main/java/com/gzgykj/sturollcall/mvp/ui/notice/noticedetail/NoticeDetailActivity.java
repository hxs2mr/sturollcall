package com.gzgykj.sturollcall.mvp.ui.notice.noticedetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;

import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseActivity;
import com.gzgykj.sturollcall.base.BaseFragment;
import com.gzgykj.sturollcall.mvp.ui.notice.NoticeContract;
import com.gzgykj.sturollcall.mvp.ui.notice.NoticePresenter;

import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Data on :2019/3/22 0022
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :通知详情
 */
public class NoticeDetailActivity extends BaseActivity<NoticePresenter> implements NoticeContract.View {
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;

    private String text;

    private Bundle bundle;
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_noticedetail;
    }

    @Override
    protected void initEventData() {
        setshowToolbarRight(false);
        setToolbarleftBack();
        setToolbarLeftTitle("通知详情");
        bundle = getIntent().getExtras();
        text = bundle.getString("TEXT");
        tv_title.setText(text);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
       // return super.onCreateFragmentAnimator();

        // 设置横向(和安卓4.x动画相同)
        //
         return new DefaultHorizontalAnimator();
    }

    @Override
    public void notice(List<String> test) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
