package org.com.statemachinedemo.controller;

import jakarta.annotation.Resource;
import org.com.statemachinedemo.entity.Order;
import org.com.statemachinedemo.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Resource
    private OrderService orderService;

    @GetMapping("/create")
    public Order create(BigDecimal price) {
        return orderService.createOrder(price);
    }

    @GetMapping("/pay/{orderId}")
    public void pay(@PathVariable("orderId") String orderId, BigDecimal price) {
        orderService.processPayment(orderService.getOrder(orderId), price);
    }

    @GetMapping("/get/{orderId}")
    public Order status(@PathVariable("orderId") String orderId) {
        return orderService.getOrder(orderId);
    }
}