package com.gzgykj.sturollcall.dagg.component;

import com.gzgykj.sturollcall.app.App;
import com.gzgykj.sturollcall.app.datamanager.DataManager;
import com.gzgykj.sturollcall.app.db.RealmHelper;
import com.gzgykj.sturollcall.app.http.RetrofitHelper;
import com.gzgykj.sturollcall.dagg.mode.AppModel;
import com.gzgykj.sturollcall.dagg.mode.HttpModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :注入操作对象
 */
@Singleton
@Component(modules = {AppModel.class, HttpModel.class})
public interface AppComponent {
    /**
     * 返回context
     * @return
     */
    App getContext();

    /**
     * 数据操作工厂
     * @return
     */
    DataManager getDataManager();


    /**
     * http 操作
     * @return
     */
    RetrofitHelper getRetrofitHelper();

    /**
     * 数据库操作
     * @return
     */
    RealmHelper getRealmHelper();

}
