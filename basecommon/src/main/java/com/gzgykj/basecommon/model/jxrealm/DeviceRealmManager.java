package com.gzgykj.basecommon.model.jxrealm;

import io.realm.Realm;

/**
 * desc   :学校Realm数据库管理类
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/11/710:05
 * version: 1.0
 */
public class DeviceRealmManager {

    private DeviceRealmManager(){
    }

    private static class SchoolRealmManagerHolder{
        private static DeviceRealmManager instance = new DeviceRealmManager();
    }

    public static DeviceRealmManager getManager(){
    return DeviceRealmManager.SchoolRealmManagerHolder.instance;
    }


    /**
     * 添加每一间寝室device id
     * @param listener
     */
    public void addSchoolRealm(final long deviceid, final IRealmListener listener) {
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DeviceidRealm schoolRealm = new DeviceidRealm();
                schoolRealm.setDevice_id(deviceid);
                realm.copyToRealm(schoolRealm);
            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                listener.OnSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                listener.onError(error);
            }
        });
    }


    /**
     * 修改学校id
     * @param listener
     */
    public void updateSchoolToRealm(final long deviceid,final IRealmListener listener){
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final DeviceidRealm schoolRealm = realm.where(DeviceidRealm.class).findFirst();
                schoolRealm.setDevice_id(deviceid);
            }
        },  new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                listener.OnSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                listener.onError(error);
            }
        });
    }

}
