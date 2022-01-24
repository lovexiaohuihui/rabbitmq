package com.bochao.java;

import com.bochao.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

/**
 * rabbitmq 生产者 队列模式
 * @author 吴军杰
 * */
public class ProducerWorker01 {

    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 4- 获取信道
        Channel channel = RabbitMqUtils.getChannel();
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
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送成功");
        }
    }
}
