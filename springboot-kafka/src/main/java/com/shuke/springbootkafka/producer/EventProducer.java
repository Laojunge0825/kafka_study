package com.shuke.springbootkafka.producer;

import cn.hutool.core.util.ObjectUtil;
import com.shuke.springbootkafka.model.User;
import jakarta.annotation.Resource;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class EventProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate2;

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
        ProducerRecord<String,String> record = new ProducerRecord(
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
     * sendDefault 在配置文件里设置好默认主题
     */
    public void sendDefault(String message){
        kafkaTemplate.sendDefault(0, System.currentTimeMillis(), "k2", message);
    }

    /**
     * 同步阻塞拿到返回结果
     */
    public void sendDefault01(String message){
        // 异步返回的对象
        CompletableFuture<SendResult<String, String>> completableFuture
                = kafkaTemplate.sendDefault(0, System.currentTimeMillis(), "k3", message);
        try {
            // 阻塞等待拿到返回结果
            SendResult<String,String> sendResult = completableFuture.get();
            try {
                // 模拟耗时操作
                for (int i= 0 ; i<4;i++){
                    System.out.println("模拟耗时："+i);
                    Thread.sleep(3000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(ObjectUtil.isNotNull(sendResult.getRecordMetadata())){
                // kafka 服务器 已经接受到了信息
                System.out.println("消息发送成功：" + sendResult.getRecordMetadata().topic());
            }
            System.out.println("producerRecord: " + sendResult.getProducerRecord());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 异步回调  使用thenAccept() thenApply() 等注册回调函数
     */
    public void sendDefault02(String message){
        // 异步返回的对象
        CompletableFuture<SendResult<String, String>> completableFuture
                = kafkaTemplate.sendDefault(0, System.currentTimeMillis(), "k3", message);


        try {
            // 非阻塞的方式拿结果
            completableFuture.thenAccept((sendResult) -> {
                try {
                    // 模拟耗时操作
                    for (int i= 0 ; i<4;i++){
                        System.out.println("模拟耗时："+i);
                        Thread.sleep(3000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (ObjectUtil.isNotNull(sendResult.getRecordMetadata())) {
                    System.out.println("消息发送成功：" + sendResult.getRecordMetadata().topic());
                }
                System.out.println("producerRecord: " + sendResult.getProducerRecord());
            }).exceptionally(throwable -> {
                System.out.println("消息发送失败： " + throwable.getMessage());
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送对象
     */
    public void sendObj(){
        User user = User.builder().id(1001).age(10).name("张三").build();
        /// 分区为 null  让Kafka自己决定放到那个分区
        kafkaTemplate2.sendDefault(null,System.currentTimeMillis(),"key",user);
    }
}
