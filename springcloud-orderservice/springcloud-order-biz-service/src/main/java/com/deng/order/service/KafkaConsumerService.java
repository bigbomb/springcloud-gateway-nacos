package com.deng.order.service;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deng.order.config.KafkaConfig;

@Component
public class KafkaConsumerService implements InitializingBean {
	private static final String TOPIC = "log_group";
    @Autowired
    private KafkaConfig kafka_config;

    @Override
    public void afterPropertiesSet() throws Exception {
        //每个线程一个KafkaConsumer实例，且线程数设置成分区数，最大化提高消费能力
        int consumerThreadNum = 2;//线程数设置成分区数，最大化提高消费能力
        for (int i = 0; i < consumerThreadNum; i++) {
            new KafkaConsumerThread(kafka_config.customerConfigs(), TOPIC).start();
        }
    }

    public class KafkaConsumerThread extends Thread {
        private KafkaConsumer<String, String> kafkaConsumer;

        public KafkaConsumerThread(Properties props, String topic) {
            this.kafkaConsumer = new KafkaConsumer<>(props);
            this.kafkaConsumer.subscribe(Arrays.asList(topic));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    ConsumerRecords<String, String> records =
                            kafkaConsumer.poll(100L);
                    for (ConsumerRecord<String, String> record : records) {
                        System.out.println("message------------ "+record.value());
                    }
                }
            } catch (Exception e) {
            } finally {
                kafkaConsumer.close();
            }
        }
    }
}

