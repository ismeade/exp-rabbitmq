package com.ade.exp.rabbit.queue.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息生产者
 * Created by liyang on 2017/3/13.
 */
public class Producer {

    public final static String QUEUE_NAME = "rabbitMQ.test";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ相关信息
        factory.setHost("192.168.101.112");
        factory.setUsername("test");
        factory.setPassword("test");
        // 创建一个新的连接
        Connection connection = factory.newConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME, // 队列名称
                false, // 是否持久化（true表示是，队列将在服务器重启时生存）
                false, // 是否是独占队列（创建者可以使用的私有队列，断开后自动删除）
                true, // 当所有消费者客户端连接断开时是否自动删除队列
                null); // 其他参数
        String message = "Hello RabbitMQ";
        // 发送消息到队列中
        for (int i = 1; i <= 10; i++) {
            channel.basicPublish("", // 交换机名称
                    QUEUE_NAME, // 队列映射的路由key
                    null, // 其他属性
                    (message + i).getBytes("UTF-8")); // 信息
            System.out.println("Producer Send +'" + message + i + "'");
        }
        // 关闭通道和连接
        channel.close();
        connection.close();
    }
}
