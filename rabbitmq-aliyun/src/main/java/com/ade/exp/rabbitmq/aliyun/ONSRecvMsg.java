package com.ade.exp.rabbitmq.aliyun;

import com.aliyun.openservices.ons.api.*;

import java.util.Properties;

/**
 *
 * Created by liyang on 2017/3/10.
 */
public class ONSRecvMsg {

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
         * 收消息使用的一级Topic，需要先在MQ控制台里申请
         */
        final String topic ="";
        /**
         * ConsumerID ,需要先在MQ控制台里申请
         */
        final String consumerID ="";
        Properties properties =new Properties();
        properties.put(PropertyKeyConst.ConsumerId, consumerID);
        properties.put(PropertyKeyConst.AccessKey, acessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        Consumer consumer =ONSFactory.createConsumer(properties);
        /**
         * 此处MQ客户端只需要订阅MQTT的一级Topic即可
         */
        consumer.subscribe(topic, "*", (message, consumeContext) -> {
            System.out.println("recv msg:"+message);
            return Action.CommitMessage;
        });
        consumer.start();
        System.out.println("[Case Normal Consumer Init]   Ok");
        Thread.sleep(Integer.MAX_VALUE);
        consumer.shutdown();
        System.exit(0);
    }

}
