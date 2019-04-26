package com.gzgykj.sturollcall.utils;

import android.content.Context;

/**
 * Data on :2019/3/22 0022
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :语音速谁哦
 */
public class SoundTipUtil {
    private static KqwSpeechSynthesizer kqwSpeechSynthesizer;

    public static void soundTip(Context context, String text) {
        kqwSpeechSynthesizer = new KqwSpeechSynthesizer(context);
        kqwSpeechSynthesizer.start(text);
    }

}
