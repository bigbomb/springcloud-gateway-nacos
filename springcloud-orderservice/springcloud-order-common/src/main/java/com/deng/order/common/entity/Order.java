package com.deng.order.common.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    /**
     * 订单id
     */
    private long orderId;
    /**
     * 订单号
     */
    private String orderNum;
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
}
