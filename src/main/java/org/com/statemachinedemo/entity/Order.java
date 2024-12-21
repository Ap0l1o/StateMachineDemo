package org.com.statemachinedemo.entity;

import lombok.Data;
import org.com.statemachinedemo.enums.OrderState;
import org.com.statemachinedemo.enums.PaymentStatus;

import java.math.BigDecimal;

@Data
public class Order {
    private String orderId;
    private BigDecimal totalAmount;
    private BigDecimal paymentAmount;
    private PaymentStatus paymentStatus;
    private OrderState state;
    // 其他订单相关属性，如订单详情、收货地址等

    public Order(String orderId, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.state = OrderState.CREATED;
        this.paymentStatus = PaymentStatus.PENDING;
    }
}