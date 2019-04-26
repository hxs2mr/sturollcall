package com.gzgykj.sturollcall.mvp.ui.sign;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseFragment;
import com.gzgykj.sturollcall.utils.PieChartManager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :签到统计
 */
public class SignFragment extends BaseFragment<SignPresenter>implements SignContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.piechart)
    PieChart pieChart;

    @BindView(R.id.sign_refreshlayout)
    SwipeRefreshLayout refreshlayout;

    @BindView(R.id.sign_recycleview)
    RecyclerView recyclerView;

    private PieChartManager pieChartManager;

    public static SignFragment newInstance()
    {
        return  new SignFragment();
    }
    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_sign;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.initAdapter();
        initRecycleview();
        //测试饼状图
        pieChartManager = new PieChartManager(pieChart);
        showhodlePieChart();
        mPresenter.sign();
    }

    private void initRecycleview() {
        refreshlayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshlayout.setOnRefreshListener(this);//刷新效果
        refreshlayout.setRefreshing(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        mPresenter.adapter.setOnLoadMoreListener(this,recyclerView);

        /**分割线**/
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.parseColor("#eeeeee"))
                .sizeResId(R.dimen.divider)
                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build());

        recyclerView.setAdapter(     mPresenter.adapter);
        View emptyView = View.inflate(getContext(),R.layout.item_empty, null);
        mPresenter.adapter.setEmptyView(emptyView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(refreshlayout!=null)
                {
                    refreshlayout.setRefreshing(false);
                }
            }
        },1000);

    }

    private void showhodlePieChart() {
        // 设置每份所占数量
        List<PieEntry> yvals = new ArrayList<>();
        yvals.add(new PieEntry(25.0f, "已到"));
        yvals.add(new PieEntry(6.0f, "未到"));


        //设置每份的颜色
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#5298fc"));
        colors.add(Color.parseColor("#2ee0c5"));

        pieChartManager.showRingPieChart(yvals,colors);
    }

    @Override
    public void sign(List<String> list) {
        mPresenter.adapter.setNewData(list);
    }

    @Override
    public void onRefresh() {
        refreshlayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(refreshlayout!=null)
                {
                    refreshlayout.setRefreshing(false);
                    mPresenter.adapter.loadMoreEnd();
                }
            }
        },1000);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.adapter.loadMoreEnd();
    }
}
