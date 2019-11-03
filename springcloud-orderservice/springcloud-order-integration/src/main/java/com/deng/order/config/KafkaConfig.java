package com.deng.order.config;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * kafka配置
 */
@Configuration
public class KafkaConfig implements InitializingBean {

    @Value("${kafka.servers}")
    public String brokerList;

    public static final String topic = "test_topic";

    public final String groupId = "group.01";

    public Properties customerConfigs() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);//自动位移提交
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);//自动位移提交间隔时间
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);//消费组失效超时时间
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");//位移丢失和位移越界后的恢复起始位置
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());

        return props;
    }

    public Properties producerConfigs() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 20000000);//20M 消息缓存
        //生产者空间不足时，send()被阻塞的时间，默认60s
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 6000);
        //生产者重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        //指定ProducerBatch（消息累加器中BufferPool中的）可复用大小
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        //生产者会在ProducerBatch被填满或者等待超过LINGER_MS_CONFIG时发送
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "produce_01");
        return props;
    }


    @Bean
    public Producer<String, Object> getKafkaProducer() {
        //KafkaProducer是线程安全的，可以在多个线程中共享单个实例
        return new KafkaProducer<String, Object>(producerConfigs());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}

