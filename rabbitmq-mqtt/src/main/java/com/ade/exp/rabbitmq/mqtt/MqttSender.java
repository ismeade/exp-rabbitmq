package com.ade.exp.rabbitmq.mqtt;

import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by liyang on 2017/3/3.
 */
public class MqttSender {

    private static final Logger LOG = LoggerFactory.getLogger(MqttSender.class);

    public static void main(String[] args) {
        MQTT mqtt = new MQTT();
        try {
            //设置服务端的ip
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

            final FutureConnection connection = mqtt.futureConnection();
            Future<Void> connectFuture = connection.connect();
            connectFuture.await();
            System.out.println("=== connect ===");
            while (true) {
                String message = new SimpleDateFormat("HH:mm:ss").format(new Date());
                Future<Void> future = connection.publish("exp/mqtt1", message.getBytes("utf-8") , QoS.AT_MOST_ONCE, true);
                future.await();
                System.out.println("Send Message: " + message);
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }
}
