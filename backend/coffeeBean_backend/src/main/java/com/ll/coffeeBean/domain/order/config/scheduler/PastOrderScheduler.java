package com.ll.coffeeBean.domain.order.config.scheduler;

import com.ll.coffeeBean.domain.order.service.PastOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PastOrderScheduler {

    private final PastOrderService pastOrderService;

    /**
     * 3시간 간격으로 3달 전까지의 PastOrder DB 삭제
     */
    @Scheduled(cron = "0 0 */3 * * *")
    public void processPastOrderByScheduling() {
        pastOrderService.processPastOrderByScheduling();
    }
}
