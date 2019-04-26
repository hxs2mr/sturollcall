package com.gzgykj.sturollcall.adapter;

import android.support.v7.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzgykj.basecommon.model.LossBean;
import com.gzgykj.basecommon.model.LossPageBean;
import com.gzgykj.basecommon.model.RcordsBean;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.app.App;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :通知通告的适配器
 */
public class LossAdapter extends BaseQuickAdapter<LossPageBean.RecordsBean,BaseViewHolder>{

    public LossAdapter(int layoutResId)  {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LossPageBean.RecordsBean item) {
        helper.setText(R.id.loss_name_tv,"报损人:"+item.getId());

        AppCompatTextView loss_status_tv = helper.getView(R.id.loss_status_tv);
        CircleImageView headimg = helper.getView(R.id.loss_header_im);


        //测试
        //(数据类型)(最小值+Math.random()*(最大值-最小值+1))
        //-1    1随机数
       int end=   (int)(-1 + Math.random()*(2-(-1)));

       if(end < 0 )
       {
           loss_status_tv.setBackgroundResource(R.drawable.loss_shape_no);
       }else {
           loss_status_tv.setBackgroundResource(R.drawable.loss_shape_yes);
       }

        Glide.with(App.getInstance())
                .load("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1752460159,226752426&fm=26&gp=0.jpg")
                .apply(App.getInstance().RECYCLER_OPTIONS)
                .into(headimg);

    }
}
