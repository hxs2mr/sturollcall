package com.gzgykj.sturollcall.mvp.ui.video;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzgykj.basecommon.utils.ActivityUtil;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.adapter.VideoQinAdapter;
import com.gzgykj.sturollcall.base.BaseActivity;
import com.gzgykj.sturollcall.mvp.model.VideoQinBean;
import com.gzgykj.sturollcall.mvp.ui.login.LoginActivity;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.gzgykj.sturollcall.widget.VideoLoginDialog;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Data on :2019/3/25 0025
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :语音通话
 */
public class VideoActivity extends BaseActivity<VideoPresenter>implements VideoContract.View, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.qin_linear)
    LinearLayoutCompat qin_linear;//点击亲属之后展开的布局

    @BindView(R.id.recycleview)
    RecyclerView recycleview;//亲属列表
    private int mHiddenViewMeasuredHeight;

    private VideoLoginDialog dialog;//提示登录弹框
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_video;
    }
    @Override
    protected void initEventData() {
        setshowToolbarRight(false);
        setToolbarleftBack();
        setToolbarLeftTitle("语音通话");
        initRecycle();
    }
    private void initRecycle() {
        dialog  = new VideoLoginDialog(getContext(),this);
        mPresenter.initAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recycleview.setLayoutManager(manager);

        mPresenter.adapter.setOnItemClickListener(this);
        /**分割线**/
        recycleview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.parseColor("#ffffff"))
                .sizeResId(R.dimen.divider1)
                .build());
        recycleview.setAdapter(mPresenter.adapter);
        mPresenter.qin();
    }

    @OnClick({R.id.btn_admin,R.id.btn_qin,R.id.btn_call_cancle,R.id.btn_call})
    public void onclick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_admin://管理员

                //弹一个登录提示
                //ToastUtil.Success("管理员通话!");
                dialog.show();

                break;
            case R.id.btn_qin://亲属  有亲属的话  张开布局
                 initRecycleAnim();
                break;
            case R.id.btn_call_cancle://亲属  取消通话
                 animateClose(recycleview,qin_linear);//关闭动画
                break;
            case R.id.btn_call://亲属   确定通话
                animateClose(recycleview,qin_linear);//关闭动画
                ToastUtil.Success("开始语音通话!");
                break;
        }
    }

    private void initRecycleAnim() {

        recycleview.measure(0,0);
        int height=recycleview.getMeasuredHeight();//获取组件的高度
        // 获取布局的高度
        mHiddenViewMeasuredHeight =height;

        if (qin_linear.getVisibility()==View.VISIBLE) {
            animateClose(recycleview,qin_linear);//关闭动画
        } else {
            animateOpen(recycleview,qin_linear);//打开动画
        }
    }


    /**
     * 获取亲人列表的数据
     * @param beans
     */
    @Override
    public void qin(List<VideoQinBean> beans) {
        mPresenter.adapter.setNewData(beans);
    }

    /**
     * 关闭动画
     * @param view
     */
    private void animateClose(final View view,View view1) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                // 动画结束时隐藏view
                view1.setVisibility(View.GONE);
            }
        });
        animator.start();
    }
    /**
     * 打开动画
     * @param view
     */
    private void animateOpen(final View view,View view1) {
        view1.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(view, 0,
                mHiddenViewMeasuredHeight);
        animator.start();
    }
    private ValueAnimator createDropAnimator(final View view, int start, int end) {
        // 创建一个数值发生器
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mPresenter.adapter = (VideoQinAdapter) adapter;
        int size = mPresenter.adapter.getData().size();
        for (int i = 0; i < size; i++)
        {
            mPresenter.adapter.getData().get(i).setIsclick(false);
        }
        mPresenter.adapter.getData().get(position).setIsclick(true);
        mPresenter.adapter.notifyDataSetChanged();
    }
    /**
     * 跳转到登陆界面
     */

    public void Login()
    {
        ActivityUtil.getInstance().openActivity(this, LoginActivity.class);
    }
}
