package com.gzgykj.sturollcall.mvp.ui.setting.face;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
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
import com.gykj.acface.customview.CirFaceRectView;
import com.gykj.acface.customview.FaceRectView;
import com.gykj.acface.model.DrawInfo;
import com.gykj.acface.util.CameraHelper;
import com.gykj.acface.util.CameraListener;
import com.gykj.acface.util.DrawHelper;
import com.gzgykj.basecommon.utils.Constants;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseActivity;
import com.gzgykj.sturollcall.mq.RabbiMqEngine;
import com.gzgykj.sturollcall.mvp.ui.face.FaceActivity;
import com.gzgykj.sturollcall.utils.FaceCenter;
import com.gzgykj.sturollcall.utils.FacePermissions;
import com.gzgykj.sturollcall.utils.SoundTipUtil;
import com.gzgykj.sturollcall.utils.SystemUtil;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.gzgykj.sturollcall.widget.CirTextureView;
import com.gzgykj.sturollcall.widget.CircularViewWidget;
import com.gzgykj.sturollcall.widget.TextureVideoViewOutlineProvider;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Data on :2019/4/24 0024
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :添加面部特征  上  下  左   右
 */
public class FaceAddActivity extends BaseActivity<FaceAddPresenter> implements FaceAddContract.View, ViewTreeObserver.OnGlobalLayoutListener, CameraListener {
    @BindView(R.id.dialog_circle_iv)
    CircularViewWidget face_iv;

    @BindView(R.id.texture_preview)
    CirTextureView textureView;     //相机预览时候的控件
    @BindView(R.id.face_rect_view)
    CirFaceRectView faceRectView;

    @BindView(R.id.tv_bottom)
    AppCompatTextView tv_bottom;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;


    @BindView(R.id.tv_complate)
    AppCompatTextView tv_complate;
    //相机初始化类
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;

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

    private  FaceTask mFaceTask;

    private boolean isRecognize = false;

    private boolean isfaceTaskbool = true;

    private int status = 0; // 0：表示正脸采集    1：表示上微微抬头 15°  2:下微微低头°  3：左薇薇偏偏  4：右微微偏
     private  Map<Integer, byte[]> facemap = new HashMap<>();
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }
    @Override
    public Object setLayout() {
        return R.layout.activity_faceadd;
    }
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
    public Context getContext() {
        return this;
    }
    @Override
    protected void initEventData() {
        setshowToolbarRight(false);
        setToolbarleftBack();
        setToolbarLeftTitle("添加面部特征");
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SoundTipUtil.soundTip(FaceAddActivity.this,"请正脸采集");
            }
        },2000);

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
                //  .previewViewSize(new Point(textureView.getMeasuredWidth(),textureView.getMeasuredHeight()))
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
            textureView.setOutlineProvider(new TextureVideoViewOutlineProvider( 280,textureView.getWidth()/2-face_h/2+5,textureView.getHeight()/2-face_w/2+5));
            textureView.setClipToOutline(true);
    }

    @Override
    public void onPreview(byte[] nv21, Camera camera) {
        if (faceRectView != null) {
            faceRectView.clearFaceInfo();
        }

        List<FaceInfo> faceInfoList = new ArrayList<>();
        int code = faceEngine.detectFaces(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList);
        if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
            code = faceEngine.process(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList, processMask);
            if (code != ErrorInfo.MOK) {
                 return;
            }
        }else {
            return;
        }

        Logger.e("HXS检测到人脸个数:"+faceInfoList.size());

        List<AgeInfo> ageInfoList = new ArrayList<>();
        List<GenderInfo> genderInfoList = new ArrayList<>();
        List<Face3DAngle> face3DAngleList = new ArrayList<>();
        List<LivenessInfo> faceLivenessInfoList = new ArrayList<>();
        int ageCode = faceEngine.getAge(ageInfoList);
        int genderCode = faceEngine.getGender(genderInfoList);
        int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
        int livenessCode = faceEngine.getLiveness(faceLivenessInfoList);

        //有其中一个的错误码不为0，return
        if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
            return;
        }

        if (faceRectView != null && drawHelper != null) {
            List<DrawInfo> drawInfoList = new ArrayList<>();
            for (int i = 0; i < faceInfoList.size(); i++) {
                drawInfoList.add(new DrawInfo(faceInfoList.get(i).getRect()));
            }
            drawHelper.draw(faceRectView, drawInfoList);
        }
        center_people =0 ;
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
            mFaceTask = new  FaceTask(nv21,faceInfoList, face3DAngleList);
            mFaceTask.execute((Void)null);
        }

    }



    private class FaceTask extends AsyncTask<Void,Void,Void> {
        private byte[] mData;
        private List<FaceInfo> mFaceInfoLists;
        private  List<Face3DAngle> mFace3DAngleList;
        FaceTask(byte[]  data , List<FaceInfo> faceInfos, List<Face3DAngle> face3DAngleList)
        {
            this.mData  =data;
            this.mFaceInfoLists = faceInfos;
            this.mFace3DAngleList = face3DAngleList;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            processImage(mData,mFaceInfoLists,mFace3DAngleList);
            return null;
        }
    }
    //开始对比
    private void processImage(byte[] nv21, List<FaceInfo> faceInfoList,  List<Face3DAngle> mFace3DAngleList) {

        FaceInfo faceInfo = faceInfoList.get(0);
        Face3DAngle face3DAngle = mFace3DAngleList.get(0);//获取人脸检测的信息
        FaceFeature faceFeature = new FaceFeature();

        /*
        *  Logger.e("HXS左右角度: " + face3DAngleList.get(i).getYaw());
            Logger.e("HXS上下角度: " + face3DAngleList.get(i).getPitch());
            Logger.e("HXS偏移角度: " + face3DAngleList.get(i).getRoll());
        }
        * */
        Log.d("HXS采集",previewSize.height+"  "+previewSize.width);
        int res = faceEngine.extractFaceFeature(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfo, faceFeature);
        Log.d("HXS采集",res+"");
        // 0：表示正脸采集    1：表示上微微抬头 15°  2:下微微低头°  3：左薇薇偏偏  4：右微微偏

        switch (status)
        {
            case 0:
                if(face3DAngle.getYaw()>=-5&&face3DAngle.getYaw()<=5&&face3DAngle.getPitch()>=-5&&face3DAngle.getPitch()<=5)//&&face3DAngle.getRoll()>165&&face3DAngle.getRoll()<=180)
                {

                    facemap.put(0,faceFeature.getFeatureData());
                    Logger.e("HXS正脸!");
                    face_iv.update(72);
                    status = 1 ;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SoundTipUtil.soundTip(FaceAddActivity.this,"请向上微微抬头");
                            tv_bottom.setText("请向上微微抬头!");
                            tv_title.setText("请向上微微抬头!");
                        }
                    });
                }
                break;
            case 1:
                if(face3DAngle.getYaw()>=-5&&face3DAngle.getYaw()<=5&& face3DAngle.getPitch()>=5&&face3DAngle.getPitch()<=35)//&& face3DAngle.getRoll()>=-5&&face3DAngle.getRoll()<=5)
                {
                    status=2;
                    facemap.put(1,faceFeature.getFeatureData());
                    Logger.e("HXS上脸!");
                    face_iv.update(144);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SoundTipUtil.soundTip(FaceAddActivity.this,"请向下微微低头");
                            tv_bottom.setText("请向下微微低头");
                            tv_title.setText("请向下微微低头!");
                        }
                    });
                }

                break;
            case 2:
                if(face3DAngle.getYaw()>=-5&&face3DAngle.getYaw()<=5&& face3DAngle.getPitch()>=-35&&face3DAngle.getPitch()<=-5)//&& face3DAngle.getRoll()>=-5&&face3DAngle.getRoll()<=5)
                {
                    status=3;
                    facemap.put(2,faceFeature.getFeatureData());
                    Logger.e("HXS下脸!");
                    face_iv.update(216);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_bottom.setText("请向左微微转头！");
                            SoundTipUtil.soundTip(FaceAddActivity.this,"请向左微微转头");
                            tv_title.setText("请向左微微转头!");
                        }
                    });
                }
                break;
            case 3:
                if(face3DAngle.getYaw()>=5&&face3DAngle.getYaw()<=35&&face3DAngle.getPitch()>=-5&&face3DAngle.getPitch()<=5)//&&face3DAngle.getRoll()>=-5&&face3DAngle.getRoll()<=5)
                {
                    status=4;
                    facemap.put(3,faceFeature.getFeatureData());
                    Logger.e("HXS左脸!");
                    face_iv.update(288);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SoundTipUtil.soundTip(FaceAddActivity.this,"请向右微微转头");
                            tv_bottom.setText("请向右微微转头！");
                            tv_title.setText("请向右微微转头!");
                        }
                    });
                }
                break;
            case 4:
                if(face3DAngle.getYaw()>=-35&&face3DAngle.getYaw()<=-10&&face3DAngle.getPitch()>=-5&&face3DAngle.getPitch()<=5)//&&face3DAngle.getRoll()>=-5&&face3DAngle.getRoll()<=5)
                {
                    status=5;
                    facemap.put(4,faceFeature.getFeatureData());
                    Logger.e("HXS右脸!");
                    face_iv.update(360);
                    //采集完成  上次服务器
                    isfaceTaskbool = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_title.setText("采集完成!");
                            tv_bottom.setText("采集完成！");
                            SoundTipUtil.soundTip(FaceAddActivity.this,"采集完成");
                           // ToastUtil.Success("采集完成!");
                            LoadingView.show();
                            mPresenter.addface("",1,"",facemap);
                        }
                    });
                }
                break;
        }
    }
    //上传人脸信息成功

    @Override
    public void addface(String date) {
        LoadingView.dismiss();
        finish();
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

}
