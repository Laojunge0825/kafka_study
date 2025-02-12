package com.shuke.springbootkafka.producer;

import com.shuke.springbootkafka.model.User;
import jakarta.annotation.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 生产者 拦截器
 */
@Component
public class EventProducerInterceptor {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void onSentEvent() {
        User user = new User(1,"张三",100);
        kafkaTemplate.send("interceptorTopic", user);
    }
}
