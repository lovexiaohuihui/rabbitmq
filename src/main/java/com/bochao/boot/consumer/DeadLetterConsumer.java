package com.bochao.boot.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 死信队列消费
 * @author 吴军杰
 * */
@Component
@Slf4j
public class DeadLetterConsumer {

    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到的死信队列消息：{}", new Date().toString(), msg);
    }
}
