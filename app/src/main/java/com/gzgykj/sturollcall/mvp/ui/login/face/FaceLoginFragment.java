package com.gzgykj.sturollcall.mvp.ui.login.face;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.gykj.acface.customview.FaceRectView;
import com.gykj.acface.model.DrawInfo;
import com.gykj.acface.util.CameraHelper;
import com.gykj.acface.util.CameraListener;
import com.gykj.acface.util.DrawHelper;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseFragment;
import com.gzgykj.sturollcall.mvp.ui.face.FaceActivity;
import com.gzgykj.sturollcall.utils.FaceCenter;
import com.gzgykj.sturollcall.utils.FacePermissions;
import com.gzgykj.sturollcall.utils.SoundTipUtil;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.gzgykj.sturollcall.widget.TextureVideoViewOutlineProvider;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

/**
 * Data on :2019/3/26 0026
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :人脸登录fragment
 */
public class FaceLoginFragment  extends BaseFragment<FaceLoginPresenter>implements FaceLoginContract.View, ViewTreeObserver.OnGlobalLayoutListener, CameraListener {

    @BindView(R.id.texture_preview)
    TextureView textureView;     //相机预览时候的控件
    @BindView(R.id.face_rect_view)
    FaceRectView faceRectView;

    @BindView(R.id.dialog_circle_iv)
    AppCompatImageView face_iv;
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

    private int mfaceRectViewWidth=200;
    private int mfaceRectViewHeight=200;
    private int face_h;
    private int face_w;

    @Override
    protected void initInject() {
            getFragmentComponent().inject(this);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_facelogin;
    }

    @Override
    protected void initData() {
        super.initData();
        facePermissions = new FacePermissions(getContext());
        //在布局结束后才做初始化操作
        textureView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        face_iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                face_h = face_iv.getHeight();
                face_w = face_iv.getWidth();
                Logger.i("face_h " + face_h + "  face_w:" + face_w);
            }
        });

    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

  /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textureView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }*/

        if (!facePermissions.checkPermissions(facePermissions.NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), facePermissions.NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initCamera();
            initEngine();
        }

    }

    /**
     * 初始化人脸
     */
    private void initEngine() {
        faceEngine = new FaceEngine();
        afcode = faceEngine.init(getContext(), FaceEngine.ASF_DETECT_MODE_VIDEO,FaceEngine.ASF_OP_0_HIGHER_EXT,
                16, 20, FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Logger.i("Face: afcode: " + afcode + "  version:" + versionInfo);
        if (afcode != ErrorInfo.MOK) {
            ToastUtil.Error("人脸识别初始化失败："+afcode);
        }
        //语音播放
        SoundTipUtil.soundTip(getContext(),"人脸对应识别框识别");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SoundTipUtil.soundTip(getContext(),"未找到用户信息");
            }
        },3000);
    }


    /**
     * 初始化相机
     */
    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        cameraHelper = new CameraHelper.Builder()
                .previewSize(previewSize)
                .metrics(metrics)
                //.previewViewSize(new Point(textureView.getMeasuredWidth(),textureView.getMeasuredHeight()))
                .rotation(  Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(cameraID != null ? cameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(textureView)
                .cameraListener(this)
                .build();
        cameraHelper.init();
        Logger.e("HXS 开始初始化相机");
    }

    /**
     * 获取当前frgamnet是否可见   就是呈现在用户面前
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(cameraHelper!=null)
        {
            if(!isVisibleToUser)//如果当前人脸识别frgament不可见
            {
                cameraHelper.stop();
            }else {
                cameraHelper.start();
            }
        }
        if(isVisibleToUser){
            hideSoftInput();
        }

        Logger.e("HXS  "+isVisibleToUser);
    }

    /**
     * 相机回调的方法
     */

    @Override
    public void onGlobalLayout() {
        mfaceRectViewWidth = faceRectView.getWidth();
        mfaceRectViewHeight = faceRectView.getHeight();
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

    @Override
    public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
        previewSize = camera.getParameters().getPreviewSize();
        Logger.e("HXS"+mfaceRectViewHeight+"::::"+mfaceRectViewWidth+"-----"+previewSize.height + previewSize.width);
        faceCenter = new FaceCenter(mfaceRectViewWidth,mfaceRectViewHeight,previewSize);
        drawHelper = new DrawHelper(previewSize.width, previewSize.height, textureView.getWidth(), textureView.getHeight(), displayOrientation
                , cameraId, true);

        /**
         * 设置为圆形
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textureView.setOutlineProvider(new TextureVideoViewOutlineProvider( 1000,textureView.getWidth()/2-face_h/2,textureView.getHeight()/2-face_w/2));
            textureView.setClipToOutline(true);
        }

        Logger.e("HXS 相机打开");
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
        if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
            code = faceEngine.process(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList, processMask);
            if (code != ErrorInfo.MOK) {
                return;
            }
        }else {
            return;
        }
        for (int i = 0; i < faceInfoList.size(); i++) {
            Logger.i(TAG+"onPreview:  人脸特征[" + i + "] = " + faceInfoList.get(i).toString());
        }

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

        for(int i = 0; i < faceInfoList.size(); i ++){
            if(faceCenter.checkIsCenter(faceInfoList.get(i).getRect())){
    /*            FaceFeature faceFeature = new FaceFeature();
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
                        if (faceSimilar.getScore() >= 0.9f) {
                            System.out.println("=======用户id====" + mFaceList.get(j).getUser_id());
                            //人脸识别成功 返回接口
                            recognizeListener.recognize(mFaceList.get(j).getUser_id(), mFaceList.get(j).getUserType());
                            dismiss();
                            break;
                        }
                        long end = System.currentTimeMillis();
                        System.out.println("=======识别时间====" + (end - start));
                    }
                    System.out.println("=======对比次数====" + number);
                    mCount++;
                    isRecognize = false;
                    if(mCount == 5){
                        mCount = 0;
                        mHandler.sendEmptyMessage(RECOGNIZED);
                    }
                }*/
            }
        }
    }

    @Override
    public void onCameraClosed() {

    }

    @Override
    public void onCameraError(Exception e) {
        Logger.e("HXS 相机出错"+e.getMessage());
    }

    @Override
    public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
        if (drawHelper != null) {
            drawHelper.setCameraDisplayOrientation(displayOrientation);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraHelper.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    public void onDestroy() {
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
