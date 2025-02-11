package com.shuke.springbootkafka.consumer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.shuke.springbootkafka.model.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    // 采用监听的方法接收事件
//    @KafkaListener(topics = { "shukeTopic" } , groupId = "shukeGroup")
    /*
      @Payload 注解用于从消息负载中提取数据
     * 并将其绑定到方法参数上
     * @Header 注解用于从消息头中提取数据
     */
    public  void onEvent(@Payload String event ,
                         @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(value = KafkaHeaders.RECEIVED_PARTITION) int partition,
                         @Header(value = KafkaHeaders.OFFSET) int offset,
                         ConsumerRecord<String,String> record ,
                         String   userJson) {


        System.out.println("读取到的事件：" + event + ", 主题：" + topic + ", 分区：" + partition + ", 偏移量：" + offset);
        System.out.println("读取到的事件信息："+record.toString() + "/n" + ", 发送消息的时间：" + DateUtil.date(record.timestamp()));
        User user = JSONUtil.toBean(userJson, User.class);
        System.out.println("User对象：" + user.toString());
    }

    @KafkaListener(topics = { "shukeTopic" } , groupId = "shukeGroup")
    public void onEvent2(@Payload String event,
                         @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(value = KafkaHeaders.RECEIVED_PARTITION) int partition,
                         @Header(value = KafkaHeaders.OFFSET) int offset,
                         Acknowledgment ack){

        try {
            System.out.println("读取到的事件：" + event + ", 主题：" + topic + ", 分区：" + partition + ", 偏移量：" + offset);
            // 手动提交   手动确认消息
            ack.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
