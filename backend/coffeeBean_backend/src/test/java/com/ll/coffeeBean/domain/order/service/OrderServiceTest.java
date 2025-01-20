package com.ll.coffeeBean.domain.order.service;

import com.ll.coffeeBean.domain.order.dto.BeanNameQuantityDTO;
import com.ll.coffeeBean.domain.order.dto.PutMenuOrderRqDTO;
import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.entity.PastOrder;
import com.ll.coffeeBean.domain.order.enums.OrderStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
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
    @DisplayName("데이터 입력 및 삭제, DB 반영 모두 확인하는 테스트")
    void test_insertAndDeleteData() {
        // given firstOrder
        assertEquals(orderRepository.count(), 1);
        assertEquals(detailOrderRepository.count(), 3);

        orderRepository.deleteAll();
        detailOrderRepository.deleteAll();

        assertEquals(orderRepository.count(), 0);
        assertEquals(detailOrderRepository.count(), 0);

        // 사용자
        SiteUser customer = SiteUser.builder()
                .email("123@google.com")
                .address("서울")
                .postCode("1251252")
                .build();

        // 주문
        MenuOrder menuOrder = MenuOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatus.READY_FOR_DELIVERY)
                .build();

        // 상세 주문. menuOrder 의 addOrder 메서드로 menuOrder.orders 에 추가
        menuOrder.addDetail(DetailOrder.builder()
                .name("americano")
                .price(1500)
                .quantity(2)
                .order(menuOrder)
                .build());

        menuOrder.addDetail(DetailOrder.builder()
                .name("latte")
                .price(3000)
                .quantity(1)
                .order(menuOrder)
                .build());

        menuOrder.addDetail(DetailOrder.builder()
                .name("ice tea")
                .price(3000)
                .quantity(3)
                .order(menuOrder)
                .build());

        // customer.menu 에 menuOrder 추가
        customer.addOrder(menuOrder);

        // persist
        siteUserRepository.save(customer);

        MenuOrder order = orderRepository.findById(2L).get();
        DetailOrder detailOrder = detailOrderRepository.findById(4L).get();

        assertEquals(orderRepository.count(), 1L);
        assertEquals(order.getOrderStatus(), OrderStatus.READY_FOR_DELIVERY);
        assertEquals(order.getCustomer().getEmail(), customer.getEmail());
        assertNull(order.getCustomer().getPastOrders());
        assertEquals(detailOrderRepository.count(), 3L);
        assertEquals(detailOrder.getName(), "americano");
        assertEquals(detailOrder.getOrder().getOrders().size(), 3);
        assertEquals(detailOrder.getOrder().getOrders().get(2).getName(), "ice tea");
        assertEquals(detailOrder.getOrder().getCustomer().getEmail(), customer.getEmail());
        assertEquals(detailOrder.getOrder().getCustomer().getOrders().size(), 1);
        assertEquals(detailOrder.getOrder().getCustomer().getOrders().get(0).getOrders().size(), 3);
        assertEquals(detailOrder.getOrder().getCustomer().getOrders().get(0).getOrders().get(0).getName(), "americano");
        assertNull(detailOrder.getPastOrder());
        assertEquals(pastOrderRepository.count(), 0L);

        assertEquals(orderRepository.count(), 1);
        assertEquals(detailOrderRepository.count(), 3);

        // when firstOrder
        orderService.processOrderByScheduled();

        // then firstOrder
        assertEquals(orderRepository.count(), 0);
        assertEquals(detailOrderRepository.count(), 3);
        assertEquals(pastOrderRepository.count(), 1L);

        detailOrder = detailOrderRepository.findById(4L).get();
        PastOrder pastOrder = pastOrderRepository.findById(1L).get();

        assertEquals(detailOrder.getName(), "americano");
        assertEquals(detailOrder.getQuantity(), 2);
        assertEquals(pastOrder.getCustomer().getEmail(), "123@google.com");
        assertEquals(pastOrder.getOrders().size(), 3);
        assertEquals(pastOrder.getOrders().get(0).getName(), "americano");
        assertEquals(pastOrder.getOrderStatus(), OrderStatus.DELIVERED);

        // given secondOrder
        customer = SiteUser.builder()
                .email("naver@google.com")
                .address("울산")
                .postCode(UUID.randomUUID().toString())
                .build();

        menuOrder = MenuOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatus.READY_FOR_DELIVERY)
                .build();

        menuOrder.addDetail(DetailOrder.builder()
                .name("ice choco")
                .price(4000)
                .quantity(4)
                .order(menuOrder)
                .build());

        menuOrder.addDetail(DetailOrder.builder()
                .name("vanilla latte")
                .price(5000)
                .quantity(1)
                .order(menuOrder)
                .build());

        menuOrder.addDetail(DetailOrder.builder()
                .name("ice mocha")
                .price(6000)
                .quantity(4)
                .order(menuOrder)
                .build());

        customer.addOrder(menuOrder);
        siteUserRepository.save(customer);
        orderRepository.save(menuOrder);

        order = orderRepository.findById(3L).get();
        DetailOrder newDetailOrder = detailOrderRepository.findById(7L).get();

        assertEquals(orderRepository.count(), 1L);
        assertEquals(order.getOrderStatus(), OrderStatus.READY_FOR_DELIVERY);
        assertEquals(order.getCustomer().getEmail(), customer.getEmail());
        assertNull(order.getCustomer().getPastOrders());
        assertEquals(detailOrderRepository.count(), 6L);
        assertEquals(newDetailOrder.getName(), "ice choco");
        assertEquals(newDetailOrder.getOrder().getOrders().size(), 3);
        assertEquals(newDetailOrder.getOrder().getOrders().get(2).getName(), "ice mocha");
        assertEquals(newDetailOrder.getOrder().getCustomer().getEmail(), customer.getEmail());
        assertEquals(newDetailOrder.getOrder().getCustomer().getOrders().size(), 1);
        assertEquals(newDetailOrder.getOrder().getCustomer().getOrders().get(0).getOrders().size(), 3);
        assertEquals(newDetailOrder.getOrder().getCustomer().getOrders().get(0).getOrders().get(0).getName(),
                "ice choco");
        assertNull(newDetailOrder.getPastOrder());
        assertEquals(pastOrderRepository.count(), 1L);

        // when secondOrder
        orderService.processOrderByScheduled();

        // then secondOrder
        assertEquals(orderRepository.count(), 0);
        assertEquals(detailOrderRepository.count(), 6);
        assertEquals(pastOrderRepository.count(), 2L);

        newDetailOrder = detailOrderRepository.findById(7L).get();
        pastOrder = pastOrderRepository.findById(2L).get();

        assertEquals(newDetailOrder.getName(), "ice choco");
        assertEquals(newDetailOrder.getPrice(), 4000);
        assertEquals(pastOrder.getCustomer().getEmail(), "naver@google.com");
        assertEquals(pastOrder.getOrders().size(), 3);
        assertEquals(pastOrder.getOrders().get(0).getName(), "ice choco");
        assertEquals(pastOrder.getOrderStatus(), OrderStatus.DELIVERED);
    }

    @Test
    @DisplayName("delete - MenuOrder 삭제 시 DetailOrder도 함께 삭제")
    void test_deleteMenuOrderAliveDetailOrderFailed() {
        // given
        assertEquals(orderRepository.count(), 1L);
        assertEquals(detailOrderRepository.count(), 3L);
        assertEquals(siteUserRepository.count(), 1L);

        // when
        List<MenuOrder> orders = orderRepository.findAllById(1L);

        for (MenuOrder order : orders) {
            order.getCustomer().removeOrder(order);
        }

        SiteUser user = siteUserRepository.findById(1L).get();

        // then
        assertEquals(orderRepository.count(), 0L);
        assertEquals(detailOrderRepository.count(), 0L);
        assertEquals(siteUserRepository.count(), 1L);
        assertEquals(user.getOrders().size(), 0);
    }

    @Test
    @DisplayName("delete - MenuOrder 삭제 시 DetailOrder 는 살아있는지 init 데이터로 확인")
    void test_deleteMenuOrderAliveDetailOrderSuccess() {
        // given
        assertEquals(orderRepository.count(), 1L);
        assertEquals(detailOrderRepository.count(), 3L);
        assertEquals(siteUserRepository.count(), 1L);

        // when
        orderService.processOrderByScheduled();

        // then
        assertEquals(orderRepository.count(), 0L);
        assertEquals(detailOrderRepository.count(), 3L);
        assertEquals(siteUserRepository.count(), 1L);
    }

    @Test
    @DisplayName("modify, delete - 변경 개수 0개")
    void test_modifyAndDeleteDetailOrder() {
        MenuOrder menuOrder = orderRepository.findById(1L).get();
        assertEquals(menuOrder.getCustomer().getEmail(), "user1@naver.com");
        assertEquals(menuOrder.getOrders().get(2).getName(), "bean3");

        List<BeanNameQuantityDTO> beanIdQuantityDTOS = new ArrayList<>();
        BeanNameQuantityDTO beanId1 = new BeanNameQuantityDTO("bean1", 1);
        BeanNameQuantityDTO beanId2 = new BeanNameQuantityDTO("bean2", 2);
        BeanNameQuantityDTO beanId3 = new BeanNameQuantityDTO("bean3", 0);
        beanIdQuantityDTOS.add(beanId1);
        beanIdQuantityDTOS.add(beanId2);
        beanIdQuantityDTOS.add(beanId3);

        PutMenuOrderRqDTO putMenuOrderRqDTO = new PutMenuOrderRqDTO(beanIdQuantityDTOS);
        orderService.modify(menuOrder, putMenuOrderRqDTO);

        assertEquals(orderRepository.count(), 1L);
        assertEquals(detailOrderRepository.count(), 2L);
        assertEquals(menuOrder.getOrders().size(), 2);

        menuOrder = orderRepository.findById(1L).get();

        beanIdQuantityDTOS = new ArrayList<>();
        beanId1 = new BeanNameQuantityDTO("bean1", 0);
        beanIdQuantityDTOS.add(beanId1);
        beanIdQuantityDTOS.add(beanId2);

        putMenuOrderRqDTO = new PutMenuOrderRqDTO(beanIdQuantityDTOS);
        orderService.modify(menuOrder, putMenuOrderRqDTO);

        assertEquals(orderRepository.count(), 1L);
        assertEquals(detailOrderRepository.count(), 1L);
        assertEquals(menuOrder.getOrders().size(), 1);
    }
}