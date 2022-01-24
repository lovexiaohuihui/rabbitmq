package com.bochao.java;

import com.bochao.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * rabbitmq 生产者 交换机 fanout 发布订阅
 * @author 吴军杰
 * */
public class ProducerFanoutLogs {

    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        // 1- 获取信道
        Channel channel = RabbitMqUtils.getChannel();

        // 2- 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 3- 发送消息
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
            channel.basicPublish(EXCHANGE_NAME, "333", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("消息发送成功:" + message);
        }
    }
}
