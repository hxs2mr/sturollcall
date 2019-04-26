package com.gzgykj.sturollcall.mvp.ui.face;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.gykj.acface.customview.FaceRectView;
import com.gykj.acface.model.DrawInfo;
import com.gykj.acface.util.CameraHelper;
import com.gykj.acface.util.CameraListener;
import com.gykj.acface.util.DrawHelper;
import com.gzgykj.basecommon.model.facerealm.FaceRealm;
import com.gzgykj.basecommon.utils.Constants;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseActivity;
import com.gzgykj.sturollcall.base.BaseFragment;
import com.gzgykj.sturollcall.mq.RabbiMqEngine;
import com.gzgykj.sturollcall.utils.ConfigUtil;
import com.gzgykj.sturollcall.utils.FaceCenter;
import com.gzgykj.sturollcall.utils.FacePermissions;
import com.gzgykj.sturollcall.utils.SoundTipUtil;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.gzgykj.sturollcall.widget.TextureVideoViewOutlineProvider;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :人脸识别
 */
public class FaceActivity extends BaseActivity<FacePresenter> implements FaceContract.View, ViewTreeObserver.OnGlobalLayoutListener, CameraListener {

    @BindView(R.id.texture_preview)
    TextureView textureView;     //相机预览时候的控件
    @BindView(R.id.face_rect_view)
    FaceRectView faceRectView;

    @BindView(R.id.dialog_circle_iv)
    AppCompatImageView  face_iv;


   //相机初始化类
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;

    private boolean isResume = false;


    //摄像头摄像框的大小
    private Camera.Size previewSize;

    //前置摄像头
    private Integer cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private FaceEngine faceEngine;


    private int afcode = -1;

    private int processMask = FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS;


    private FacePermissions facePermissions;


    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;

    private String TAG ="FACE";
    /**
     * 判断中心点人数
     */
    private int center_people = 0;

    private FaceCenter faceCenter;

    private int mfaceRectViewWidth ;
    private int mfaceRectViewHeight;
    private int face_h;
    private int face_w;

    private FaceTask mFaceTask;

    private boolean isRecognize = false;

    //人脸数据库数据
    private RealmResults<FaceRealm> mFaceList;

    private Bundle bundle ;
    private  int callid=-1;
    private boolean isfaceTaskbool = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Activity启动后就锁定为启动时的方向
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            default:
                break;
        }

    }

    @Override
    protected void initInject()  {
        getActivityComponent().inject(this);
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_face;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initEventData() {

        bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            callid = bundle.getInt(Constants.CALLID);
        }
        if(LoadingView.isShowing())
        {
            LoadingView.dismiss();
        }
        setshowToolbarRight(false);
        setToolbarleftBack();
        setToolbarLeftTitle("人脸签到");
        face_iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                face_h = face_iv.getHeight();
                face_w = face_iv.getWidth();
                Logger.i("face_h " + face_h + "  face_w:" + face_w);
            }
        });

        //在布局结束后才做初始化操作
        textureView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        facePermissions = new FacePermissions(this);

        mFaceList = Realm.getDefaultInstance().where(FaceRealm.class).findAll();
    }

    /**
     * 初始化人脸
     */
    private void initEngine() {
        faceEngine = new FaceEngine();
        afcode = faceEngine.init(this, FaceEngine.ASF_DETECT_MODE_VIDEO,FaceEngine.ASF_OP_0_HIGHER_EXT,
                16, 20, FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Logger.i("Face: afcode: " + afcode + "  version:" + versionInfo);
        if (afcode != ErrorInfo.MOK) {
            ToastUtil.Error("人脸识别初始化失败："+afcode);
        }
        //语音播放
        SoundTipUtil.soundTip(FaceActivity.this,"人脸对应识别框识别");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SoundTipUtil.soundTip(FaceActivity.this,"未找到用户信息");
            }
        },3000);
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LoadingView.show();
        cameraHelper = new CameraHelper.Builder()
                .previewSize(previewSize)
                .metrics(metrics)
                //.previewViewSize(new Point(textureView.getMeasuredWidth(),textureView.getMeasuredHeight()))
                //.rotation(getWindowManager().getDefaultDisplay().getRotation()) //平时
                .rotation(2)//该设备
                .specificCameraId(cameraID != null ? cameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(textureView)
                .cameraListener(this)
                .build();
        cameraHelper.init();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onGlobalLayout() {
        textureView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (!facePermissions.checkPermissions(FacePermissions.NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, FacePermissions.NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initCamera();
            initEngine();
        }
        mfaceRectViewWidth = faceRectView.getWidth();
        mfaceRectViewHeight = faceRectView.getHeight();

        Logger.i(TAG+"mfaceRectViewWidth="+mfaceRectViewWidth);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                initCamera();
                initEngine();
                if (cameraHelper != null) {
                    cameraHelper.start();
                }
            } else {
                ToastUtil.Warning("请打开相机权限!");
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
        Logger.i(TAG+"摄像头打开: " + cameraId + "  " + displayOrientation + " " + isMirror);
        LoadingView.dismiss();
        previewSize = camera.getParameters().getPreviewSize();
        faceCenter = new FaceCenter(mfaceRectViewWidth,mfaceRectViewHeight,previewSize);
        drawHelper = new DrawHelper(previewSize.width, previewSize.height, textureView.getWidth(), textureView.getHeight(), displayOrientation
                , cameraId, isMirror);

        /**
         * 设置为圆形  手机其实为1000
         */
          /*  textureView.setOutlineProvider(new TextureVideoViewOutlineProvider( 180,textureView.getWidth()/2-face_h/2,textureView.getHeight()/2-face_w/2));
            textureView.setClipToOutline(true); */
    }

    /**
     * 获取每一帧图形 进行对比
     * @param nv21
     * @param camera 相机实例
     */
    @Override
    public void onPreview(byte[] nv21, Camera camera) {
        if (faceRectView != null) {
            faceRectView.clearFaceInfo();
        }
        List<FaceInfo> faceInfoList = new ArrayList<>();

        int code = faceEngine.detectFaces(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList);
        Logger.e("HXS绘制0"+code+"    大小:"+faceInfoList.size());
        if (faceRectView != null && drawHelper != null) {
            List<DrawInfo> drawInfoList = new ArrayList<>();
            for (int i = 0; i < faceInfoList.size(); i++) {
                drawInfoList.add(new DrawInfo(faceInfoList.get(i).getRect()));
            }
            Logger.e("HXS绘制1");

            drawHelper.draw(faceRectView, drawInfoList);
        }


      /*  if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
            code = faceEngine.process(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList, processMask);
            if (code != ErrorInfo.MOK) {
                return;
            }
        }else {
            return;
        }
        List<AgeInfo> ageInfoList = new ArrayList<>();
        List<GenderInfo> genderInfoList = new ArrayList<>();
        List<Face3DAngle> face3DAngleList = new ArrayList<>();
        List<LivenessInfo> faceLivenessInfoList = new ArrayList<>();
        int ageCode = faceEngine.getAge(ageInfoList);
        int genderCode = faceEngine.getGender(genderInfoList);
        int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
        int livenessCode = faceEngine.getLiveness(faceLivenessInfoList);*/


        //有其中一个的错误码不为0，return
    /*    if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
            return;
        }   */

        center_people =0 ;
        if(faceCenter!=null)
        {
            for(int i = 0;i<faceInfoList.size();i++){

                if(faceCenter.checkIsCenter(faceInfoList.get(i).getRect())){
                    center_people++;
                    Logger.e(TAG+"是中心点");
                }
            }
        }

        Logger.e(TAG+"中心点人数"+center_people);
        //中心点站的人太多
        if(center_people > 1){
            return;
        }

        //放入ansytask中对比

        //借助AsyncTask开启一个线程在后台处理数据


        if(isfaceTaskbool)
        {
            if(null != mFaceTask){
                switch(mFaceTask.getStatus()){
                    case RUNNING:
                        return;
                    case PENDING:
                        mFaceTask.cancel(false);
                        break;
                }
            }
            mFaceTask = new FaceTask(nv21,faceInfoList);
            mFaceTask.execute((Void)null);
        }



    }

    private class FaceTask extends AsyncTask<Void,Void,Void>{

        private byte[] mData;
        private List<FaceInfo> mFaceInfoLists;

        FaceTask(byte[]  data , List<FaceInfo> faceInfos)
        {
                this.mData  =data;
                this.mFaceInfoLists = faceInfos;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            processImage(mData,mFaceInfoLists);
            return null;
        }
    }

    //开始对比
    private void processImage(byte[] nv21, List<FaceInfo> faceInfoList) {

        if(isRecognize)
        {
            return;
        }
        for(int i = 0; i < faceInfoList.size(); i ++){
                isRecognize = true;
                FaceFeature faceFeature = new FaceFeature();
                FaceFeature face = new FaceFeature();

                //  进行人脸特征提取.extractFaceFeature
                int res = faceEngine.extractFaceFeature(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList.get(i), faceFeature);
                if (res == 0) {
                    FaceSimilar faceSimilar = new FaceSimilar();
                    long start = System.currentTimeMillis();
                    int number = 0;
                    for(int j = 0; j< mFaceList.size(); j++) {

                        face.setFeatureData(mFaceList.get(j).getFeatures());
                        // 进行人脸比对.compareFaceFeature
                        number++;
                        int compareResult = faceEngine.compareFaceFeature(face, faceFeature, faceSimilar);
                        System.out.println("=======相似度====" + faceSimilar.getScore());
                        if (faceSimilar.getScore() >= 0.6f) {
                            System.out.println("=======用户id====" + mFaceList.get(j).getUserId());
                            if(Constants.ISCALL)
                            {
                                //人脸识别成功 返回接口
                                //recognizeListener.recognize(mFaceList.get(j).getUser_id(), mFaceList.get(j).getUserType());
                                RabbiMqEngine.getRabbiMqEngine().sendMessage(true,Constants.UPFACEJX,Constants.PAGE_SIEZ,0, (int) mFaceList.get(j).getUserId(),callid); //发送消息初始化
                            }else {
                                    runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        isfaceTaskbool = false;
                                        ToastUtil.Error("点名未开始或已结束!");
                                        finish();
                                    }
                                });
                            }

                            break;
                        }
                        long end = System.currentTimeMillis();
                        System.out.println("=======识别时间====" + (end - start));
                    }
                    System.out.println("=======对比次数====" + number);
                }
        }
        isRecognize = false;
    }


    @Override
    public void onCameraClosed() {
        Logger.i(TAG+"摄像头关闭: ");
        LoadingView.dismiss();
    }

    @Override
    public void onCameraError(Exception e) {
        LoadingView.dismiss();
        Logger.i(TAG+"相机初始化错误: " + e.getMessage());
    }

    @Override
    public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
        if (drawHelper != null) {
            drawHelper.setCameraDisplayOrientation(displayOrientation);
        }
        Logger.i(TAG+"摄像头配置更改: " + cameraID + "  " + displayOrientation);
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
    }

    /**
     * 释放资源
     */
    @Override
    protected void onDestroy() {
        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }
        unInitEngine();
        super.onDestroy();
    }
    private void unInitEngine() {
        if (afcode == 0) {
            afcode = faceEngine.unInit();
            Logger.i(TAG+"人脸释放资源完成: "+afcode );
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
