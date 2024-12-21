package org.com.statemachinedemo.actions;

import org.com.statemachinedemo.enums.OrderState;
import org.com.statemachinedemo.enums.OrderEvent;
import org.com.statemachinedemo.entity.Order;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

@Component
@WithStateMachine
public class OrderAction {
//    @Autowired
//    private InventoryService inventoryService;
//    @Autowired
//    private ShippingService shippingService;
//    @Autowired
//    private PaymentService paymentService;

    // 支付成功处理
    @OnTransition(source = "PAYMENT_PENDING", target = "PAID")
    public void handlePaymentConfirmation(StateContext<OrderState, OrderEvent> context) {
        Order order = (Order) context.getMessage().getHeaders().get("order");
        System.out.println("支付确认成功，订单已支付。");
    }

    // 发货处理
    @OnTransition(source = "PAID", target = "SHIPPED")
    public void shipOrder(StateContext<OrderState, OrderEvent> context) {
        Order order = (Order) context.getMessage().getHeaders().get("order");
//        inventoryService.reduceStock(order.getOrderId());
//        shippingService.shipOrder(order.getOrderId());
        System.out.println("订单已发货，库存已减少，发货流程已启动。");
    }

    // 配送完成处理
    @OnTransition(source = "SHIPPED", target = "DELIVERED")
    public void markDeliveryCompleted(StateContext<OrderState, OrderEvent> context) {
        Order order = (Order) context.getMessage().getHeaders().get("order");
        System.out.println("配送已完成，订单已送达。");
    }

    // 订单取消处理
    @OnTransition(source = "CREATED", target = "CANCELLED")
    public void cancelOrder(StateContext<OrderState, OrderEvent> context) {
        Order order = (Order) context.getMessage().getHeaders().get("order");
//        paymentService.refund(order.getOrderId());
        System.out.println("订单已取消，退款流程已启动。");
    }
}