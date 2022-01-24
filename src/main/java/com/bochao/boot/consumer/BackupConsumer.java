package com.bochao.boot.consumer;

import com.bochao.boot.conifg.ConfirmQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 备份交换机消费者
 * */
@Component
@Slf4j
public class BackupConsumer {

    @RabbitListener(queues = ConfirmQueueConfig.BACKUP_QUEUE_NAME)
    public void backupQueueConsumer(Message message) {
        String msg = new String(message.getBody());
        log.info("备份交换收到的消息：{}", msg);
    }
}
