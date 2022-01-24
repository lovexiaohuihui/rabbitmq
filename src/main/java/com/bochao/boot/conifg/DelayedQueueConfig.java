package com.bochao.boot.conifg;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于插件的延迟队列配置类
 * @author 吴军杰
 * */
@Configuration
public class DelayedQueueConfig {

    // 交换机名称
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    // 队列名称
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    // routingKey
    public static final String DELAYED_ROUTINGKEY = "delayed.routingKey";

    // 声明交换机
    @Bean
    public CustomExchange delayedExchange() {
        /**
         * 参数
         * 1- 交换机名称
         * 2- 交换机类型
         * 3- 是否持久化
         * 4- 是否自动删除
         * 5- 其他参数
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    // 声明队列
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    // 绑定交换机与队列
    @Bean
    public Binding binding(@Qualifier("delayedExchange") CustomExchange delayedExchange, @Qualifier("delayedQueue") Queue delayedQueue) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTINGKEY).noargs();
    }
}
