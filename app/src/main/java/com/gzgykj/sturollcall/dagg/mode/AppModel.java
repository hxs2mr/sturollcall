package com.gzgykj.sturollcall.dagg.mode;

import com.gzgykj.sturollcall.app.App;
import com.gzgykj.sturollcall.app.datamanager.DataManager;
import com.gzgykj.sturollcall.app.db.DBHelper;
import com.gzgykj.sturollcall.app.db.RealmHelper;
import com.gzgykj.sturollcall.app.http.HttpHelper;
import com.gzgykj.sturollcall.app.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.internal.http.HttpHeaders;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :全局单利modelu 与APP生命周期共存
 */

@Module
public class AppModel {
    private App mApp;
    public AppModel(App app)
    {
        mApp  = app;
    }

    @Provides
    @Singleton
    App provideApp()
    {
        return  mApp;
    }

    @Provides
    @Singleton
    HttpHelper provideHttpHelper(RetrofitHelper retrofitHelper)
    {
        return  retrofitHelper;
    }

    @Provides
    @Singleton
    DBHelper provideDBHelper(RealmHelper realmHelper)
    {
        return realmHelper;
    }
    @Provides
    @Singleton
    DataManager provideDataManager(HttpHelper httpHelper,DBHelper dbHelper)
    {
        return  new DataManager(httpHelper,dbHelper);
    }

}
