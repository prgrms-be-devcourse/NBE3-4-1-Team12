package com.ll.coffeeBean.global.initData;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.coffeeBean.repository.CoffeeBeanRepository;
import com.ll.coffeeBean.domain.coffeeBean.service.CoffeeBeanService;
import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.repository.DetailOrderRepository;
import com.ll.coffeeBean.domain.order.repository.OrderRepository;
import com.ll.coffeeBean.domain.order.service.OrderService;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.SiteUserRepository;
<<<<<<< HEAD

import com.ll.coffeeBean.domain.siteUser.service.UserService;

import com.ll.coffeeBean.domain.siteUser.service.SiteUserService;

=======
import com.ll.coffeeBean.domain.siteUser.service.SiteUserService;
>>>>>>> 942f7bb4dda87e5b85e542f3d40ab73c89a88b1e
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
<<<<<<< HEAD

	private final OrderService orderService;
	private final OrderRepository orderRepository;
	private final UserService userService;
	private final SiteUserRepository siteUserRepository;
	private final DetailOrderRepository detailOrderRepository;
	private final CoffeeBeanService coffeeBeanService;
	private final CoffeeBeanRepository coffeeBeanRepository;
    private final SiteUserService siteUserService;


=======
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final SiteUserService siteUserService;
    private final SiteUserRepository siteUserRepository;
    private final DetailOrderRepository detailOrderRepository;
    private final CoffeeBeanService coffeeBeanService;
    private final CoffeeBeanRepository coffeeBeanRepository;
>>>>>>> 942f7bb4dda87e5b85e542f3d40ab73c89a88b1e

    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        // 커피콩 샘플 데이터 생성
        if (coffeeBeanService.count() == 0) {
            CoffeeBean bean1 = new CoffeeBean("bean1", 1000, 49);
            coffeeBeanRepository.save(bean1);
            CoffeeBean bean2 = new CoffeeBean("bean2", 1000, 48);
            coffeeBeanRepository.save(bean2);
            CoffeeBean bean3 = new CoffeeBean("bean3", 1000, 47);
            coffeeBeanRepository.save(bean3);
        }

<<<<<<< HEAD

=======
>>>>>>> 942f7bb4dda87e5b85e542f3d40ab73c89a88b1e
        // 고객 샘플 데이터 생성
        SiteUser user1;
        Optional<SiteUser> user1Optional = siteUserService.findByEmail("user1@naver.com");
        if (user1Optional.isEmpty()) {
            user1 = new SiteUser();
            user1.setEmail("user1@naver.com");
            siteUserRepository.save(user1);
        } else {
            user1 = user1Optional.get();
        }
<<<<<<< HEAD

=======
>>>>>>> 942f7bb4dda87e5b85e542f3d40ab73c89a88b1e

        // 주문 샘플 데이터 생성
        if (orderService.count() == 0) {
            MenuOrder order1 = new MenuOrder();
            order1.setCustomer(user1);

            // DetailOrder 생성 시, order 를 추가하여 연결
            order1.addDetail(DetailOrder.builder()
                    .name("bean1")
                    .quantity(1)
                    .price(1000)
                    .order(order1)
                    .build());

            order1.addDetail(DetailOrder.builder()
                    .name("bean2")
                    .quantity(1)
                    .price(1000)
                    .order(order1)
                    .build());

            order1.addDetail(DetailOrder.builder()
                    .name("bean3")
                    .quantity(1)
                    .price(1000)
                    .order(order1)
                    .build());

            user1.addOrder(order1);

            // 연관관계로 인해 얽혀있는 3개의 엔티티가 모두 저장!
            siteUserRepository.save(user1);

//			DetailOrder bean1 = DetailOrder.builder()
//                    .name("bean1")
//                    .quantity(1)
//                    .price(1000)
//                    .build();
//            order1.addDetail(bean1);
////			detailOrderRepository.save(bean1);
//
//            DetailOrder bean2 = DetailOrder.builder()
//                    .name("bean2")
//                    .quantity(1)
//                    .price(1000)
//                    .build();
//            order1.addDetail(bean2);
////			detailOrderRepository.save(bean2);
//
//            DetailOrder bean3 = DetailOrder.builder()
//                    .name("bean2")
//                    .quantity(1)
//                    .price(1000)
//                    .build();
//
//            order1.addDetail(bean3);
//			detailOrderRepository.save(bean3);

//			List<DetailOrder> orderList = new ArrayList<>();
//			orderList.add(bean1);
//			orderList.add(bean2);
//			orderList.add(bean3);
//			order1.setOrders(orderList);

//			orderRepository.save(order1);
        }
    }
}
