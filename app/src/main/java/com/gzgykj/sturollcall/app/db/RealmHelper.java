package com.gzgykj.sturollcall.app.db;

import android.provider.SyncStateContract;

import com.gzgykj.basecommon.utils.Constants;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :Realm数据库
 */
public class RealmHelper  implements  DBHelper{

    private Realm mRealm;
    @Inject
    public RealmHelper()
    {
        mRealm = Realm.getInstance(
                new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(Constants.DB_NAME)
                .build());
    }

    @Override
    public boolean queryNewsId(int id) {
        return false;
    }

    @Override
    public void closeDB() {
        if (mRealm!=null&&!mRealm.isClosed()){
            mRealm.close();
        }
    }

    @Override
    public int db_insert() {
       /* mRealm.beginTransaction();
        String uuid= UUID.randomUUID().toString().replaceAll("-", "");
        int anInt = new Random().nextInt();
        Test_Personal personal=new Test_Personal(uuid,"itisi"+anInt,anInt);
        Test_Personal personal1 = mRealm.copyToRealm(personal);

        Logger.i("getId:"+personal1.getId());
        mRealm.deleteAll();  //该死 数据被清空了 难过一直查询不到数据
        mRealm.commitTransaction();*/
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
