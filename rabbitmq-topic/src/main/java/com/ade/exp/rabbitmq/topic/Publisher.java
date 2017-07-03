package com.ade.exp.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * 消息发布者
 * Created by liyang on 2017/3/13.
 */
public class Publisher {

    private static final String EXCHANGE_NAME = "topic.test";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.112");
        factory.setPort(5672);
        factory.setUsername("test");
        factory.setPassword("test");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //
//        channel.exchangeDeclare(EXCHANGE_NAME, // 交换机名称
//                "topic", // 交换机类型
//                true, // durable
//                true, // autoDelete
//                false, // internal
//                null); // 其他参数

        String message = "Hello World.";

        for (int i = 1; i <= 100; i++) {
            channel.basicPublish(EXCHANGE_NAME, // 交换机名称
                    "test.t1", // 主题
                    null, // 其他参数
//                    MessageProperties.PERSISTENT_TEXT_PLAIN, // 其他参数
                    (message + i).getBytes("UTF-8")); // 消息
            System.out.println(" [x] Sent :'" + message + i + "'");
        }
        connection.close();
    }

}
