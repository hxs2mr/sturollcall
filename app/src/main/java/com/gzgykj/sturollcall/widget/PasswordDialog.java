package com.gzgykj.sturollcall.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.flyco.dialog.widget.base.BaseDialog;
import com.gzgykj.sturollcall.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * name : HXS
 * e-mail : 1363826037@qq.com
 * descript:借用登记dialog
 * date   : 2019/3/1214:19
 * version: 1.0
 */
public class PasswordDialog extends BaseDialog<PasswordDialog> {

    @BindView(R.id.borrow_ok)
     Button button_ok;

    public PasswordDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.4f);
        View inflate = View.inflate(mContext, R.layout.item_changepassword, null);
        ButterKnife.bind(this,inflate);
        return  inflate;
    }

    @Override
    public void setUiBeforShow() {

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
