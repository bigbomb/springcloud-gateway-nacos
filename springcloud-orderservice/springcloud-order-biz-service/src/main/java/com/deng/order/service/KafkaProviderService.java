package com.deng.order.service;

import java.time.LocalDateTime;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.deng.order.common.entity.Order;



@ConditionalOnProperty(prefix = "kafka",name = "enable", havingValue = "true")
@Component
public class KafkaProviderService {

    /**
     * 消息 TOPIC
     */
	
    private static final String TOPIC = "test_topic";
    
    @Autowired
    private Producer<String, Object> producer;
    public void sendMessage(long orderId, String orderNum, LocalDateTime createTime) {
        // 构建一个订单类
        Order order = Order.builder()
                .orderId(orderId)
                .orderNum(orderNum)
                .createTime(createTime)
                .build();

        
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC, JSONObject.toJSONString(order));
        try {
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println(metadata.partition() + ":" + metadata.offset());
                    }
                }
            });
        } catch (Exception e) {
        }
    }


}


