package com.gzgykj.sturollcall.mvp.ui.notice;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzgykj.basecommon.utils.ActivityUtil;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.adapter.NoticeAdapter;
import com.gzgykj.sturollcall.base.BaseFragment;
import com.gzgykj.sturollcall.mvp.ui.notice.noticedetail.NoticeDetailActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :通知通告
 */
public class NoticeFragment extends BaseFragment<NoticePresenter> implements NoticeContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.notice_recyclevew)
    RecyclerView recyclerView;

    @BindView(R.id.notice_refreshlayout)
    SwipeRefreshLayout refreshLayout;

    private int pageIndex= 1;
    private int pageTotal= 0;
    private boolean isRefreshing = true;//当前是刷新还是加载
    public static  NoticeFragment newInstance()
    {
        return  new NoticeFragment();
    }
    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_notice;
    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        /**获取模拟数据**/
        mPresenter.notice();

        initRecycleview();
    }

    private void initRecycleview() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(this);//刷新效果
        refreshLayout.setRefreshing(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        mPresenter.adapter.setOnLoadMoreListener(this,recyclerView);
        mPresenter.adapter.setOnItemClickListener(this);

        /**分割线**/
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                        .color(Color.parseColor("#eeeeee"))
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                        .build());

        recyclerView.setAdapter(mPresenter.adapter);
        View emptyView = View.inflate(getContext(),R.layout.item_empty, null);
        mPresenter.adapter.setEmptyView(emptyView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(refreshLayout!=null)
                {
                    refreshLayout.setRefreshing(false);
                }
            }
        },1000);
    }
    @Override
    public void notice(List<String> test) {
         mPresenter.adapter.setNewData(test);
    }

    /**
     * 刷新操作
     */
    @Override
    public void onRefresh() {
        mPresenter.notice();
        refreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(refreshLayout!=null)
                {
                    refreshLayout.setRefreshing(false);
                }

            }
        },1000);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        mPresenter.adapter.loadMoreEnd();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
       // ToastUtil.Success("点击!");
         // startWithPopTo(new NoticeDetailActivity(),NoticeFragment.class,false);
       //  startWithPop(new NoticeDetailActivity());
        mPresenter.adapter = (NoticeAdapter) adapter;

        Bundle bundle = new Bundle();
        bundle.putString("TEXT",mPresenter.adapter.getData().get(position));
        ActivityUtil.getInstance().openActivity(getActivity() ,NoticeDetailActivity.class,bundle);
        // showHideFragment(this,new NoticeDetailActivity());
       // getParentFragment().start(new NoticeDetailActivity());
    }

    /**
     * 传递的数据
     * @param args
     */
    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);


    }
}
