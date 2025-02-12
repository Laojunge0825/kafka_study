package com.shuke.springbootkafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消费者  拦截器
 */
@Component
public class EventConsumerInterceptor {

    /**
     * containerFactory = "CustomerKafkaListenerContainerFactory"
     * 表示使用自定义的 KafkaListenerContainerFactory 配置来创建 KafkaListenerContainer。
     *
     */
    @KafkaListener(topics = {"interceptorTopic"} , groupId = "interceptorGroup" ,
            containerFactory = "CustomerKafkaListenerContainerFactory")
    public void onEventBatch(List<ConsumerRecord<String, String>> records) {
        System.out.println("拦截器--消费的消息:records = " + records);
    }
}
