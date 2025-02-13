package com.shuke.springbootkafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消费者 分区器
 */
@Component
public class EventConsumerPartition {

    /**
     * concurrency = "3" 指定消费者数量
     * 表示该方法将被并发执行，并发数量为 3。
     *
     */
    @KafkaListener(topics = {"myTopic04"}, groupId = "myGroup05",concurrency = "3",
            containerFactory = "CustomerKafkaListenerContainerFactory")
    public void onEvent(ConsumerRecord<String, String> record) {

            System.out.println(Thread.currentThread().getId()+"   --- 消费消息：" + record);

    }
}
