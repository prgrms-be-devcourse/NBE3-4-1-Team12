package com.ll.coffeeBean.domain.siteUser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ll.coffeeBean.domain.order.dto.GetResPastOrderDto;
import com.ll.coffeeBean.domain.order.entity.PastOrder;
import com.ll.coffeeBean.domain.order.service.OrderService;
import com.ll.coffeeBean.domain.order.service.PastOrderService;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.SiteUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SiteUserServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PastOrderService pastOrderService;

    @Autowired
    private SiteUserService siteUserService;

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Test
    @DisplayName("회원 별 지난 주문 조회 확인")
    void testPastOrderHistory() {
        assertEquals(siteUserRepository.count(), 1);
        SiteUser siteUser = siteUserService.findByEmail("user1@naver.com").get();

        orderService.processOrderByScheduling();

        GetResPastOrderDto dto = pastOrderService.getList(siteUser, 1, 10).getItems().getFirst();
        PastOrder userPastOrder = siteUser.getPastOrders().getFirst();

        assertEquals(dto.getEmail(), siteUser.getEmail());
        assertEquals(dto.getOrders().size(), userPastOrder.getOrders().size());
        assertEquals(dto.getOrders().getFirst().getName(), userPastOrder.getOrders().getFirst().getName());
        assertEquals(dto.getOrders().getLast().getName(), userPastOrder.getOrders().getLast().getName());
    }
}