package com.bochao.java;

import com.bochao.util.RabbitMqUtils;
import com.bochao.util.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 消费者 队列模式 手动应答模式
 * @author 吴军杰
 * */
public class ConsumerNack01 {

    // 队列名称
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        // 4- 获取信道
        Channel channel = RabbitMqUtils.getChannel();
        // 5- 消费消息
        // 消息消息回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // 睡十秒
            SleepUtils.threadSleep(20000);
            // 手动应答 1 消息标记tag 2 是否批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
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

        System.out.println("我是第1个");
        // 设置不公平分发 默认 0 轮询
        // int prefetchCount = 1;
        // 预取值
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        // 手动应答
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
