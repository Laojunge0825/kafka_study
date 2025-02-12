package com.shuke.springbootkafka.producer;

import cn.hutool.json.JSONUtil;
import com.shuke.springbootkafka.model.User;
import jakarta.annotation.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class EventProducerPartition {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void onSend(){
        for (int i = 0; i < 100; i++) {

            kafkaTemplate.send("myTopic04","k: "+i, "消息： "+i);
        }
    }

}
