package org.com.statemachinedemo.config;

import org.com.statemachinedemo.enums.OrderEvent;
import org.com.statemachinedemo.enums.OrderState;
import org.com.statemachinedemo.listener.OrderTransitionListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> {
    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .beanFactory(beanFactory)
                .listener(new OrderTransitionListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderState.CREATED)
                .states(EnumSet.allOf(OrderState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderState.CREATED)
                .target(OrderState.PAID)
                .event(OrderEvent.PAYMENT_CONFIRMED)
                .and()
                .withExternal()
                .source(OrderState.PAID)
                .target(OrderState.SHIPPED)
                .event(OrderEvent.SHIPMENT_ARRANGED)
                .and()
                .withExternal()
                .source(OrderState.SHIPPED)
                .target(OrderState.DELIVERED)
                .event(OrderEvent.DELIVERY_COMPLETED)
                .and()
                .withExternal()
                .source(OrderState.CREATED)
                .target(OrderState.CANCELLED)
                .event(OrderEvent.ORDER_CANCELLED);
    }

}