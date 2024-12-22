package org.com.statemachinedemo.service;

import org.com.statemachinedemo.entity.Order;
import org.com.statemachinedemo.entity.Product;
import org.com.statemachinedemo.enums.OrderEvent;
import org.com.statemachinedemo.enums.OrderState;
import org.com.statemachinedemo.enums.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private ProductService productService;
    private final Map<String, Order> orderRepository = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public Order getOrder(String orderId) {
        return orderRepository.get(orderId);
    }

    // 创建订单并初始化状态机
    public Order createOrder(Long productId, Long quantity) {
        Product product = productService.getProductById(productId);
        if (product == null || product.getStock() < quantity) {
            throw new IllegalArgumentException("Product not available or insufficient stock");
        }
        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        Order order = new Order(UUID.randomUUID().toString(), productId, quantity, totalAmount);
        orderRepository.put(order.getOrderId(), order);
        // 启动状态机
        stateMachine.start();

        return order;
    }

    // 处理支付
    public void processPayment(Order order, BigDecimal paymentAmount) {
        order.setPaymentAmount(paymentAmount);
        if (paymentAmount.equals(order.getTotalAmount())) {
            stateMachine.sendEvent(MessageBuilder.withPayload(OrderEvent.PAYMENT_CONFIRMED)
                    .setHeader("order", order)
                    .build());
        } else {
            order.setPaymentStatus(PaymentStatus.FAILED);
            logger.info("订单-{} 支付失败", order.getOrderId());
        }
    }

    // 安排发货
    public void arrangeShipment(Order order) {
        stateMachine.sendEvent(MessageBuilder.withPayload(OrderEvent.SHIPMENT_ARRANGED)
                .setHeader("order", order)
                .build());
    }

    // 完成配送
    public void completeDelivery(Order order) {
        stateMachine.sendEvent(MessageBuilder.withPayload(OrderEvent.DELIVERY_COMPLETED)
                .setHeader("order", order)
                .build());
    }

    // 取消订单
    public void cancelOrder(Order order) {
        stateMachine.sendEvent(MessageBuilder.withPayload(OrderEvent.ORDER_CANCELLED)
                .setHeader("order", order)
                .build());
    }

    // 停止状态机（销毁资源）
    public void stopStateMachine() {
        stateMachine.stop();
    }
}