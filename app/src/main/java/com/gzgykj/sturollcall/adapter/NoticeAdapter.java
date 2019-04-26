package com.gzgykj.sturollcall.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzgykj.sturollcall.R;
import java.util.List;
/**
 * Data on :2019/4/4 0004
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class NoticeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public NoticeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_title,item);
    }
}
