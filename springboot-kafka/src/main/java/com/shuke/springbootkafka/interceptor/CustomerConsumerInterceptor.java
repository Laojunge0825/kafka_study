package com.shuke.springbootkafka.interceptor;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;

import java.util.Map;

/**
 * 消费者拦截器
 */
public class CustomerConsumerInterceptor implements ConsumerInterceptor<String,String> {

    /**
     * 在消息被消费之前被调用
     * 可以对消息进行修改、添加自定义的消息头、记录日志等操作
     */
    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> consumerRecords) {
        System.out.println("消费者拦截器 -- 拦截信息：" + consumerRecords);
        return consumerRecords;
    }

    /**
     * 消费者提交偏移量之后被调用
     * 可以在提交偏移量之前进行一些额外的处理，比如记录日志、更新数据库等操作
     */
    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {
        System.out.println("提交了" + map.size() + "个消息");
        System.out.println("提交的消息：" + map);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
