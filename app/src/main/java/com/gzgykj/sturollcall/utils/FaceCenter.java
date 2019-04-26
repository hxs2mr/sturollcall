package com.gzgykj.sturollcall.utils;

import android.graphics.Rect;
import android.hardware.Camera;

/**
 * Data on :2019/3/22 0022
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :判断是否是人脸中心区域
 */
public class FaceCenter {
    /**
     * 判断是否中心点
     */
    private int mfaceRectViewWidth;
    private int mfaceRectViewHeight;
    private    Camera.Size previewSize;
    public FaceCenter(int fw,int fh,  Camera.Size ps)
    {
        this.mfaceRectViewWidth = fw;
        this.mfaceRectViewHeight  =fh;
        this.previewSize = ps;
    }
    public  boolean checkIsCenter(Rect rect){
        float l = (mfaceRectViewWidth/2 )*((float)previewSize.width/mfaceRectViewWidth);
        float t = (mfaceRectViewHeight/2 )*((float)previewSize.height/mfaceRectViewHeight);
        float r = (mfaceRectViewWidth/2 )*((float)previewSize.width/mfaceRectViewWidth);
        float b = (mfaceRectViewHeight/2 )*((float)previewSize.height/mfaceRectViewHeight);
        if(rect.left >= l
                && rect.top >= t
                && rect.right <= r
                && rect.bottom <= b){
            return true;
        }
        return false;
    }

}
