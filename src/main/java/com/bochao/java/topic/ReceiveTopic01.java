package com.bochao.java.topic;

import com.bochao.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 消费者 交换机 主题模式 topic
 * @author 吴军杰
 * */
public class ReceiveTopic01 {

    // 交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        // 1- 获取信道
        Channel channel = RabbitMqUtils.getChannel();
        // 2- 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 3- 声明一个队列
        String queueName = "Q1";
        channel.queueDeclare(queueName, false, false, false, null);
        // 4- 绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
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
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
