package com.cpucode.kafka.interceptor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 生产者 - 拦截器
 *
 * @author : cpucode
 * @date : 2021/11/30 19:23
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public class KafkaProducerInterceptor {
    public static void main(String[] args) {
        //0. 创建配置对象
        Properties props= new Properties();

        //kafka集群，broker-list
        props.put("bootstrap.servers", "cpucode100:9092");
        // ack的级别
        props.put("acks", "all");
        //重试次数
        props.put("retries", 3);
        //批次大小
        props.put("batch.size", 16384);
        //等待时间
        props.put("linger.ms", 1);
        //RecordAccumulator缓冲区大小
        props.put("buffer.memory", 33554432);  //32M

        // kv的序列化器
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 配置拦截器
        List<String> interceptors = new ArrayList<String>();
        interceptors.add("com.cpucode.kafka.interceptor.TimeStampInterceptor");
        interceptors.add("com.cpucode.kafka.interceptor.CountInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);

        //1. 创建生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);

        //2. 生产数据
        for (int i = 0; i < 20 ; i++) {
            kafkaProducer.send(new ProducerRecord<String,String>("cpu", "cpuCode" + i));
        }

        //3. 关闭对象
        kafkaProducer.close();
    }
}
