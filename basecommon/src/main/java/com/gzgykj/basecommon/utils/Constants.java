package com.gzgykj.basecommon.utils;

import android.os.Environment;

import java.io.File;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on : 公用参数类
 */
public final class Constants {

    /**
     * RabbitMq
     *
     */
    public static final String MQ_HOST = "120.78.175.59";
    public static final int MQ_PORT = 33675;
    public static final String MQ_USERNAME = "cashier";
    public static final String MQ_PASSWORD = "pass4cashier";

    public static final String QUEUE_NAME = "topic.dormitory.local.";
    public static final String MQ_EXCHANGE_CAR = "cashierTopicExchange";

    public static final String FACE_DB_PATH = Environment.getExternalStorageDirectory() + File.separator+"faceDb/";
    public static final String DB_NAME = "FACE";

    public static final String PROJECT_NAME = "HXS";
    public static final String DELITE = "SS";

    public static final int PAGE_SIEZ= 10;

    /*缓存路径*/
    public static final String PATH_SDCARD = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + PROJECT_NAME + File.separator + "hxs";

    /**
     * 请求的token
     */
    public static String TOKEN="";

    public static  String BUILDID="BUILDID";
    public static  String DORMITORYID="DORMITORYID";

    public static  int MQinit=0;
    public static  int LossPage=1;
    public static  int ADDWarring=2;
    public static  int WarringPage=3;
    public static  int UPFACEJX=4;
    //点名签到模块
    public static boolean ISCALL= false;
    public static String CALLID="CALLID";
}
