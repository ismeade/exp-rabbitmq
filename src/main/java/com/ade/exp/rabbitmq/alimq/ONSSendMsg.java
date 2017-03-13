package com.ade.exp.rabbitmq.alimq;

import com.aliyun.openservices.ons.api.*;

import java.util.Properties;

/**
 *
 * Created by liyang on 2017/3/10.
 */
public class ONSSendMsg {

    public static void main(String[] args) throws InterruptedException {
        /**
         * 设置阿里云的AccessKey，用于鉴权
         */
        final String acessKey ="";
        /**
         * 设置阿里云的SecretKey，用于鉴权
         */
        final String secretKey ="";
        /**
         * 发消息使用的一级Topic，需要先在MQ控制台里申请
         */
        final String topic ="";
        /**
         * ProducerID，需要先在MQ控制台里申请
         */
        final String producerId ="";
        Properties properties =new Properties();
        properties.put(PropertyKeyConst.ProducerId, producerId);
        properties.put(PropertyKeyConst.AccessKey, acessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        Producer producer = ONSFactory.createProducer(properties);
        producer.start();
        byte[] body=new byte[1024];
        final Message msg = new Message(
                topic,//MQ消息的Topic，需要事先申请
                "TagTest",//MQ Tag，可以进行消息过滤
                "test".getBytes());//消息体，和MQTT的body对应
        /**
         * 使用MQ客户端给MQTT设备发送P2P消息时，需要在MQ消息中设置mqttSecondTopic属性
         * 设置的值是“/p2p/”+目标ClientID
         */
        String targetClientID="GID_XXXXX@@@Client_001";
        msg.putUserProperties("mqttSecondTopic", "/p2p/"+targetClientID);
        System.out.println(msg);
        //发送消息，只要不抛异常就是成功。
        SendResult sendResult = producer.send(msg);
        System.out.println(sendResult);
        /**
         * 如果仅仅发送Pub/Sub消息，则只需要设置实际MQTT订阅的Topic即可，支持设置二级Topic
         */
        msg.putUserProperties("mqttSecondTopic", "/notice/");
        SendResult result =producer.send(msg);
        System.out.println(result);
        producer.shutdown();
        System.exit(0);
    }

}
