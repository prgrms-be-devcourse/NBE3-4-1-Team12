package com.ll.coffeeBean.domain.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    void test_delete(){
        assertEquals(orderRepository.count(), 0);
        assertEquals(detailOrderRepository.count(), 0);

        SiteUser customer = SiteUser.builder()
                .email("123@google.com")
                .address("서울")
                .postCode("1251252")
                .build();

        MenuOrder menuOrder = MenuOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatus.READY_FOR_DELIVERY)
                .build();

<<<<<<< HEAD
//        List<DetailOrder> orders = new ArrayList<>();

        menuOrder.addOrder(DetailOrder.builder()
=======
        siteUserRepository.save(customer);

        List<DetailOrder> orders = new ArrayList<>();

        orders.add(DetailOrder.builder()
>>>>>>> 32b0151 (버그 상태)
                .name("americano")
                .price(1500)
                .quantity(2)
                .order(menuOrder)
                .build());

<<<<<<< HEAD
        menuOrder.addOrder(DetailOrder.builder()
=======
        orders.add(DetailOrder.builder()
>>>>>>> 32b0151 (버그 상태)
                .name("latte")
                .price(3000)
                .quantity(1)
                .order(menuOrder)
                .build());

<<<<<<< HEAD
        menuOrder.addOrder(DetailOrder.builder()
=======
        orders.add(DetailOrder.builder()
>>>>>>> 32b0151 (버그 상태)
                .name("ice tea")
                .price(3000)
                .quantity(3)
                .order(menuOrder)
                .build());

<<<<<<< HEAD
        customer.addMenu(menuOrder);
        siteUserRepository.save(customer);
=======
        menuOrder.setOrders(orders);
>>>>>>> 32b0151 (버그 상태)
        orderRepository.save(menuOrder);

        assertEquals(orderRepository.count(), 1);
        assertEquals(detailOrderRepository.count(), 3);
<<<<<<< HEAD
        orderService.processOrderByScheduled();
        orderService.flush();
=======

//        customer.setMenu(List.of(menuOrder));
        List<SiteUser> os = siteUserRepository.findAll();
        orderService.processOrderByScheduled();
>>>>>>> 32b0151 (버그 상태)

        assertEquals(orderRepository.count(), 0);
        assertEquals(detailOrderRepository.count(), 0);
    }

    // 현재 Test 에서 Scheduled 를 확인하지 못해서 단순 로직만 확인
    // 실제 환경에서는 스케쥴러가 잘 작동하는 것 확인
    // 현재는 아무 CRUD 작업이 진행되지 않아, 임시로 모든 값들을 DB에 저장
    // 추후 CRUD 작업이 이루어진 이후에 더 간단하게 수정 예정
    @Test
    @DisplayName("스케쥴 처리 확인용 샘플 데이터 생성")
    void t1() {

        assertEquals(orderRepository.count(), 0);
        assertEquals(detailOrderRepository.count(), 0);
        SiteUser customer = SiteUser.builder()
                .email("123@google.com")
                .address("서울")
                .postCode("1251252")
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
                .order(menuOrder)
                .build());

        orders.add(DetailOrder.builder()
                .name("latte")
                .price(3000)
                .quantity(1)
                .order(menuOrder)
                .build());

        orders.add(DetailOrder.builder()
                .name("ice tea")
                .price(3000)
                .quantity(3)
                .order(menuOrder)
                .build());

        menuOrder.setOrders(orders);
        customer.setMenu(List.of(menuOrder));
        orderRepository.save(menuOrder);
        siteUserRepository.save(customer);

        MenuOrder menuOrder1 = orderRepository.findById(1L).get();
        DetailOrder detailOrder1 = detailOrderRepository.findById(1L).get();

        assertEquals(orderRepository.count(), 1L);
        assertEquals(menuOrder1.getOrderStatus(), OrderStatus.READY_FOR_DELIVERY);
        assertEquals(menuOrder1.getCustomer().getEmail(), customer.getEmail());
        assertNull(menuOrder1.getCustomer().getPastOrders());
        assertEquals(detailOrderRepository.count(), 3L);
        assertEquals(detailOrder1.getName(), "americano");
        assertEquals(detailOrder1.getOrder().getOrders().size(), 3);
        assertEquals(detailOrder1.getOrder().getOrders().get(2).getName(), "ice tea");
        assertEquals(detailOrder1.getOrder().getCustomer().getEmail(), customer.getEmail());
        assertEquals(detailOrder1.getOrder().getCustomer().getMenu().size(), 1);
        assertEquals(detailOrder1.getOrder().getCustomer().getMenu().get(0).getOrders().size(), 3);
        assertEquals(detailOrder1.getOrder().getCustomer().getMenu().get(0).getOrders().get(0).getName(), "americano");
        assertNull(detailOrder1.getPastOrder());
        assertEquals(pastOrderRepository.count(), 0L);

        orderService.processOrderByScheduled();
//        assertEquals(pastOrderRepository.count(), 1L);
//        assertEquals(pastOrderRepository.findById(1L).get().getOrderStatus(), OrderStatus.DELIVERED);
        assertEquals(orderRepository.count(), 0);
        assertEquals(detailOrderRepository.count(), 0);

//        customer = SiteUser.builder()
//                .email("naver@google.com")
//                .address("울산")
//                .postCode(UUID.randomUUID().toString())
//                .build();
//
//        menuOrder = MenuOrder.builder()
//                .customer(customer)
//                .orderStatus(OrderStatus.READY_FOR_DELIVERY)
//                .build();
//
//        orders = new ArrayList<>();
//
//        orders.add(DetailOrder.builder()
//                .name("americano")
//                .price(1500)
//                .quantity(4)
//                .order(menuOrder)
//                .build());
//
//        orders.add(DetailOrder.builder()
//                .name("ice tea")
//                .price(3000)
//                .quantity(1)
//                .order(menuOrder)
//                .build());
//
//        orders.add(DetailOrder.builder()
//                .name("ice choco")
//                .price(4000)
//                .quantity(4)
//                .order(menuOrder)
//                .build());
//
//        menuOrder.setOrders(orders);
//        orderRepository.save(menuOrder);
//        siteUserRepository.save(customer);
//
//        assertEquals(orderRepository.count(), 1L);
//        assertEquals(orderRepository.findById(2L).get().getOrderStatus(), OrderStatus.READY_FOR_DELIVERY);
//        assertEquals(detailOrderRepository.count(), 3L);
//        assertEquals(pastOrderRepository.count(), 1L);
//
//        orderService.processOrderByScheduled();
//
//        assertEquals(pastOrderRepository.count(), 2L);
//        assertEquals(pastOrderRepository.findById(2L).get().getOrderStatus(), OrderStatus.DELIVERED);
//        assertEquals(orderRepository.count(), 0);
//        assertEquals(detailOrderRepository.count(), 0);
    }
}