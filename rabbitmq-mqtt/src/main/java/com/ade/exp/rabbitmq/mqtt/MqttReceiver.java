package com.ade.exp.rabbitmq.mqtt;


import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 *
 * Created by liyang on 2017/3/3.
 */
public class MqttReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(MqttReceiver.class);

    public static Topic[] topics = {
            new Topic("exp/mqtt1", QoS.AT_MOST_ONCE),
//            new Topic("exp/mqtt2", QoS.AT_LEAST_ONCE),
//            new Topic("exp/mqtt3", QoS.EXACTLY_ONCE)
    };

    public static void main(String[] args) {
        //创建MQTT对象
        MQTT mqtt = new MQTT();
        try {
            //设置mqtt broker的ip和端口
            mqtt.setHost("192.168.101.231", 1883);
            //连接前清空会话信息
            mqtt.setCleanSession(true);
            //设置重新连接的次数
            mqtt.setReconnectAttemptsMax(6);
            //设置重连的间隔时间
            mqtt.setReconnectDelay(2000);
            //设置心跳时间
            mqtt.setKeepAlive((short) 30);
            //设置缓冲的大小
            mqtt.setSendBufferSize(2 * 1024);

            mqtt.setClientId("Mqtt-Receiver-1");

            //获取mqtt的连接对象BlockingConnection
            final FutureConnection connection = mqtt.futureConnection();
            Future<Void> connectFuture = connection.connect();
            connectFuture.await();
            System.out.println("=== connect ===");
            connection.subscribe(topics);

            while (true) {
                Future<Message> futureMessage = connection.receive();
                Message message = futureMessage.await();

                System.out.println("Receive Message: " + new String(message.getPayload()));

                message.ack(new Callback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        System.out.println("success");
                    }
                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println("fail");
                    }
                });
            }
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }
}
