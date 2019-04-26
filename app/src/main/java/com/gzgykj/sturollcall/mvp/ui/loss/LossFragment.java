package com.gzgykj.sturollcall.mvp.ui.loss;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzgykj.basecommon.model.LossBean;
import com.gzgykj.basecommon.model.LossPageBean;
import com.gzgykj.basecommon.mq.LossCallBack;
import com.gzgykj.basecommon.utils.ActivityUtil;
import com.gzgykj.basecommon.utils.Constants;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.adapter.LossAdapter;
import com.gzgykj.sturollcall.app.App;
import com.gzgykj.sturollcall.base.BaseFragment;
import com.gzgykj.sturollcall.mq.RabbiMqEngine;
import com.gzgykj.sturollcall.mvp.ui.loss.lossdetail.LossDetailActivity;
import com.gzgykj.sturollcall.widget.LossAddDialog;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :维修 报损
 */
public class LossFragment extends BaseFragment<LossPresenter> implements LossContract.View, SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.add_loss_linear)
    LinearLayoutCompat add_loss_linear;

    @BindView(R.id.loss_refreshlayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.loss_recyclevew)
    RecyclerView recyclerView;

    private int pageindex= 1;
    private int pagetotal= 0;
    private boolean isRefreshing = true;//当前是刷新还是加载
    private LossAddDialog  lossAddDialog;;
    public static LossFragment newInstance()
    {
        return  new LossFragment();
    }
    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }
    @Override
    public Object setLayout() {
        return R.layout.fragment_loss;
    }
    /**
     * 加载数据的地方
     * @param savedInstanceState
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        /***
         * 链接rabbitmq
         * （1）传入设备ID
         * （2）发送消息
         */


        EventBus.getDefault().register(this);

        initRecycleview();
        mPresenter.loss();
        lossAddDialog = new LossAddDialog(getContext(),this);


    }

    private void initRecycleview() {
        mPresenter.initAdapter();
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(this);//刷新效果
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        mPresenter.adapter.setOnLoadMoreListener(this,recyclerView);
        mPresenter.adapter.setOnItemClickListener(this);
        /**分割线**/
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.parseColor("#efefef"))
                .sizeResId(R.dimen.divider1)
                .build());
        recyclerView.setAdapter(mPresenter.adapter);
        View emptyView = View.inflate(getContext(),R.layout.item_empty, null);
        mPresenter.adapter.setEmptyView(emptyView);
        refreshLayout.setRefreshing(true);
        RabbiMqEngine.getRabbiMqEngine().sendMessage(true,1,Constants.PAGE_SIEZ,pageindex,0,0); //发送消息初始化



        /**
         * 获取学生信息
         */
      //  RabbiMqEngine.getRabbiMqEngine().sendStuDataMessage(true);
    }

    @OnClick(R.id.add_loss_linear)
    public void onclick()
    {
        lossAddDialog.show();
    }

    @Override
    public void loss(List<String> list) {
        //mPresenter.adapter.setNewData(list);
    }

    /**
     * 报损详情
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ActivityUtil.getInstance().openActivity(getActivity(), LossDetailActivity.class);
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        pageindex =1;
        isRefreshing = true;
        refreshLayout.setRefreshing(true);
        RabbiMqEngine.getRabbiMqEngine().sendMessage(true,Constants.LossPage,Constants.PAGE_SIEZ,pageindex,0,0); //发送消息初始化
    }
    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        pageindex++;
        if (pageindex > pagetotal) {
            mPresenter.adapter.loadMoreEnd();
            return;
        } else {
            isRefreshing = false;
            RabbiMqEngine.getRabbiMqEngine().sendMessage(true,Constants.LossPage,Constants.PAGE_SIEZ,pageindex,0,0); //发送消息初始化
        }
    }

    /**
     * 新增报损
     * flage:2表示  新增报损
     */
    public  void addLoss(String username,String articleName,String spec ,String reportDesc)
    {
        LoadingView.setLabel("新增中..").show();
        RabbiMqEngine.getRabbiMqEngine().sendAddMessage(true,username,articleName,spec,reportDesc); //发送消息初始化
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //rabbidmq推过来的数据

    @Subscribe
    public void MessagePageBean(LossPageBean bean)
    {
        System.out.println("新数据来了********");
        if (bean.getRecords() == null || bean.getRecords().size() <= 0) {
            View emptyView = View.inflate(getContext(), R.layout.item_empty, null);
            mPresenter.adapter.setEmptyView(emptyView);
        }
        pagetotal = bean.getPages();
        if (isRefreshing) {
            refreshLayout.setRefreshing(false);
            mPresenter.adapter.getData().clear();
            mPresenter.adapter.setNewData(bean.getRecords());
        } else {
            mPresenter.adapter.addData(bean.getRecords());
            mPresenter.adapter.loadMoreComplete();
        }
    }

    @Subscribe
    public void messageAddloss(LossPageBean bean)
    {
        lossAddDialog.dismiss();
        LoadingView.dismiss();
    }

}
