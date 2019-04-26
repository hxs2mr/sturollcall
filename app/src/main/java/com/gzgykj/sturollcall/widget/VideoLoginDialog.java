package com.gzgykj.sturollcall.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;

import com.flyco.dialog.widget.base.BaseDialog;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.mvp.ui.video.VideoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * name : HXS
 * e-mail : 1363826037@qq.com
 * descript:借用登记dialog
 * date   : 2019/3/1214:19
 * version: 1.0
 */
public class VideoLoginDialog extends BaseDialog<VideoLoginDialog> {

    @BindView(R.id.iv_close)
    AppCompatImageView iv_close;
    @BindView(R.id.btn_login)
    AppCompatButton button_ok;


    private VideoActivity videoActivity;
    public VideoLoginDialog(Context context, Activity activity) {
        super(context);
        this.videoActivity = (VideoActivity) activity;
    }

    @Override
    public View onCreateView() {
        View inflate = View.inflate(mContext, R.layout.dialog_login, null);
        ButterKnife.bind(this,inflate);
        return  inflate;
    }

    @Override
    public void setUiBeforShow() {

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录
                videoActivity.Login();
                dismiss();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
