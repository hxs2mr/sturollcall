package com.gzgykj.sturollcall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzgykj.sturollcall.R;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :通知通告的适配器
 */
public class SignAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    public SignAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.sign_time_tv,item);
    }
}
