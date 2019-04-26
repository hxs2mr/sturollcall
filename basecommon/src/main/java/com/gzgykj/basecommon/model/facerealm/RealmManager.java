package com.gzgykj.basecommon.model.facerealm;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data on :2019/4/12 0012
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :数据库管理工具
 */
public class RealmManager {

    /**人脸数据库 模块**/

    private RealmManager(){}

    //静态累不累
    private static class  RealmManagerHolder
    {
        private static RealmManager instance = new RealmManager();
    }
    public static  RealmManager getInstance()
    {
        return      RealmManager.RealmManagerHolder.instance;
    }

    /**
     * 添加人脸
     * @param user_id
     * @param featuresType
     * @param features
     */
    public void addFaceToRealm(final long user_id, final int featuresType, final byte[] features, final IAddFaceListener listener){
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FaceRealm faceRealm = new FaceRealm();
                faceRealm.setFeatures(features);
                faceRealm.setFeaturesType(featuresType);
                faceRealm.setUserId( user_id);
                realm.copyToRealm(faceRealm);
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
     * 更新人脸
     * @param user_id
     * @param featuresType
     * @param features
     */
    public void updateFaceToRealm(final long user_id, final int featuresType, final byte[] features, final IAddFaceListener listener){
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final FaceRealm faceRealm = realm.where(FaceRealm.class).equalTo("userId", user_id).equalTo("featuresType", featuresType).findFirst();
                faceRealm.setFeatures(features);
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
    /**
     * 删除人脸
     * @param user_id
     * @param featuresType
     * @param features
     */
    public void deleteFaceToRealm(final long user_id,final int featuresType, final byte[] features, final IAddFaceListener listener){
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final RealmResults<FaceRealm> faceRealm = realm.where(FaceRealm.class).equalTo("userId", user_id).findAll();
                faceRealm.deleteAllFromRealm();
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

}
