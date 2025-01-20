package com.ll.coffeeBean.domain.siteUser.service;

import com.ll.coffeeBean.domain.order.dto.GetResPastOrderDto;
import com.ll.coffeeBean.domain.order.service.OrderService;
import com.ll.coffeeBean.domain.order.service.PastOrderService;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.standard.PageDto.PageDto;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void beforeEach() {
        orderService.processOrderByScheduled();
    }


    @Test
    @DisplayName("회원 별 지난 주문 조회 확인")
    void testPastOrderHistory() {
        SiteUser siteUser = siteUserService.findByEmail("user1@naver.com").get();
        PageDto<GetResPastOrderDto> list = pastOrderService.getList(siteUser, 1, 10);


    }
}