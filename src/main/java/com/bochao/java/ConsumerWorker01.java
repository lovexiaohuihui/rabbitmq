package com.bochao.java;

import com.bochao.util.RabbitMqUtils;
import com.rabbitmq.client.*;

/**
 * 消费者 队列模式
 * @author 吴军杰
 * */
public class ConsumerWorker01 {

    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 4- 获取信道
        Channel channel = RabbitMqUtils.getChannel();
        // 5- 消费消息
        // 消息消息回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        // 消息失败回调函数
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费失败");
        };
        /**
         * 参数
         *  1- 队列名称
         *  2- 消费成功后是否自动应答
         *  3- 消费消息回调函数
         *  4- 消息失败回调函数
         * */

        System.out.println("我是第2个");

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
