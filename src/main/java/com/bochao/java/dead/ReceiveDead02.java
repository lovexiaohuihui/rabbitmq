package com.bochao.java.dead;

import com.bochao.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者 死信队列
 * @author 吴军杰
 * */
public class ReceiveDead02 {

    // 死信交换机名称
    public static final String DEAD_EXCHANGE= "dead_exchange";
    // 死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        // 1- 获取信道
        Channel channel = RabbitMqUtils.getChannel();
        // 2- 声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 3- 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        // 4- 绑定死信交换机与队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        System.out.println("01号消费者等待消息..................");
        // 5- 消息消费
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
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);
    }
}
