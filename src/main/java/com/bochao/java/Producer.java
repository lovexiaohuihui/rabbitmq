package com.bochao.java;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * rabbitmq 生产者 队列模式
 * @author 吴军杰
 * */
public class Producer {

    // 队列名称
    public static final String QUEUE_NAME = "mirror_hello";

    public static void main(String[] args) throws Exception {
        // 1- 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 2- 赋值连接需要参数
        connectionFactory.setHost("192.168.110.228");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        // 3- 开启连接
        Connection connection = connectionFactory.newConnection();
        // 4- 获取信道
        Channel channel = connection.createChannel();
        // 5- 生成队列
        /**
         * 参数
         *  1- 队列名称
         *  2- 是否持久化 默认false
         *  3- 是否支持多个消费者消费
         *  4- 是否自动删除
         *  5- 其他参数
         * */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 6- 发送消息
        String message = "hello word!";
        /**
         * 参数
         *  1- 交换机名称
         *  2- 路由的key值
         *  3- 其他参数
         *  4- 消息体
         * */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("消息发送成功");
    }
}
