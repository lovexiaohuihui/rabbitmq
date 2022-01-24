package com.bochao.java;

import com.bochao.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认
 * */
public class ProducerConfirm {

    public static final int SEND_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 1- 单个发布确认
        sendConfirmOne();
        // 2- 批量发布确认
        sendConfirmBatch();
        // 3- 异步发布确认
        sendConfirmAsync();
    }

    // 单个发布确认
    public static void sendConfirmOne() throws Exception {
        // 获取管道
        Channel channel = RabbitMqUtils.getChannel();
        // 声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();
        // 批量发消息
        for (int i = 0; i < SEND_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 单个消息发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                // System.out.println("发布成功");
            }
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("单个消息发布确认发布" + SEND_COUNT + "条用时：" + (end - begin) + "ms");
    }

    // 批量发布确认
    public static void sendConfirmBatch() throws Exception {
        // 获取管道
        Channel channel = RabbitMqUtils.getChannel();
        // 声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();
        // 批量发消息
        // 每多少条确认一次
        int batch = 100;
        for (int i = 0; i < SEND_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 批量发布确认
            if (i % batch == 0) {
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    // System.out.println("发布成功");
                }
            }
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量消息发布确认发布" + SEND_COUNT + "条用时：" + (end - begin) + "ms");
    }

    // 异步发布确认
    public static void sendConfirmAsync() throws Exception {
        // 获取管道
        Channel channel = RabbitMqUtils.getChannel();
        // 声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 线程安全有序的一个哈希表，适用于高并发的情况下
        ConcurrentSkipListMap<Long, String> outStandingConfirm = new ConcurrentSkipListMap<>();
        // 设置监听，消息发布成功失败回调
        // 成功监听
        /**
         * 参数
         * 1- 消息的标记
         * 2- 是否为批量确认
         * */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            // 消息发送成功，删除消息
            // 批量
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outStandingConfirm.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outStandingConfirm.remove(deliveryTag);
            }
        };
        // 失败监听
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = outStandingConfirm.get(deliveryTag);
            System.out.println("发布失败" + message);
        };
        channel.addConfirmListener(ackCallback, nackCallback);
        // 开始时间
        long begin = System.currentTimeMillis();
        // 批量发消息
        for (int i = 0; i < SEND_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 记录所有的消息
            outStandingConfirm.put(channel.getNextPublishSeqNo(), message);
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量消息发布确认发布" + SEND_COUNT + "条用时：" + (end - begin) + "ms");
    }
}
