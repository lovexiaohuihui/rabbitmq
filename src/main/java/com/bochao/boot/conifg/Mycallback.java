package com.bochao.boot.conifg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 交换机确认回调方法
 * @author 吴军杰
 * */
@Component
@Slf4j
public class Mycallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        // 注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 1 发消息 交换机收到了
     *  1.1 correlationData 保存回调消息的ID以及相关信息
     *  1.2 交换机收到消息 acl = true
     *  1.3 cause null
     * 2 发消息 交换机没有收到  收到失败了
     *  1.1 correlationData 保存回调消息的ID以及相关信息
     *  1.2 交换机未收到消息 ack = false
     *  1.3 cause 失败原因
     * */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机收到id为{}的消息", id);
        } else {
            log.info("交换机没有收到id为{}的消息，原因:{}", id, cause);
        }
    }

    /**
     * 消息无法到达队列消息回退
     * */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("回退的消息:{}, 原因：{}", new String(message.getBody()), replyText);
    }
}
