package com.gzgykj.sturollcall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.mvp.model.PoliceBean;
import com.gzgykj.sturollcall.utils.DateUtil;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :通知通告的适配器
 */
public class PoliceAdapter extends BaseQuickAdapter<PoliceBean.RecordsBean,BaseViewHolder>{

    public PoliceAdapter(int layoutResId) {
        super(layoutResId);
    }
    @Override
    protected void convert(BaseViewHolder helper, PoliceBean.RecordsBean item) {
        helper.setText(R.id.tv_title,"你使用了一键报警,向宿管请求帮助!");
        helper.setText(R.id.tv_time,"时间:"+ DateUtil.timeSpanToDate(item.getGmtCreate()));
    }
}
