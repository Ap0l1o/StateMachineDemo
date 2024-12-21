package org.com.statemachinedemo;

import org.com.statemachinedemo.entity.Order;
import org.com.statemachinedemo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class StateMachineTest {
    @Autowired
    private OrderService orderService;

    @Test
    void testOrderProcess() {
        // 创建订单
        Order order = orderService.createOrder(new BigDecimal("100"));

        // 支付成功
        orderService.processPayment(order, new BigDecimal("100"));

//        // 安排发货
//        orderService.arrangeShipment(order);
//
//        // 完成配送
//        orderService.completeDelivery(order);
//
//        // 取消订单（模拟异常情况）
//        orderService.cancelOrder(order);
//
        // 停止状态机
        orderService.stopStateMachine();
    }

//    @Test
//    void testPaymentFailure() {
//        // 创建订单
//        Order order = orderService.createOrder(new BigDecimal("100"));
//
//        // 支付失败
//        orderService.processPayment(order, new BigDecimal("50"));
//        assertEquals(OrderState.PAYMENT_PENDING, orderService.getCurrentOrderState(order));
//
//        // 停止状态机
//        orderService.stopStateMachine();
//    }
//
//    @Test
//    void testCancelAfterPayment() {
//        // 创建订单
//        Order order = orderService.createOrder(new BigDecimal("100"));
//
//        // 支付成功
//        orderService.processPayment(order, new BigDecimal("100"));
//        assertEquals(OrderState.PAID, orderService.getCurrentOrderState(order));
//
//        // 取消订单
//        orderService.cancelOrder(order);
//        assertEquals(OrderState.CANCELLED, orderService.getCurrentOrderState(order));
//
//        // 停止状态机
//        orderService.stopStateMachine();
//    }

}
