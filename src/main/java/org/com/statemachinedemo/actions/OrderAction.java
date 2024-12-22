package org.com.statemachinedemo.actions;

import org.com.statemachinedemo.enums.OrderState;
import org.com.statemachinedemo.enums.OrderEvent;
import org.com.statemachinedemo.entity.Order;
import org.com.statemachinedemo.enums.PaymentStatus;
import org.com.statemachinedemo.listener.OrderTransitionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

@Component
@WithStateMachine
public class OrderAction {

    private static final Logger logger = LoggerFactory.getLogger(OrderAction.class);
    // 支付成功处理
    @OnTransition(source = "CREATED", target = "PAID")
    public void handlePaymentConfirmation(StateContext<OrderState, OrderEvent> context) {
        Order order = (Order) context.getMessage().getHeaders().get("order");
        if (order != null) {
            order.setPaymentStatus(PaymentStatus.SUCCESS);
        }
        logger.info("订单-{} 支付确认成功，订单已支付。", order.getOrderId());
    }

    // 发货处理
    @OnTransition(source = "PAID", target = "SHIPPED")
    public void shipOrder(StateContext<OrderState, OrderEvent> context) {
        Order order = (Order) context.getMessage().getHeaders().get("order");
        if (order != null) {
            order.setState(OrderState.SHIPPED);
        }
        logger.info("订单-{} 已发货，库存已减少，发货流程已启动。", order.getOrderId());
    }

    // 配送完成处理
    @OnTransition(source = "SHIPPED", target = "DELIVERED")
    public void markDeliveryCompleted(StateContext<OrderState, OrderEvent> context) {
        Order order = (Order) context.getMessage().getHeaders().get("order");
        if (order != null) {
            order.setState(OrderState.DELIVERED);
        }
        logger.info("订单-{} 配送已完成，订单已送达。", order.getOrderId());
    }

    // 订单取消处理
    @OnTransition(source = "CREATED", target = "CANCELLED")
    public void cancelOrder(StateContext<OrderState, OrderEvent> context) {
        Order order = (Order) context.getMessage().getHeaders().get("order");
        if (order != null) {
            order.setState(OrderState.CANCELLED);
        }
        logger.info("订单-{} 已取消，退款流程已启动。", order.getOrderId());
    }
}