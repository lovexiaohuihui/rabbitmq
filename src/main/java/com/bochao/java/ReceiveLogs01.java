package com.bochao.java;

import com.bochao.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 消费者 交换机 发布订阅模式 fanout
 * @author 吴军杰
 * */
public class ReceiveLogs01 {

    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        // 1- 获取信道
        Channel channel = RabbitMqUtils.getChannel();
        // 2- 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 3- 声明一个队列 临时队列 当消费者断开与队列连接的时候 队列就会自动删除
        String queueName = channel.queueDeclare().getQueue();
        // 4- 绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "111");
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
