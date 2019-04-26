package com.gzgykj.sturollcall.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.gzgykj.basecommon.utils.ActivityManager;
import com.gzgykj.basecommon.utils.ActivityUtil;
import com.gzgykj.sturollcall.R;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import me.yokeyword.fragmentation_swipeback.core.ISwipeBackActivity;

public abstract class BaseMainActivity extends SwipeBackActivity implements ISwipeBackActivity {
    private Unbinder  mUnbinder;
    protected Activity mActivity;

    @BindView(R.id.toolbar_activity)
    Toolbar toolbar_activity;

    @BindView(R.id.left_title_tv)
    AppCompatTextView left_title_tv;//返回的当前标题

    @BindView(R.id.left_image)
    AppCompatImageView left_image;//返回

    @BindView(R.id.icon_video)
    AppCompatImageView icon_video;  //语音图标

    @BindView(R.id.right_tv)
    AppCompatTextView right_tv; //语音通话文字


    final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //将window背景设置为空
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
        setContentView((Integer) setLayout());
        mDelegate.onCreate(savedInstanceState);
        //StatusBarUtil.setTranslucent(this,30);
        mUnbinder = ButterKnife.bind(this);
        getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_LEFT);
        mActivity = this;
        onViewCreatre();
        initToolbar();
        initEventData();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.onPostCreate(savedInstanceState);
    }

    /**
     * 第一部分   **************************设置Toolbar
     */
    /**
     */
    protected   void setToolbarleftBack() {
        left_title_tv.setVisibility(View.VISIBLE);
        left_image.setImageResource(R.mipmap.icon_back);
    }

    /**
     * 设置文字颜色
     * @param color
     */
    protected   void setToolbarleftTvColor(String color) {
        left_title_tv.setTextColor(Color.parseColor(color));
    }

    /**
     * 设置右边语音聊天不可见
     * @param ishow
     */
    protected  void setshowToolbarRight(boolean ishow)
    {
        if(!ishow)
        {
            right_tv.setVisibility(View.INVISIBLE);
            icon_video.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置Toolbar的背景
     * @param color
     */
    protected void setToolbarBackground(int color) {
        toolbar_activity.setBackgroundColor(color);
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        boolean IsNatave = setIsNavigationIconShow(true);
        int menuLayoutId = setMenuLayoutId();//溢出菜单布局id
        Toolbar.OnMenuItemClickListener onMenuItemClickListener = setMenuItemClickListener();//溢出菜单点击事件
        if (onMenuItemClickListener!=null){
            right_tv.setVisibility(View.INVISIBLE);
            icon_video.setVisibility(View.INVISIBLE);
        }
        if(IsNatave)
        {
            left_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    left_image.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.getInstance().closeActivity(mActivity);
                        }
                    },0);
                }
            });

            left_title_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    left_title_tv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.getInstance().closeActivity(mActivity);
                        }
                    },0);
                }
            });
        }
        //可换成字体图标的方式添加图标

        if (menuLayoutId != 0 && onMenuItemClickListener != null) {
            //设置 Toolbar menu
            toolbar_activity.inflateMenu(menuLayoutId);
            // 设置溢出菜单的图标 --这个图标可传  可不传
            toolbar_activity.setOverflowIcon(getResources().getDrawable(R.mipmap.icon_menu));
            // 设置menu item 点击事件
            toolbar_activity.setOnMenuItemClickListener(onMenuItemClickListener);
        }
    }

    /**
     * 设置溢出菜单的点击事件---还需设置溢出菜单布局
     *
     * @return
     */
    protected Toolbar.OnMenuItemClickListener setMenuItemClickListener() {
        return null;
    }
    /**
     * 设置溢出菜单布局---
     * 还需设置溢出菜单的点击事件
     *
     * @return
     */
    protected int setMenuLayoutId() {
        return 0;
    }
    /**
     * 设置左面的标题
     * @param title
     * @return
     */
    protected  void setToolbarLeftTitle(String title)
    {
        left_title_tv.setText(title);
    }

    /**
     * 设置左面的标题
     */
    protected  void setToolbarLeftTV(String title)
    {
        left_title_tv.setText(title);
    }

    /**
     * 是否显示返回按钮
     */
    protected  boolean setIsNavigationIconShow(boolean show)
    {
        return  show;
    }
    /**
     * 设置Toolbat右边的点击时间(语音聊天)
     * @param clickListener
     */
    protected void setToolbarMoreClickListener(View.OnClickListener clickListener) {
        right_tv.setOnClickListener(clickListener);
        icon_video.setOnClickListener(clickListener);
    }

    /**
     * 左面的点击事件
     * @param clickListener
     */
    protected void setToolbarleftClickListener(View.OnClickListener clickListener) {
        left_image.setOnClickListener(clickListener);
        left_title_tv.setOnClickListener(clickListener);
    }


    /**
     * 添加已经创建好的Activity  入栈
     */
    protected  void onViewCreatre(){
        ActivityManager.getInstance().add(this);
    }


    /**
     * 第二部分 ****************************************初始化Frgamentation
     */

    /**
     * 获取当前父类实例
     * @return
     */
    @Override
    public SupportActivityDelegate getSupportDelegate() {
        return  mDelegate;
    }

    /**
     * 操作非回退栈的Fragment
     * @return
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return  mDelegate.extraTransaction();
    }

    @Override
    public void onBackPressedSupport() {
        ActivityUtil.getInstance().closeActivity(this);
        mDelegate.onBackPressedSupport();
    }

    /**
     * 获取设置的全局动画
     * @return
     */
    @Override
    public FragmentAnimator getFragmentAnimator() {
        return  mDelegate.getFragmentAnimator();
    }

    /**
     * 设置Fragments内的全局动画
     * @param fragmentAnimator
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * 构建Fragment转场动画
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return super.onCreateFragmentAnimator();
    }

    /**
     * 前面的事务全部执行后 执行该Action
     */
    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    // *********以下方法可选

    //加载根布局
    public void loadRootFragment(int containerId, @NonNull ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    //挑战fragment
    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
    }

    /**
     * fragment退出
     */
    public void pop() {
        mDelegate.pop();
    }

    /**
     * 初始化时间和数据
     */
    protected abstract void initEventData();

    /**
     * 返回布局XML
     * @return
     */
    public  abstract Object setLayout();

    //释放资源
    @Override
    protected void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
        mUnbinder.unbind();
    }
}
