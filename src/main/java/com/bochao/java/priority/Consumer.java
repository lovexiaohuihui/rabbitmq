package com.bochao.java.priority;

import com.rabbitmq.client.*;

/**
 * 消费者 队列模式
 * @author 吴军杰
 * */
public class Consumer {

    // 队列名称
    public static final String QUEUE_NAME = "hello";

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
        // 5- 消费消息
        // 消息消息回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };
        // 消息失败回调函数
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费失败!!");
        };
        /**
         * 参数
         *  1- 队列名称
         *  2- 消费成功后是否自动应答
         *  3- 消费消息回调函数
         *  4- 消息失败回调函数
         * */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
