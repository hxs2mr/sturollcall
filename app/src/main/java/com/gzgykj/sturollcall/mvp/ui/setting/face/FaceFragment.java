package com.gzgykj.sturollcall.mvp.ui.setting.face;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gzgykj.basecommon.utils.ActivityUtil;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseFragment;
import com.gzgykj.sturollcall.mvp.ui.face.FaceContract;
import com.gzgykj.sturollcall.mvp.ui.face.FacePresenter;

import butterknife.OnClick;

/**
 * Data on :2019/3/25 0025
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :添加
 */
public class FaceFragment extends BaseFragment<FacePresenter>implements FaceContract.View {
    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }
    @Override
    public Object setLayout() {
        return R.layout.fragment_face;
    }
    /**
     * 点击添加头部特征
     * @param view
     */
    @OnClick(R.id.btn_add)
    public  void onclick(View view)
    {
        ActivityUtil.getInstance().openActivity(getActivity(),FaceAddActivity.class);
    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }
}
