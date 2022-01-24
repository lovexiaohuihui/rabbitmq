package com.bochao.boot.controller;

import com.bochao.boot.conifg.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 消息生产者
 * @author 吴军杰
 * */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessage {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send/{message}")
    public void sendMeaasge(@PathVariable("message") String message) {
        log.info("当前时间：{},发送消息给两个ttl队列：{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自 ttl 为 10S 的队列: " + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自 ttl 为 40S 的队列: " + message);
    }

    /**
     * ttl 过期时间
     * */
    @GetMapping("/send/{message}/{ttltime}")
    public void sendMeaasge(@PathVariable("message") String message, @PathVariable("ttltime") String ttltime) {
        log.info("当前时间：{},发送消息给动态ttl队列：{},ttl:{}", new Date().toString(), message, ttltime);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            msg.getMessageProperties().setExpiration(ttltime);
            return msg;
        });
    }

    /**
     * 发送基于插件的延迟消息
     * */
    @GetMapping("/sendDelayed/{message}/{delayedTime}")
    public void sendDelayMsg(@PathVariable("message") String message, @PathVariable("delayedTime") Integer delayedTime) {
        log.info("当前时间：{},发送消息给延迟队列：{},delayedTime:{}", new Date().toString(), message, delayedTime);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_ROUTINGKEY, message, msg -> {
            msg.getMessageProperties().setDelay(delayedTime);
            return msg;
        });
    }
}
