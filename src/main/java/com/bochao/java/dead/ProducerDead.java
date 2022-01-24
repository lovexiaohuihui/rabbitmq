package com.bochao.java.dead;

import com.bochao.util.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbitmq 生产者 死信队列
 * @author 吴军杰
 * */
public class ProducerDead {

    // 普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        // 1- 获取信道
        Channel channel = RabbitMqUtils.getChannel();

        // 2- 声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 3- 发送消息
        // 属性 十秒超时 进入死信队列
        // AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 1; i < 11 ; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes("UTF-8"));
            System.out.println("发送的消息:" + message);
        }
    }
}
