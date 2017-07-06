package com.ade.exp.rabbitmq.topic;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消息订阅者
 * Created by liyang on 2017/3/13.
 */
public class Subscriber {

    private static final String EXCHANGE_NAME = "topic.test4";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.112");
        factory.setUsername("test");
        factory.setPassword("test");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 如果没有该交换机时，会自动创建，没有指定的交换机后边将报错，如果交换机已经存在，这行可以省略
        channel.exchangeDeclare(EXCHANGE_NAME, // 交换机名称
                "topic", // 交换机类型
                true, // durable
                true, // autoDelete
                false, // internal
                null); // 其他参数
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, // 队列名称
                EXCHANGE_NAME, // 交换机名称
                "test.#"); // 绑定主题

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, false, consumer);
    }

}
