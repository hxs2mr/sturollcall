package com.gzgykj.sturollcall.app.datamanager;

import com.gzgykj.basecommon.response.AppRespose;
import com.gzgykj.sturollcall.app.db.DBHelper;
import com.gzgykj.sturollcall.app.http.HttpHelper;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :数据操作工厂  统一管理 网络 数据库 sharefresces操作
 */
public class DataManager implements DBHelper, HttpHelper {

    private HttpHelper mHttpHelper;
    private  DBHelper mDBHelper;

    public DataManager(HttpHelper httpHelper,DBHelper dbHelper)
    {
        this.mDBHelper = dbHelper;
        this.mHttpHelper = httpHelper;
    }

    /**网络请求**/
    @Override
    public Observable<AppRespose<String>> xxx(Map<String, String> action) {
        return mHttpHelper.xxx(action);
    }

    @Override
    public Observable<AppRespose<String>> sendsms(Map<String, String> action) {
        return mHttpHelper.sendsms(action);
    }

    @Override
    public Observable<AppRespose<String>> addface(Map<String, Object> action) {
        return mHttpHelper.addface(action);
    }

    @Override
    public Observable<AppRespose<String>> login(Map<String, String> action) {
        return mHttpHelper.login(action);
    }

    /**********以下是数据库操作***********/
    @Override
    public boolean queryNewsId(int id) {
        return false;
    }

    @Override
    public void closeDB() {

    }

    @Override
    public int db_insert() {
        return 0;
    }

    @Override
    public int db_delete() {
        return 0;
    }

    @Override
    public int db_update() {
        return 0;
    }

    @Override
    public int db_select() {
        return 0;
    }


}
