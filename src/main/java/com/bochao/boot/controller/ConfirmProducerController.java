package com.bochao.boot.controller;

import com.bochao.boot.conifg.ConfirmQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发布确认消息发送者
 * @author 吴军杰
 * */
@RestController
@RequestMapping("/confirm")
@Slf4j
public class ConfirmProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send/{message}")
    public void send(@PathVariable("message") String message) {
        CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmQueueConfig.CONFIRM_EXCHANGE_NAME, ConfirmQueueConfig.CONFIRM_ROUTINGKEY, message, correlationData1);
        log.info("发送的消息:{}", message);
        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmQueueConfig.CONFIRM_EXCHANGE_NAME, ConfirmQueueConfig.CONFIRM_ROUTINGKEY + "22", message, correlationData2);
        log.info("发送的消息:{}", message);
    }
}
