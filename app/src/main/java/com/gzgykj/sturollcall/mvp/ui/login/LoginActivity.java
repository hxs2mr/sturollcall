package com.gzgykj.sturollcall.mvp.ui.login;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;

import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.adapter.LoginViewPagerAdapter;
import com.gzgykj.sturollcall.base.BaseActivity;
import com.gzgykj.sturollcall.mvp.ui.login.face.FaceLoginFragment;
import com.gzgykj.sturollcall.mvp.ui.login.pass.PasswordFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Data on :2019/3/20 0020
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :语音聊天时候的登录界面   包含人脸识别登录  密码用户登录frgament
 */
public class LoginActivity extends BaseActivity<LoginPresenter>implements LoginContract.View {
    @BindView(R.id.tablayout)
    TabLayout tableLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private List<Fragment> fragments = new ArrayList<>();
    private String[] tabTitle=new String[]{"人脸登录","账号登录"};//,"共同目标"};

    private LoginViewPagerAdapter adapter;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }
    @Override
    public Object setLayout() {
        return R.layout.activity_login;
    }
    @Override
    protected void initEventData() {
        setshowToolbarRight(false);
        setToolbarleftBack();
        setToolbarLeftTitle("登录");
        inittablayout();
    }

    private void inittablayout() {
        fragments.add(new FaceLoginFragment());
        fragments.add(new PasswordFragment());
        //   fragments.add(new HomeChildThreadFragment());
        viewPager.setFocusable(true);
        viewPager.setFocusableInTouchMode(true);
        viewPager.requestFocus();

        if(tableLayout.getTabCount()>0)
        {
            tableLayout.removeAllTabs();
        }
        for(int i = 0 ; i < tabTitle.length ;i++)
        {
            tableLayout.addTab(tableLayout.newTab().setText(tabTitle[i]));
        }
        //设置顶部标签指示条的颜色和选中页面时标签字体颜色
        tableLayout.setSelectedTabIndicatorColor(Color.parseColor("#5298fc"));
        tableLayout.setTabTextColors(Color.parseColor("#dbdbdb"), Color.parseColor("#81613f"));
        //这里注意的是，因为我是在fragment中创建MyFragmentStatePagerAdapter，所以要传getChildFragmentManager()
        adapter = new LoginViewPagerAdapter(getSupportFragmentManager(), tabTitle,fragments);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));

        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //滑动监听加载数据，一次只加载一个标签页
                adapter.getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //在选中的顶部标签时，为viewpager设置currentitem
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    public Context getContext() {
        return this;
    }

}
