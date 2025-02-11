package com.shuke.springbootkafka.config;

import cn.hutool.core.util.ObjUtil;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CustomerProducerInterceptor implements ProducerInterceptor<String, Object> {

    /**
     *  在消息发送之前被调用
     *  可以对消息进行修改、添加自定义的消息头、记录日志等操作
     * @return
     */
    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> producerRecord) {
        System.out.println("拦截器 onSend 方法被调用");
        System.out.println("拦截信息："+producerRecord.toString());
        return producerRecord;
    }

    /**
     * 服务器收到消息后的确认
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
