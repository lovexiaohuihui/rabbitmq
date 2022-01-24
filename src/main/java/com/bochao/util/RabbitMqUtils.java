package com.bochao.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * rabbitmq 工具类
 * */
public class RabbitMqUtils {

    /**
     * 获取信道
     * */
    public static Channel getChannel() throws Exception{
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
        return channel;
    }
}
