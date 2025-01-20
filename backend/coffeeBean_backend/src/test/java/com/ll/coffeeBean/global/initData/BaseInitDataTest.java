package com.ll.coffeeBean.global.initData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.repository.DetailOrderRepository;
import com.ll.coffeeBean.domain.order.repository.OrderRepository;
import com.ll.coffeeBean.domain.order.repository.PastOrderRepository;
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
class BaseInitDataTest {
    @Autowired
    private SiteUserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DetailOrderRepository detailOrderRepository;

    @Autowired
    private PastOrderRepository pastOrderRepository;

    @Test
    @DisplayName("BaseInit 데이터 생성 및 데이터 확인")
    void test_baseInitData() {
        SiteUser user = userRepository.findById(1L).get();
        MenuOrder order = orderRepository.findById(1L).get();
        DetailOrder detailOrder = detailOrderRepository.findById(1L).get();

        assertEquals(user.getEmail(), "user1@naver.com");
        assertEquals(user.getOrders().size(), 1);
        assertEquals(user.getOrders().get(0).getOrders().size(), 3);
        assertEquals(user.getOrders().get(0).getCustomer().getEmail(), order.getCustomer().getEmail());
        assertEquals(user.getOrders().get(0).getOrders().get(0).getName(), detailOrder.getName());
        assertNull(user.getOrders().get(0).getOrders().get(0).getPastOrder());
        assertEquals(order.getCustomer().getEmail(), "user1@naver.com");
        assertEquals(pastOrderRepository.count(), 0);
    }
}