package com.bochao.java;

import com.bochao.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * rabbitmq 生产者 队列模式
 * @author 吴军杰
 * */
public class ProducerNack {

    // 队列名称
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        // 4- 获取信道
        Channel channel = RabbitMqUtils.getChannel();
        // 发布确认
        channel.confirmSelect();
        // 5- 生成队列
        /**
         * 参数
         *  1- 队列名称
         *  2- 是否持久化 默认false
         *  3- 是否支持多个消费者消费
         *  4- 是否自动删除
         *  5- 其他参数
         * */
        // 队列持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        // 6- 发送消息
        // 从控制台获取消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            /**
             * 参数
             *  1- 交换机名称
             *  2- 路由的key值
             *  3- 其他参数
             *  4- 消息体
             * */
            String message = scanner.next();
            // 消息持久化进磁盘 MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("消息发送成功:" + message);
        }
    }
}
