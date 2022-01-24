package com.bochao.boot.consumer;

import com.bochao.boot.conifg.ConfirmQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 发布确认消费者监听
 * */
@Component
@Slf4j
public class ConfirmComsumer {

    @RabbitListener(queues = ConfirmQueueConfig.CONFIRM_QUEUE_NAME)
    public void consumer(Message message) {
        String msg = new String(message.getBody());
        log.info("接收到的消息：{}", msg);
    }
}
