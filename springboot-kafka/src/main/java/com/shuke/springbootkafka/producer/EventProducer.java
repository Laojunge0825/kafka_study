package com.shuke.springbootkafka.producer;

import jakarta.annotation.Resource;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class EventProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    public void send2(String topic, String message) {
        // 通过构建器模式创建 msg 对象
        Message<String> msg = MessageBuilder.withPayload(message)
                // 在KafkaHeader 中设置topic的名字
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        kafkaTemplate.send(msg);
    }

    public void send3(String topic, String message) {

        //Headers 里面存取信息(key-value 键值对)，消费者接收到消息后，可以拿到header里面的信息
        Headers headers = new RecordHeaders();
        headers.add("name","zhang-san".getBytes(StandardCharsets.UTF_8));
        headers.add("orderID","OD123456789".getBytes(StandardCharsets.UTF_8));
        ProducerRecord<String,String> record = new ProducerRecord<>(
                topic,
                0,
                System.currentTimeMillis(),
                "k1",
                message,
                headers
        );
        kafkaTemplate.send(record);
    }

    public void send4(String topic, String message) {
        kafkaTemplate.send(topic, 0, System.currentTimeMillis(), "k2", message);
    }

    /**
        sendDefault 在配置文件里设置好默认主题
     */
    public void sendDefault(String message){
        kafkaTemplate.sendDefault(0, System.currentTimeMillis(), "k2", message);
    }
}
