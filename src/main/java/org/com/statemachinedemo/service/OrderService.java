package org.com.statemachinedemo.service;

import org.com.statemachinedemo.entity.Order;
import org.com.statemachinedemo.enums.OrderEvent;
import org.com.statemachinedemo.enums.OrderState;
import org.com.statemachinedemo.enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private StateMachine<OrderState, OrderEvent> stateMachine;
    private final Map<String, Order> orderRepository = new HashMap<>();

    public Order getOrder(String orderId) {
        return orderRepository.get(orderId);
    }

    // 创建订单并初始化状态机
    public Order createOrder(BigDecimal totalAmount) {
        Order order = new Order(UUID.randomUUID().toString(), totalAmount);
        orderRepository.put(order.getOrderId(), order);
        // 启动状态机
        stateMachine.start();

        return order;
    }

    // 处理支付
    public void processPayment(Order order, BigDecimal paymentAmount) {
        order.setPaymentAmount(paymentAmount);
        order.setPaymentStatus(paymentAmount.equals(order.getTotalAmount())? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("order", order);
            stateMachine.sendEvent(MessageBuilder.withPayload(OrderEvent.PAYMENT_CONFIRMED)
                    .setHeader("order", order)
                    .build());
        } else {
            System.out.println("支付失败");
        }
    }

    // 安排发货
    public void arrangeShipment(Order order) {
        stateMachine.sendEvent(OrderEvent.SHIPMENT_ARRANGED);
    }

    // 完成配送
    public void completeDelivery(Order order) {
        stateMachine.sendEvent(OrderEvent.DELIVERY_COMPLETED);
    }

    // 取消订单
    public void cancelOrder(Order order) {
        stateMachine.sendEvent(OrderEvent.ORDER_CANCELLED);
    }

    // 获取订单当前状态
    public OrderState getCurrentOrderState(Order order) {
        return order.getState();
    }

    // 停止状态机（销毁资源）
    public void stopStateMachine() {
        stateMachine.stop();
    }
}