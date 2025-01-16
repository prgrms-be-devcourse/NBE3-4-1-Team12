package com.ll.coffeeBean.domain.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.enums.OrderStatus;
import com.ll.coffeeBean.domain.order.repository.DetailOrderRepository;
import com.ll.coffeeBean.domain.order.repository.OrderRepository;
import com.ll.coffeeBean.domain.order.repository.PastOrderRepository;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.SiteUserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@EnableScheduling
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DetailOrderRepository detailOrderRepository;

    @Autowired
    private PastOrderRepository pastOrderRepository;

    @Autowired
    private SiteUserRepository siteUserRepository;

    // 현재 Test 에서 Scheduled 를 확인하지 못해서 단순 로직만 확인
    // 실제 환경에서는 스케쥴러가 잘 작동하는 것 확인
    // 현재는 아무 CRUD 작업이 진행되지 않아, 임시로 모든 값들을 DB에 저장
    // 추후 CRUD 작업이 이루어진 이후에 더 간단하게 수정 예정
    @Test
    @DisplayName("스케쥴 처리 확인용 샘플 데이터 생성")
    void t1() {
        SiteUser customer = SiteUser.builder()
                .email("123@google.com")
                .address("서울")
                .postCode(UUID.randomUUID().toString())
                .build();

        MenuOrder menuOrder = MenuOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatus.READY_FOR_DELIVERY)
                .build();

        List<DetailOrder> orders = new ArrayList<>();

        orders.add(DetailOrder.builder()
                .name("americano")
                .price(1500)
                .quantity(2)
                .build());

        orders.add(DetailOrder.builder()
                .name("latte")
                .price(3000)
                .quantity(1)
                .build());

        orders.add(DetailOrder.builder()
                .name("ice tea")
                .price(3000)
                .quantity(3)
                .build());

        menuOrder.setOrders(orders);
        orderRepository.save(menuOrder);
        siteUserRepository.save(customer);
        
        orderService.processOrderByScheduled();

        assertEquals(pastOrderRepository.count(), 3L);
        assertEquals(pastOrderRepository.findById(1L).get().getOrderStatus(), OrderStatus.DELIVERED );
        assertEquals(orderRepository.count(), 0);
        assertEquals(detailOrderRepository.count(), 0);

        customer = SiteUser.builder()
                .email("naver@google.com")
                .address("울산")
                .postCode(UUID.randomUUID().toString())
                .build();

        menuOrder = MenuOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatus.READY_FOR_DELIVERY)
                .build();

        orders = new ArrayList<>();

        orders.add(DetailOrder.builder()
                .name("americano")
                .price(1500)
                .quantity(4)
                .build());

        orders.add(DetailOrder.builder()
                .name("ice Tea")
                .price(3000)
                .quantity(1)
                .build());

        orders.add(DetailOrder.builder()
                .name("ice Choco")
                .price(4000)
                .quantity(4)
                .build());

        menuOrder.setOrders(orders);
        orderRepository.save(menuOrder);
        siteUserRepository.save(customer);
        
        orderService.processOrderByScheduled();

        assertEquals(pastOrderRepository.count(), 6);
        assertEquals(pastOrderRepository.findById(4L).get().getOrderStatus(), OrderStatus.DELIVERED);
        assertEquals(orderRepository.count(),0);
        assertEquals(detailOrderRepository.count(),0);
    }
}