package com.gzgykj.sturollcall.adapter;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.app.App;
import com.gzgykj.sturollcall.mvp.model.VideoQinBean;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :通知通告的适配器
 */
public class VideoQinAdapter extends BaseQuickAdapter<VideoQinBean,BaseViewHolder>{

    public VideoQinAdapter(int layoutResId) {
        super(layoutResId);
    }
    @Override
    protected void convert(BaseViewHolder helper, VideoQinBean item) {
        AppCompatImageView imageView = helper.getView(R.id.iv_select);
        helper.setText(R.id.rb_name,item.getName());
        if(item.isIsclick())
        {
            imageView.setBackgroundResource(R.drawable.video_yes);
        }else {
            imageView.setBackgroundResource(R.drawable.video_no);
        }
    }
}
