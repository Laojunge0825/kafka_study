package com.shuke.springbootkafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    // 采用监听的方法接收事件
    @KafkaListener(topics = { "shuke01" } , groupId = "shuke-group01")
    public  void onEvent(String event) {
        System.out.println("读取到的事件：" + event);
    }
}
