package com.gzgykj.sturollcall.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.flyco.dialog.widget.base.BaseDialog;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.mvp.ui.loss.LossFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * name : HXS
 * e-mail : 1363826037@qq.com
 * descript:借用登记dialog
 * date   : 2019/3/1214:19
 * version: 1.0
 */
public class LossAddDialog extends BaseDialog<LossAddDialog> {

    @BindView(R.id.borrow_ok)
     Button button_ok;
    @BindView(R.id.borrow_cancel)
     Button button_cancle;


    @BindView(R.id.ev_username)
    EditText ev_username;

    @BindView(R.id.ev_articleName)
    EditText ev_articleName;

    @BindView(R.id.ev_number)
    EditText ev_number;

    @BindView(R.id.ev_Desc)
    EditText ev_Desc;

    private LossFragment mFragment;
    public LossAddDialog(Context context,LossFragment fragment) {
        super(context);
        this.mFragment = fragment;
    }

    @Override
    public View onCreateView() {
        widthScale(0.7f);
        View inflate = View.inflate(mContext, R.layout.loss_item, null);
        ButterKnife.bind(this,inflate);
        return  inflate;
    }

    @Override
    public void setUiBeforShow() {
        button_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( check())
                {
                    mFragment.addLoss( ev_username.getText().toString(), ev_articleName.getText().toString(), ev_number.getText().toString(), ev_Desc.getText().toString());
                    dismiss();
                }

            }
        });
    }


    public  boolean check()
    {
        boolean  isnonull = true;
        String username = ev_username.getText().toString();
        String articleName = ev_articleName.getText().toString();
        String number = ev_number.getText().toString();
        String Desc = ev_Desc.getText().toString();
        if(TextUtils.isEmpty(username))
        {
            ev_username.setError("输入报损人!");
            isnonull = false;
        }
        if(TextUtils.isEmpty(articleName))
        {
            ev_articleName.setError("输入物品名称!");
            isnonull = false;
        }
        if(TextUtils.isEmpty(number))
        {
            ev_number.setError("输入型号!");
            isnonull = false;
        }
        if(TextUtils.isEmpty(Desc))
        {
            ev_Desc.setError("输入描述!");
            isnonull = false;
        }

        return isnonull;

    }
}
