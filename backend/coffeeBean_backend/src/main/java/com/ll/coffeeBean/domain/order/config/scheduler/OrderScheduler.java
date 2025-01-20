package com.ll.coffeeBean.domain.order.config.scheduler;

import com.ll.coffeeBean.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderService orderService;

    /**
     * 전날 14시부터 당일 14시까지의 주문 자동 처리 매일 14시에 주문 처리 수행
     */
    @Scheduled(cron = "0 0 14 * * *")
    public void processOrderByScheduling() {
        orderService.processOrderByScheduling();
    }
}
