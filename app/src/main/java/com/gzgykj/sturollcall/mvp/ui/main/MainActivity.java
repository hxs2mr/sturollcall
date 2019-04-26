package com.gzgykj.sturollcall.mvp.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gykj.jxfvlibrary.listener.OnJxFvListener;
import com.gykj.jxfvlibrary.manager.JXFvManager;
import com.gzgykj.basecommon.model.jxrealm.DeviceRealmManager;
import com.gzgykj.basecommon.model.jxrealm.DeviceidRealm;
import com.gzgykj.basecommon.model.jxrealm.IRealmListener;
import com.gzgykj.basecommon.utils.ActivityUtil;
import com.gzgykj.basecommon.utils.Constants;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.app.App;
import com.gzgykj.sturollcall.base.BaseActivity;
import com.gzgykj.sturollcall.base.BaseMainFragment;
import com.gzgykj.sturollcall.cache.ACache;
import com.gzgykj.sturollcall.mq.RabbiMqEngine;
import com.gzgykj.sturollcall.mvp.model.CityInfo;
import com.gzgykj.sturollcall.mvp.model.MainTimeBean;
import com.gzgykj.sturollcall.mvp.model.StuListBean;
import com.gzgykj.sturollcall.mvp.ui.loss.LossFragment;
import com.gzgykj.sturollcall.mvp.ui.notice.NoticeFragment;
import com.gzgykj.sturollcall.mvp.ui.police.PoliceFragment;
import com.gzgykj.sturollcall.mvp.ui.rollcall.RollCallFragment;
import com.gzgykj.sturollcall.mvp.ui.setting.SettingFragment;
import com.gzgykj.sturollcall.mvp.ui.sign.SignFragment;
import com.gzgykj.sturollcall.mvp.ui.video.VideoActivity;
import com.gzgykj.sturollcall.utils.SystemUtil;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :Fragment 管理 以及Raggbitmq初始化
 */
public class MainActivity extends BaseActivity<MainPresenter>implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.fl_container)
    FrameLayout frameLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private View headerView;

    TextView main_time_tv ;//时间mm:dd

    TextView main_week_tv;//星期几

    TextView main_date_tv;//年月日

    TextView main_calendar_tv;//老列月日
    TextView main_address_tv;//城市

    private ACache mACache;
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    private RxPermissions rxPermissions;


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }
    @Override
    public Object setLayout() {
        return R.layout.activity_main;
    }
    @Override
    protected void initEventData() {
        EventBus.getDefault().register(this);
        rxPermissions = new RxPermissions(this);
      //  StatusBarUtil.setTranslucentForDrawerLayout(this,drawerLayout);
        mACache = ACache.get(getContext());
        setSwipeBackEnable(false); // 是否允许滑动边缘退出
        headerView = navigationView.getHeaderView(0);//获取headerlayout

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_rollcall);   //起始点击的位置
        navigationView.setItemIconTintList(null);
        initheader();
        //时间  星期
        mPresenter.time();
        showDrawerLocation();

        /**初始化frgamnet**/
        initfragment();


        setToolbarleftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
            }

            /**
             * 当一个抽屉被完全打开的时候被调用
             */
            @Override
            public void onDrawerOpened(@NonNull View view) {
                Logger.i("HXS 抽屉打开");
            }


            /**
             * 当一个抽屉被完全关闭的时候被调用
             */
            @Override
            public void onDrawerClosed(@NonNull View view) {
                Logger.i("HXS 抽屉关闭");
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        /**
         * 点击语音通话
         */
        setToolbarMoreClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.getInstance().openActivity(MainActivity.this, VideoActivity.class);
            }
        });
        nedd_permissions();

        //初始化指静脉
        mPresenter.initRealmData(654321);

        if(SystemUtil.isNetworkConnected()){

            App.getInstance().connectRabbitMq("654321"); //  建立通道链接  用于接收消息

            //MQ链接

            RabbiMqEngine.getRabbiMqEngine().sendMessage(true, Constants.MQinit,0,0,0,0); //发送消息初始化


             // 获取学生信息

            RabbiMqEngine.getRabbiMqEngine().sendStuDataMessage(true);
        }else {
            ToastUtil.Warning("网络未连接!");
        }
    }

    @SuppressLint("CheckResult")
    private void nedd_permissions() {
        rxPermissions
                .requestEach(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE
                )
                .subscribe(permission -> {
                    if (permission.granted) {
                        //权限获取成功
                    } else {
                        ToastUtil.Error("请打开相应权限！");
                    }
                });
    }

    private void initfragment() {
        //获取起始的fangment
        BaseMainFragment fragment  = findFragment(RollCallFragment.class);
        if(fragment==null)
        {
            loadRootFragment(R.id.fl_container,RollCallFragment.newInstance());
        }
    }
    private void showDrawerLocation() {
        //  Home_Cache.put("homebean", GsonUtil.obj2JSON(message),60 * 60 * 24 * 28);
        String result =  mACache.getAsString("CityInfo");
        CityInfo info;
        if(result!=null)
        {
            info  = JSON.parseObject(result,CityInfo.class);
            if(null != info){
                main_address_tv.setText(info.getResult().getAddressComponent().getCity());
            }
        }
    }
    private void initheader() {
        main_time_tv = headerView.findViewById(R.id.main_time_tv);
        main_week_tv = headerView.findViewById(R.id.main_week_tv);
        main_date_tv = headerView.findViewById(R.id.main_date_tv);
        main_calendar_tv = headerView.findViewById(R.id.main_calendar_tv);
        main_address_tv = headerView.findViewById(R.id.main_address_tv);
    }

    @Override
    public Context getContext() {
        return this;
    }
    /**
     * 获取一系列时间
     * @param bean
     */
    @Override
    public void time(MainTimeBean bean) {
        Logger.i("HXS time"+bean.getWeek());
        if(!bean.getHhmm().equals(""))
        {
            main_time_tv.setText(bean.getHhmm());
            main_week_tv.setText(bean.getWeek());
            main_date_tv.setText(bean.getYymmdd());
            main_calendar_tv.setText(bean.getOldtime());
        }
    }
    //左面划出对的点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        ISupportFragment topfragment = getTopFragment();//获取栈顶的fragment
        BaseMainFragment Home = (BaseMainFragment) topfragment;//起始页fragment
        int id = item.getItemId();
        drawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (id){
                    case R.id.nav_rollcall://人员点名
                        setToolbarLeftTV("人员点名");
                        RollCallFragment fragment = findFragment(RollCallFragment.class);
                        Bundle newBundle = new Bundle();
                        newBundle.putString("from", "From:" + topfragment.getClass().getSimpleName());
                        fragment.putNewBundle(newBundle);
                        //已经在栈内,则以SingleTask模式start
                        Home.start(fragment , SupportFragment.SINGLETASK);

                        break;
                    case R.id.nav_notice://通知·通告
                        setToolbarLeftTV("通知通告");
                        NoticeFragment noticeFragment =findFragment(NoticeFragment.class);
                        if(noticeFragment == null)
                        {
                            Home.startWithPopTo(NoticeFragment.newInstance(),RollCallFragment.class,false);
                        }else {
                            // 如果已经在栈内,则以SingleTask模式start
                            Home.start(noticeFragment, SupportFragment.SINGLETASK);
                        }
                        break;
                    case R.id.nav_loss://报损
                        setToolbarLeftTV("报损");
                        LossFragment lossFragment = findFragment(LossFragment.class);
                        if(lossFragment == null)
                        {
                            Home.startWithPopTo(LossFragment.newInstance(),RollCallFragment.class,false);
                        }else {
                            //如果已经在栈内  则以SingleTask模式start,也可以用popTo
                            Home.start(lossFragment,SupportFragment.SINGLETASK);
                            //  Home.popTo(LossFragment.class,false);
                        }
                        break;
                    case R.id.nav_sign://签到统计
                        setToolbarLeftTV("签到统计");
                        SignFragment signFragment  =findFragment(SignFragment.class);
                        if(signFragment== null)
                        {
                            Home.startWithPopTo(SignFragment.newInstance(),RollCallFragment.class,false);
                        }else {
                            Home.start(signFragment,SupportFragment.SINGLETASK);
                        }
                        break;

                    case R.id.nav_police://报警记录
                        setToolbarLeftTV("报警记录");
                        PoliceFragment policeFragment = findFragment(PoliceFragment.class);
                        if(policeFragment == null)
                        {
                            Home.startWithPopTo(PoliceFragment.newInstance(),RollCallFragment.class,false);
                        }else {
                            Home.start(policeFragment,SupportFragment.SINGLETASK);
                        }

                        break;
                    case R.id.nav_setting://设置
                        setToolbarLeftTV("设置");
                        SettingFragment settingFragment = findFragment(SettingFragment.class);
                        if(settingFragment == null)
                        {
                            Home.startWithPopTo(SettingFragment.newInstance(),RollCallFragment.class,false);
                        }else {
                            Home.start(settingFragment,SupportFragment.SINGLETASK);
                        }
                        break;
                }
            }
        },300);

        return true;
    }
    /**
     * 打开抽屉
     */


    /**
     * 设置动画
     */


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        //return super.onCreateFragmentAnimator();

        // 设置横向(和安卓4.x动画相同)
        //
            return new DefaultHorizontalAnimator();
    }
    /**
     * 返回退出
     */
    @Override
    public void onBackPressedSupport() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            ISupportFragment topFragment = getTopFragment();
            // 主页的Fragment
            if (topFragment instanceof BaseMainFragment) {
                navigationView.setCheckedItem(R.id.nav_rollcall);
            }
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                pop();
            } else {
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    finish();
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    ToastUtil.Warning("再按一次退出!");
                }
            }
        }
    }

    /**
     * 学生信息
     */
    @Subscribe
    public void stulistevent(StuListBean bean)
    {
        mPresenter.SendMQRealm(bean.getListID());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RabbiMqEngine.getRabbiMqEngine().destoryConnect();
        mPresenter.canceltime();
        EventBus.getDefault().unregister(this);
    }


}
