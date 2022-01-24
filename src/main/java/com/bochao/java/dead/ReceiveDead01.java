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
public class ReceiveDead01 {

    // 普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机名称
    public static final String DEAD_EXCHANGE= "dead_exchange";
    // 普通交换机队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        // 1- 获取信道
        Channel channel = RabbitMqUtils.getChannel();
        // 2- 声明普通交换级
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 3- 声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 3- 声明普通队列
        // 普通交换机参数
        Map<String, Object> arguments = new HashMap<>();
        // 设置超时时间，超时则进入死信
        // arguments.put("x-message-ttl", 10000);
        // 设置队列最大长度，超过的进入死信队列
        // arguments.put("x-max-length", 6);
        // 正常队列死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        // 4- 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        // 5- 绑定普通交换机与队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        // 6- 绑定死信交换机与队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        System.out.println("01号消费者等待消息..................");
        // 7- 消息消费
        // 消息消息回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            if (msg.equals("info5")) {
                channel.basicNack(message.getEnvelope().getDeliveryTag(), false, false);
                System.out.println("拒绝的消息：" + new String(message.getBody()));
            } else {
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                System.out.println("接收到的消息：" + new String(message.getBody()));
            }
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
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, cancelCallback);
    }
}
