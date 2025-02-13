package com.shuke.springbootkafka.config;

import com.shuke.springbootkafka.interceptor.CustomerConsumerInterceptor;
import com.shuke.springbootkafka.interceptor.CustomerProducerInterceptor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /// 值的序列化方式
    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    /// 反序列化
    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    /**
     * 初始化topic  自定义分区和副本因子
     */
    @Bean
    public NewTopic newTopic() {

        return new NewTopic("myTopic04", 10, (short) 1);
    }

    /**
     * 生产者相关配置
     */
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(6);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        /// 指定分区策略  这里时指定自定义的轮询策略
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomerProducerPartitioner.class.getName());

        // 配置生产者拦截器
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomerProducerInterceptor.class.getName());
        return props;
    }

    /**
     * 生产者工厂
     */
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * 消费者相关配置
     */
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(6);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        // 配置消费者拦截器
//        props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomerConsumerInterceptor.class.getName());
        // 指定分区策略  指定轮询的分区策略
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
        return props;
    }
    /**
     * 消费者工厂
     */
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * kafkaTemplate 覆盖默认配置类中的kafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        System.out.println("使用新的KafkaTemplate");
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * 自定义监听容器工厂  使用自定义的消费者工厂
     */
    @Bean
    public KafkaListenerContainerFactory<?> CustomerKafkaListenerContainerFactory() {
        System.out.println("自定义监听器容器工厂");
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
