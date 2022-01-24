package com.bochao.boot.consumer;

import com.bochao.boot.conifg.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 基于插件的延迟队列监听消费者
 * */
@Slf4j
@Component
public class DelayedQueueConsumer {

    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void delayedQueueConsumer(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到消息：{}", new Date().toString(), msg);
    }
}
