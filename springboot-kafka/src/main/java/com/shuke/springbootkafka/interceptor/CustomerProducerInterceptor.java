package com.shuke.springbootkafka.interceptor;

import cn.hutool.core.util.ObjUtil;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerInterceptor;

import java.util.Map;

/**
 * 生产者拦截器
 */
public class CustomerProducerInterceptor implements ProducerInterceptor<String, Object> {

    /**
     *  在消息发送之前被调用
     *  可以对消息进行修改、添加自定义的消息头、记录日志等操作
     */
    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> producerRecord) {
        System.out.println("生产者拦截器  -- 拦截信息："+producerRecord.toString());
        return producerRecord;
    }

    /**
     *  在消息发送成功或失败时被调用
     *  可以在消息发送成功或失败时进行一些额外的处理，比如记录日志、更新数据库等操作
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if(ObjUtil.isNotEmpty(recordMetadata)){
            System.out.println("服务器 收到该消息:"+recordMetadata.offset());
        } else {
            System.out.println("服务器 未收到该消息" + e.getMessage());
        }

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
