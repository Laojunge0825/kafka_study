package com.shuke.springbootkafka.config;

import cn.hutool.core.util.ObjectUtil;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.concurrent.atomic.AtomicInteger;

import java.util.List;
import java.util.Map;

/**
 * 自定义生产者分区器
 */
public class CustomerProducerPartitioner implements Partitioner {

    private AtomicInteger nextPartition = new AtomicInteger(0);


    /**
     * 自定义分区策略  轮询策略
     * cluster 集群信息
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 获取主题的所有分区
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        // 当前主题的有效分区个数
        int numPartitions = partitions.size();

        if (ObjectUtil.isNull(key)){
            ///轮询
            //取下一个值
            int next = nextPartition.getAndIncrement();
            if(next >= numPartitions){
                // 使用 compareAndSet 确保只有一个线程能够成功地将 next 设置为 0
                nextPartition.compareAndSet(next, 0);
            }
            // 这里分区ID 不能直接返回next  要和分区数取余
            System.out.println("分区ID：" + next % numPartitions);
            return next % numPartitions;
        }else {
            // 如果消息的key不为空 ，则使用默认的分区策略
            return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
        }

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
