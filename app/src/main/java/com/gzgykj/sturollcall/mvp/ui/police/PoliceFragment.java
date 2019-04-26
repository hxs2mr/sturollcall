package com.gzgykj.sturollcall.mvp.ui.police;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzgykj.basecommon.utils.Constants;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseFragment;
import com.gzgykj.sturollcall.mq.RabbiMqEngine;
import com.gzgykj.sturollcall.mvp.model.PoliceBean;
import com.gzgykj.sturollcall.mvp.ui.notice.NoticeFragment;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * Data on :2019/3/28 0028
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :报警记录
 */
public class PoliceFragment extends BaseFragment<PolicePresenter> implements PoliceContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.police_recycleview)
    RecyclerView recyclerView;

    @BindView(R.id.police_refreshlayout)
    SwipeRefreshLayout refreshLayout;


    private int pageIndex= 1;
    private int pageTotal= 0;
    private boolean isRefreshing = true;//当前是刷新还是加载

    public static PoliceFragment newInstance()
    {
        return  new PoliceFragment();
    }
    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_police;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        EventBus.getDefault().register(this);
        mPresenter.initadapter();
        initRecycleview();
    }
    private void initRecycleview() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        mPresenter.police();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        mPresenter.adapter.setOnLoadMoreListener(this,recyclerView);
      //  mPresenter.adapter.setOnItemClickListener(this);

        /**分割线**/
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.parseColor("#eeeeee"))
                .sizeResId(R.dimen.divider)
                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build());

        recyclerView.setAdapter(mPresenter.adapter);
        View emptyView = View.inflate(getContext(),R.layout.item_empty, null);
        mPresenter.adapter.setEmptyView(emptyView);
        refreshLayout.setOnRefreshListener(this);//刷新效果
        refreshLayout.setRefreshing(true);
        RabbiMqEngine.getRabbiMqEngine().sendMessage(true,Constants.WarringPage, Constants.PAGE_SIEZ,pageIndex,0,0); //发送消息初始化
    }
    @Override
    public void police(List<String> list) {
    }

    @Override
    public void onRefresh() {
        pageIndex =1;
        isRefreshing =  true;
     //   mPresenter.police();
        refreshLayout.setRefreshing(true);
        RabbiMqEngine.getRabbiMqEngine().sendMessage(true,Constants.WarringPage,Constants.PAGE_SIEZ,pageIndex,0,0); //发送消息初始化
    }

    @Override
    public void onLoadMoreRequested() {
         pageIndex++;
        if (pageIndex > pageTotal) {
            mPresenter.adapter.loadMoreEnd();
            return;
        } else {
            isRefreshing = false;
            RabbiMqEngine.getRabbiMqEngine().sendMessage(true,Constants.WarringPage,Constants.PAGE_SIEZ,pageIndex,0,0); //发送消息初始化
        }
    }
    @Subscribe
    public void MessagePolice(PoliceBean bean)
    {
        System.out.println("新数据来了********");
        if (bean.getRecords() == null || bean.getRecords().size() <= 0) {
            View emptyView = View.inflate(getContext(), R.layout.item_empty, null);
            mPresenter.adapter.setEmptyView(emptyView);
        }
        pageTotal = bean.getTotal();
        if (isRefreshing) {
            refreshLayout.setRefreshing(false);
            mPresenter.adapter.getData().clear();
            mPresenter.adapter.setNewData(bean.getRecords());
        } else {
            mPresenter.adapter.addData(bean.getRecords());
            mPresenter.adapter.loadMoreComplete();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
