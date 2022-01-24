package com.bochao.boot.conifg;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布确认 配置类
 * @author 吴军杰
 * */
@Configuration
public class ConfirmQueueConfig {

    // 交换机名称
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    // 队列名称
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    // routingKey
    public static final String CONFIRM_ROUTINGKEY = "confirm.routingKey";
    // 备份交换机名称
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    // 备份队列名称
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    // 报警队列名称
    public static final String WARNING_QUEUE_NAME = "warning_queue";

    // 声明交换机
    @Bean
    public DirectExchange confirmExchange() {
        // 消息无法正常发送转发到备份交换机
        return (DirectExchange) ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME)
                .build();
    }

    // 声明队列
    @Bean
    public Queue confirmQueue() {
        return new Queue(CONFIRM_QUEUE_NAME);
    }

    // 绑定
    @Bean
    public Binding confirmQueueBindingConfirmExchange(@Qualifier("confirmExchange") DirectExchange confirmExchange, @Qualifier("confirmQueue") Queue confirmQueue) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTINGKEY);
    }

    // 声明备份交换机
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    // 声明备份队列
    @Bean
    public Queue backupQueue() {
        return new Queue(BACKUP_QUEUE_NAME);
    }

    // 声明报警队列
    @Bean
    public Queue warningQueue() {
        return new Queue(WARNING_QUEUE_NAME);
    }

    // 备份队列与备份交换机绑定
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue backupQueue, @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    // 报警队列与备份交换机绑定
    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue warningQueue, @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
