package com.bochao.boot.consumer;

import com.bochao.boot.conifg.ConfirmQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 报警队列消费者
 * */
@Component
@Slf4j
public class WarningComsumer {

    @RabbitListener(queues = ConfirmQueueConfig.WARNING_QUEUE_NAME)
    public void warningQueueConsumer(Message message) {
        String msg = new String(message.getBody());
        log.info("报警队列消费者:{}", msg);
    }
}
