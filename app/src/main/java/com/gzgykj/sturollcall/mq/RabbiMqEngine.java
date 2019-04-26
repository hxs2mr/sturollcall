package com.gzgykj.sturollcall.mq;


import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.gykj.jxfvlibrary.domain.FvEntity;
import com.gykj.jxfvlibrary.listener.OnJxFvListener;
import com.gykj.jxfvlibrary.manager.JXFvManager;
import com.gzgykj.basecommon.model.AddLossBean;
import com.gzgykj.basecommon.model.MQSendBean;
import com.gzgykj.basecommon.model.MqInitBean;
import com.gzgykj.basecommon.model.LossPageBean;
import com.gzgykj.basecommon.model.facerealm.FaceDataBean;
import com.gzgykj.basecommon.model.facerealm.FaceRealm;
import com.gzgykj.basecommon.model.facerealm.FeatureBean;
import com.gzgykj.basecommon.model.facerealm.IAddFaceListener;
import com.gzgykj.basecommon.model.facerealm.RealmManager;
import com.gzgykj.basecommon.model.facerealm.StuDataBean;
import com.gzgykj.basecommon.model.facerealm.StuRespose;
import com.gzgykj.basecommon.response.AppRespose;
import com.gzgykj.basecommon.response.MQRespose;
import com.gzgykj.basecommon.utils.Constants;
import com.gzgykj.basecommon.utils.ThreadManager;
import com.gzgykj.sturollcall.mvp.model.AddWarr;
import com.gzgykj.sturollcall.mvp.model.CallStopBean;
import com.gzgykj.sturollcall.mvp.model.EvAddWarrBean;
import com.gzgykj.sturollcall.mvp.model.InCallBean;
import com.gzgykj.sturollcall.mvp.model.PoliceBean;
import com.gzgykj.sturollcall.mvp.model.StuListBean;
import com.gzgykj.sturollcall.mvp.model.UpFaceJxBean;
import com.gzgykj.sturollcall.utils.SharedPreferencedUtils;
import com.orhanobut.logger.Logger;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * desc   : RabbiMq引擎类
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/9/49:57
 * version: 1.0
 */
public class RabbiMqEngine {

    private ConnectionFactory factory;

    private Handler handler = new Handler();
    private Connection connection;

    byte[] group1_byte = new byte[20];
    byte[] veinsId = new byte[250];

    private RabbiMqEngine(){
        // 声明ConnectionFactory对象
        factory = new ConnectionFactory();
    }

    private static class RabbiMqEngineHolder{
        private static RabbiMqEngine instance = new RabbiMqEngine();
    }

    public static RabbiMqEngine getRabbiMqEngine(){
        return RabbiMqEngineHolder.instance;
    }

    public void setUpConnectionFactory(){
        factory.setHost(Constants.MQ_HOST);//主机地址：
        factory.setPort(Constants.MQ_PORT);// 端口号
        factory.setUsername(Constants.MQ_USERNAME);// 用户名
        factory.setPassword(Constants.MQ_PASSWORD);// 密码
        factory.setAutomaticRecoveryEnabled(true);// 设置连接恢复
    }

    public  void sendFaceJXMessage(boolean face_init,boolean jx_init,List<Long> data)
    {
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    //创建连接
                    connection = factory.newConnection();

                    //创建通道
                    Channel channel = connection.createChannel();

                    //2.指定一个队列
                    channel.queueDeclare(Constants.QUEUE_NAME+"654321", true, false, false, null);// 声明共享队列
                    //人脸
                        //人脸  MQ初始化 发送的消息
                        if(face_init){
                            FeatureBean entity = new FeatureBean();
                            entity.setEvent("FACE_FEATURES");
                            entity.setMethod("SYN_INIT");
                            entity.setDevieceType(2);
                            entity.setDeviceId("654321");
                            entity.setData(data);
                            //3.往队列中发出一条消息
                            System.out.println("HXS学生人脸的数据:"+JSON.toJSONString(entity));
                            channel.basicPublish("cashierTopicExchange", "topic.cashier.server", null, JSON.toJSONString(entity).getBytes());
                        }

                        if(jx_init)
                        {
                            FeatureBean entity = new FeatureBean();
                            entity.setEvent("FINGERS_FEATURES");
                            entity.setMethod("SYN_INIT");
                            entity.setDevieceType(2);
                            entity.setDeviceId("654321");
                            entity.setData(data);
                            //3.往队列中发出一条消息
                            Logger.d("HXS学生指静脉的数据:"+JSON.toJSONString(entity));
                            channel.basicPublish("cashierTopicExchange", "topic.cashier.server", null, JSON.toJSONString(entity).getBytes());
                        }
                    //4.关闭频道和连接
                    channel.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *
     * @param face_init
     * @param flage
     * @param pagesize
     * @param pageindex
     */

    public void sendMessage(final boolean face_init, final int flage, final int pagesize, final int pageindex,int userid,int callid){
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                        //创建连接
                        connection = factory.newConnection();

                        //创建通道
                        Channel channel = connection.createChannel();

                        //2.指定一个队列
                        channel.queueDeclare(Constants.QUEUE_NAME+"654321", true, false, false, null);// 声明共享队列
                        //人脸
                        if(face_init){
                            MQRespose entity = new MQRespose<MQSendBean>();

                            entity.setDevieceType(2);
                            entity.setDeviceId("654321");

                            switch (flage)
                            {

                                case 0:
                                    entity.setMethod("SYN_INIT");
                                    entity.setEvent("DORMITORY_EQUIPMENT");
                                    break;
                                case 1:
                                    MQSendBean bean = new MQSendBean();
                                    entity.setMethod("PAGE");
                                    entity.setEvent("DORMITORY_REPORT_LOSS");
                                    bean.setCurrent(pageindex);
                                    bean.setDormitoryId(SharedPreferencedUtils.getInt(Constants.DORMITORYID,1));
                                    bean.setSize(pagesize);
                                    bean.setStatus(1);
                                    entity.setData(bean);
                                    break;
                                case 2: //报警MQ
                                    AddWarr addWarr = new AddWarr();
                                    entity.setMethod("INSERT");
                                    entity.setEvent("DORMITORY_WARNING");
                                    addWarr.setBuildingId(SharedPreferencedUtils.getInt(Constants.BUILDID,1));
                                    addWarr.setDormitoryId(SharedPreferencedUtils.getInt(Constants.DORMITORYID,1));
                                    entity.setData(addWarr);
                                    break;
                                case 3: //报警记录列表
                                    MQSendBean warrpg = new MQSendBean();
                                    entity.setMethod("PAGE");
                                    entity.setEvent("DORMITORY_WARNING");
                                    warrpg.setCurrent(pageindex);
                                    warrpg.setDormitoryId(SharedPreferencedUtils.getInt(Constants.DORMITORYID,1));
                                    warrpg.setSize(pagesize);
                                    entity.setData(warrpg);
                                    break;
                                case 4://上报签到的情况
                                    UpFaceJxBean jxBean = new UpFaceJxBean();
                                    entity.setMethod("UPDATE");
                                    entity.setEvent("DORMITORY_LAUNCH_CALL");
                                    jxBean.setRollCallId(userid);
                                    jxBean.setUserId(callid);
                                    jxBean.setDormitoryId(SharedPreferencedUtils.getInt(Constants.DORMITORYID,1));
                                    entity.setData(jxBean);
                                    break;
                            }
                            System.out.println("HXS传的数据:"+JSON.toJSONString(entity));
                            //3.往队列中发出一条消息
                            channel.basicPublish("cashierTopicExchange", "topic.dormitory.server", null, JSON.toJSONString(entity).getBytes());
                        }
                       //4.关闭频道和连接
                        channel.close();
                        connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取学生信息
     */
    public  void sendStuDataMessage(boolean stuinit)
    {
            ThreadManager.getInstance().submit(new Runnable() {
                @Override
                public void run() {
                    Connection connection = null;
                    try {
                        //创建连接
                        connection = factory.newConnection();

                        //创建通道
                        Channel channel = connection.createChannel();

                        //2.指定一个队列
                        channel.queueDeclare(Constants.QUEUE_NAME+"654321", true, false, false, null);// 声明共享队列
                        //人脸
                        if(stuinit){
                            //人脸  MQ初始化 发送的消息
                            if(stuinit){
                                MQRespose entity = new MQRespose();
                                entity.setEvent("DORMITORY_BEDS");
                                entity.setMethod("SELECT");
                                entity.setDevieceType(2);
                                entity.setDeviceId("654321");
                                entity.setData(SharedPreferencedUtils.getInt(Constants.DORMITORYID,1));
                                //3.往队列中发出一条消息
                                System.out.println("HXS学生列表的数据:"+JSON.toJSONString(entity));
                                channel.basicPublish("cashierTopicExchange", "topic.dormitory.server", null, JSON.toJSONString(entity).getBytes());
                            }
                        }
                        //4.关闭频道和连接
                        channel.close();
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    /**int buildingId,int dormitoryId,
     * 发送添加的消息
     * @param face_init
     */
    public void sendAddMessage(final boolean face_init, final String userName, final String articleName, final String spec, final String reportDesc){
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    //创建连接
                    connection = factory.newConnection();

                    //创建通道
                    Channel channel = connection.createChannel();

                    //2.指定一个队列
                    channel.queueDeclare(Constants.QUEUE_NAME+"654321", true, false, false, null);// 声明共享队列
                    //人脸
                    if(face_init){
                        MQRespose<AddLossBean> entity = new MQRespose<AddLossBean>();
                        AddLossBean bean = new AddLossBean();
                        entity.setDevieceType(2);
                        entity.setDeviceId("654321");
                        entity.setMethod("INSERT");
                        entity.setEvent("DORMITORY_REPORT_LOSS");

                        bean.setBuildingId(SharedPreferencedUtils.getInt(Constants.BUILDID,1));
                        bean.setDormitoryId(SharedPreferencedUtils.getInt(Constants.DORMITORYID,1));
                        bean.setUserName(userName);
                        bean.setSpec(spec);
                        bean.setArticleName(articleName);
                        bean.setReportDesc(reportDesc);
                        entity.setData(bean);
                        System.out.println("HXS  新增传的数据:"+JSON.toJSONString(entity));
                        //3.往队列中发出一条消息
                        channel.basicPublish("cashierTopicExchange", "topic.dormitory.server", null, JSON.toJSONString(entity).getBytes());
                    }

                    //4.关闭频道和连接
                    channel.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**int buildingId,int dormitoryId,
     * 发送添加的消息
     * @param face_init
     */
    public void sendEndMessage(final boolean face_init, String entity){
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    //创建连接
                    connection = factory.newConnection();

                    //创建通道
                    Channel channel = connection.createChannel();

                    //2.指定一个队列
                    channel.queueDeclare(Constants.QUEUE_NAME+"654321", true, false, false, null);// 声明共享队列
                    //人脸
                    if(face_init){
                        //3.往队列中发出一条消息
                        channel.basicPublish("cashierTopicExchange", "topic.dormitory.server", null, entity.getBytes());
                    }

                    //4.关闭频道和连接
                    channel.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 销毁连接
     */
    public void destoryConnect(){
        if(null != connection){
            try {
                connection.abort();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                connection = null;
            }
        }
    }

    public void connect(final String deviceId){
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    connection = factory.newConnection();

                    //创建通道
                     final Channel channel = connection.createChannel();

                    //命名一个队列
                    String queueName = Constants.QUEUE_NAME+deviceId;

                    // 声明队列（持久的、非独占的、连接断开后队列不会自动删除）
                    AMQP.Queue.DeclareOk q = channel.queueDeclare(queueName, true, false, false, null);// 声明共享队列

                    // 根据路由键将队列绑定到交换机上（需要知道交换机名称和路由键名称）
                    channel.queueBind(q.getQueue(), Constants.MQ_EXCHANGE_CAR, Constants.QUEUE_NAME+deviceId);
                    // 创建消费者获取rabbitMQ上的消息。每当获取到一条消息后，就会回调handleDelivery（）方法，
                    // 该方法可以获取到消息数据并进行相应处理
                    Consumer consumer = new DefaultConsumer(channel){
                        @Override
                        public void handleDelivery(String consumerTag, final Envelope envelope, AMQP.BasicProperties properties, final byte[] body) throws IOException {
                            super.handleDelivery(consumerTag, envelope, properties, body);
                            // 通过geiBody方法获取消息中的数据
                            // 发消息通知UI更新
                          //  Logger.e("HXS","MQ链接成功!");
                            //
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    Logger.d("HXS 返回的数据:"+new String(body));

                                }
                            });
                             MQRespose entity = JSON.parseObject(body,MQRespose.class);  //获取到的消息进行转换

                            String mEvent = "";
                            if(null != entity){
                                mEvent = entity.getEvent();
                            }
                            //添加  跟新  删除  人脸数据库 和 指静脉数据库
                            switch (mEvent){
                                case "DORMITORY_REPORT_LOSS":

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                                if(entity.getMethod().equals("INSERT")){
                                                    EventBus.getDefault().post("添加成功");
                                                }else {
                                                    LossPageBean PGINIT = JSON.parseObject(JSON.toJSONString(entity.getData()), LossPageBean.class);
                                                    Logger.e("HXS分页返回的数据:"+ JSON.toJSONString(PGINIT));
                                                    EventBus.getDefault().post(PGINIT);
                                                }
                                        }
                                    });
                                    channel.basicAck(envelope.getDeliveryTag(),false);
                                    break;
                                case "DORMITORY_EQUIPMENT":
                                        AppRespose appRespose =  JSON.parseObject(JSON.toJSONString(entity .getData()), AppRespose.class);
                                        MqInitBean MQINIT = JSON.parseObject(JSON.toJSONString(appRespose.getData()), MqInitBean.class);
                                        handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                                SharedPreferencedUtils.setInt(Constants.BUILDID,MQINIT.getBuildingId());
                                                SharedPreferencedUtils.setInt(Constants.DORMITORYID,MQINIT.getDormitoryId());
                                                Logger.e("HXS初始化:"+MQINIT.getBuildingId());
                                        }
                                    });
                                    channel.basicAck(envelope.getDeliveryTag(),false);
                                    break;
                                case "DORMITORY_WARNING":
                                    handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            if(entity.getMethod().equals("INSERT"))
                                            {
                                                Logger.e("HXS添加报警返回!"+JSON.toJSONString(entity));
                                                EventBus.getDefault().post(new EvAddWarrBean(""));
                                            }else {
                                                    PoliceBean policeBean = JSON.parseObject(JSON.toJSONString(entity.getData()), PoliceBean.class);
                                                Logger.e("HXS报警列表返回!"+JSON.toJSONString(entity));
                                                EventBus.getDefault().post(policeBean);
                                            }

                                        }
                                    });
                                    channel.basicAck(envelope.getDeliveryTag(),false);
                                    break;
                                case "DORMITORY_BEDS"://获取学生信息中的userid   然后再去获取人脸face特征值
                                    StuRespose data =  JSON.parseObject(JSON.toJSONString(entity.getData()), StuRespose.class);
                                    List<StuDataBean>  list= data.getData();
                                    List<Long> list_id = new ArrayList<>();

                                    //获取学生信息USERid
                                    for (int i =0 ; i < list.size();i++)
                                    {
                                        list_id.add(list.get(i).getUserId());
                                    }
                                    EventBus.getDefault().post(new StuListBean(list_id));

                          //           RabbiMqEngine.getRabbiMqEngine().sendFaceJXMessage(true,list_id );
                        /*          String dataJson = JSON.(entity.getData());
                                     List<StuDataBean>  list= JSON.parseArray(dataJson, StuDataBean.class);
                                     Logger.e("HXS学生信息列表***********:"+list.size());*/
                                    channel.basicAck(envelope.getDeliveryTag(),false);
                                    break;

                                case  "FACE_FEATURES"://人脸入库  增加  删除   更新(人脸数据库的初始化)

                                    /*
                                    *  AppRespose appRespose =  JSON.parseObject(JSON.toJSONString(entity .getData()), AppRespose.class);
                                        MqInitBean MQINIT = JSON.parseObject(JSON.toJSONString(appRespose.getData()), MqInitBean.class);
                                    * */
                                    FaceDataBean dataBean =  JSON.parseObject(JSON.toJSONString(entity.getData()), FaceDataBean.class);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Logger.e("HXS人脸返回的数据:"+JSON.toJSONString(entity));

                                            if(entity.getMethod().equals("INSERT"))
                                            {
                                                 insertFace(dataBean,channel,envelope);
                                            }else {
                                                deleteFace(dataBean,channel,envelope);
                                            }
                                        }
                                    });
                                    break;
                                case "FINGERS_FEATURES":
                                    Logger.e("HXS指静脉返回的数据:"+JSON.toJSONString(entity));
                                    channel.basicAck(envelope.getDeliveryTag(),false);
                                    break;

                                case "DORMITORY_LAUNCH_CALL":

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //表示停止点名
                                            if(entity.getMethod().equals("STOP"))
                                            {
                                                int callid = Integer.parseInt(entity.getData()+"");
                                                Logger.e("HXS结束点名:"+ JSON.toJSONString(entity));
                                                EventBus.getDefault().post(new CallStopBean(callid));
                                            } else  if(entity.getMethod().equals("SYN_INIT")){//发起点名
                                                InCallBean inCallBean =  JSON.parseObject(JSON.toJSONString(entity.getData()), InCallBean.class);
                                                EventBus.getDefault().post(inCallBean);
                                                Logger.e("HXS开始点名:"+ JSON.toJSONString(entity));
                                            }

                                        }
                                    });



                                    channel.basicAck(envelope.getDeliveryTag(),false);
                                    break;
                              }

                        }
                    };
                    channel.basicConsume(q.getQueue(), false, consumer);
                } catch (Exception e) {
                    Logger.e("MQ链接失败:"+e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 插入人脸数据
     * @param entity
     * @param channel
     * @param envelope
     */
    private void insertFace(final FaceDataBean entity, final Channel channel, final Envelope envelope){
        Realm mRealm = Realm.getDefaultInstance();
        FaceRealm faceRealm = mRealm.where(FaceRealm.class).equalTo("userId", entity.getUserId()).equalTo("featuresType", entity.getFeaturesType()).findFirst();
        if(null == faceRealm){
            RealmManager.getInstance().addFaceToRealm(entity.getUserId(), entity.getFeaturesType(), entity.getFeatures(), new IAddFaceListener() {
                @Override
                public void OnSuccess() {
                    Logger.e("HXS"+entity.getUserId() +"：添加人脸成功");
                    try {
                        channel.basicAck(envelope.getDeliveryTag(),false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(Throwable error) {
                    Logger.e("HXS"+entity.getUserId() +"=添加人脸失败"+error.getMessage());
                }
            });
        }else {
              updateFace( entity, channel, envelope);
        }
    }

    /**
     * 更新对应人脸数据
     * @param entity
     * @param channel
     * @param envelope
     */
    private void updateFace(final FaceDataBean entity, final Channel channel, final Envelope envelope){
        RealmManager.getInstance().updateFaceToRealm(entity.getUserId(),entity.getFeaturesType(), entity.getFeatures(), new IAddFaceListener() {
            @Override
            public void OnSuccess() {
                Logger.e("HXS"+entity.getUserId() +"：修改人脸成功");
                try {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable error) {
                Logger.e("HXS"+entity.getUserId() +"=修改人脸失败"+error.getMessage());
            }
        });
    }
    /**
     * 删除某一个人脸数据
     * @param entity
     * @param channel
     * @param envelope
     */
    private void deleteFace(final FaceDataBean entity, final Channel channel, final Envelope envelope){
        RealmManager.getInstance().deleteFaceToRealm(entity.getUserId(),entity.getFeaturesType(), entity.getFeatures(), new IAddFaceListener() {
            @Override
            public void OnSuccess() {
                Logger.e("HXS"+entity.getUserId() +"：删除人脸成功");
                try {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable error) {
                Logger.e("HXS"+entity.getUserId() +"=删除人脸失败"+error.getMessage());
            }
        });
    }



    /**指静脉模块**/

    //插入指静脉数据
    private void insertFv(FvEntity entity, final Channel channel, final Envelope envelope){
        byte[] veinsId = new byte[50];
        StringBuffer buffer = new StringBuffer();
        buffer.append(entity.getData().getUserType())
                .append("_")
                .append(entity.getData().getUserId())
                .append("_")
                .append(entity.getData().getFeaturesType());
        System.arraycopy(buffer.toString().getBytes(), 0,veinsId,0,buffer.toString().getBytes().length);
        JXFvManager.getInstance().jxAddTwoVeinFeature(entity.getData().getFeatures2(), entity.getData().getFeatures3(), veinsId, new OnJxFvListener() {
            @Override
            public void success() {
                Logger.d("HXS添加指静脉成功");
                try {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String error) {
                Logger.d("HXS添加指静脉失败"+error);
            }
        });
    }

    //跟新
    private void updateFv(final FvEntity entity, final Channel channel, final Envelope envelope){
        byte[] veinsId = new byte[50];
        StringBuffer buffer = new StringBuffer();
        buffer.append(entity.getData().getUserType())
                .append("_")
                .append(entity.getData().getUserId())
                .append("_")
                .append(entity.getData().getFeaturesType());
        System.arraycopy(buffer.toString().getBytes(), 0,veinsId,0,buffer.toString().getBytes().length);
        JXFvManager.getInstance().jxRemoveVeinFeature(veinsId, new OnJxFvListener() {
            @Override
            public void success() {
                Logger.d("HXS修改指纹成功");
                insertFv(entity,channel,envelope);
            }

            @Override
            public void failed(String error) {
                Logger.d("HXS修改指纹失败"+error);
            }
        });

    }

    //删除
    private void deleteFv(FvEntity entity, final Channel channel, final Envelope envelope){
        byte[] veinsId = new byte[50];
        StringBuffer buffer = new StringBuffer();
        buffer.append(entity.getData().getUserType())
                .append("_")
                .append(entity.getData().getUserId())
                .append("_")
                .append(entity.getData().getFeaturesType());
        System.arraycopy(buffer.toString().getBytes(), 0,veinsId,0,buffer.toString().getBytes().length);
        JXFvManager.getInstance().jxRemoveVeinFeature(veinsId, new OnJxFvListener() {
            @Override
            public void success() {
                Logger.d("HXS删除指纹成功");
                try {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failed(String error) {
                Logger.d("HXS删除指纹失败"+error);
            }
        });
    }


}
