package com.shuke.springbootkafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventBatchComsumer {

    @KafkaListener(topics = {"batchTopic"} , groupId = "batchGroup")
    public void onEventBatch(List<ConsumerRecord<String, String>> records) {
        System.out.println("批量消费消息:records.size() = " + records.size() + ",records = " + records);
    }
}
