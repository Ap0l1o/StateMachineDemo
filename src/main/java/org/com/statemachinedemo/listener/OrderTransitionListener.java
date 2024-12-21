package org.com.statemachinedemo.listener;

import org.com.statemachinedemo.enums.OrderEvent;
import org.com.statemachinedemo.enums.OrderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class OrderTransitionListener extends StateMachineListenerAdapter<OrderState, OrderEvent> {
    private static final Logger logger = LoggerFactory.getLogger(OrderTransitionListener.class);

    @Override
    public void transition(Transition<OrderState, OrderEvent> transition) {
        logger.info("订单状态从 {} 转换到 {}，触发事件：{}", transition.getSource(), transition.getTarget(), transition.getTrigger());
    }

    @Override
    public void transitionStarted(Transition<OrderState, OrderEvent> transition) {
        logger.info("订单状态转换开始：从 {} 到 {}，触发事件：{}", transition.getSource(), transition.getTarget(), transition.getTrigger());
    }

    @Override
    public void transitionEnded(Transition<OrderState, OrderEvent> transition) {
        logger.info("订单状态转换结束：从 {} 到 {}，触发事件：{}", transition.getSource(), transition.getTarget(), transition.getTrigger());
    }
}