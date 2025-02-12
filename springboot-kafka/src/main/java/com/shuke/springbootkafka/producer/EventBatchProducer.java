package com.shuke.springbootkafka.producer;

import com.shuke.springbootkafka.model.User;
import jakarta.annotation.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventBatchProducer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(){
        for (int i = 0; i < 125; i++) {
            User user = new User(); // 模拟用户对象
            user.setId(i);
            user.setName("用户：" + i);
            user.setAge(i);
            kafkaTemplate.send("batchTopic","key"+i, "hello" + user.getName());
        }
    }
}
