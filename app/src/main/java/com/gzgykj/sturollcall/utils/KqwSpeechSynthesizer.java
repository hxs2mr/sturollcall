package com.gzgykj.sturollcall.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.orhanobut.logger.Logger;

/**
 * Data on :2019/3/22 0022
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :语音合成器
 */
public class KqwSpeechSynthesizer {


    // Log标签
    private static final String TAG = "KqwSpeechSynthesizer";

    private Context mContext;

    // 语音合成对象
    private SpeechSynthesizer mTts;

    public KqwSpeechSynthesizer(Context context) {
        mContext = context;
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, new InitListener() {
            @Override
            public void onInit(int code) {
                Logger.d(TAG, "InitListener init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    ToastUtil.Warning( "初始化失败,错误码：" + code);
                }
            }
        });

    }

    /**
     * 开始语音合成
     *
     * @param text
     */
    public void start(String text) {
        // 设置参数
        if(mTts==null)
        {
            ToastUtil.Warning("语音对象创建错误!");
            return;
        }
        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            ToastUtil.Warning( "语音合成失败,错误码：" + code);
        }
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            Log.i(TAG, "开始合成");
        }

        @Override
        public void onSpeakPaused() {
            Log.i(TAG, "暂停合成");
        }

        @Override
        public void onSpeakResumed() {
            Log.i(TAG, "继续合成");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            Log.i(TAG, "传冲进度 ：" + percent);
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            Log.i(TAG, "合成进度 ： " + percent);
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Log.i(TAG, "合成完成");
                //   mTts.startSpeaking(text, mTtsListener);
            } else if (error != null) {
                Log.i(TAG, "error : " + error.toString());
            }
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            // TODO Auto-generated method stub

        }
    };

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 设置使用本地引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
        // 设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");

        // 设置语速
        mTts.setParameter(SpeechConstant.SPEED, "45");

        // 设置音调
        mTts.setParameter(SpeechConstant.PITCH, "60");

        // 设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "60");

        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
    }

    // 获取发音人资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        // 合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        // 发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/xiaofeng.jet"));
        return tempBuffer.toString();
    }

}