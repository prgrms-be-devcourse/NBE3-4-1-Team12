package com.ll.coffeeBean.global.initData;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.coffeeBean.repository.CoffeeBeanRepository;
import com.ll.coffeeBean.domain.coffeeBean.service.CoffeeBeanService;
import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.service.OrderService;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.SiteUserRepository;
import com.ll.coffeeBean.domain.siteUser.service.SiteUserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final OrderService orderService;
    private final SiteUserService siteUserService;
    private final SiteUserRepository siteUserRepository;
    private final CoffeeBeanService coffeeBeanService;
    private final CoffeeBeanRepository coffeeBeanRepository;

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
            CoffeeBean bean1 = new CoffeeBean("Columbia Nariñó", 5000, 50);
            coffeeBeanRepository.save(bean1);
            CoffeeBean bean2 = new CoffeeBean("Brazil Serra Do Caparao", 5500, 50);
            coffeeBeanRepository.save(bean2);
            CoffeeBean bean3 = new CoffeeBean("Columbia Quindio", 6000, 50);
            coffeeBeanRepository.save(bean3);
            CoffeeBean bean4 = new CoffeeBean("Ethiopia Sidamo", 6500, 50);
            coffeeBeanRepository.save(bean4);
        }

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

        // 주문 샘플 데이터 생성
        if (orderService.count() == 0) {
            MenuOrder order1 = new MenuOrder();
            order1.setCustomer(user1);

            // DetailOrder 생성 시, order 를 추가하여 연결
            order1.addDetail(DetailOrder.builder()
                    .name("Columbia Nariñó")
                    .quantity(1)
                    .price(5000)
                    .order(order1)
                    .build());

            order1.addDetail(DetailOrder.builder()
                    .name("Brazil Serra Do Caparao")
                    .quantity(1)
                    .price(5500)
                    .order(order1)
                    .build());

            order1.addDetail(DetailOrder.builder()
                    .name("Columbia Quindio")
                    .quantity(1)
                    .price(6000)
                    .order(order1)
                    .build());

            order1.addDetail(DetailOrder.builder()
                    .name("Ethiopia Sidamo")
                    .quantity(1)
                    .price(6500)
                    .order(order1)
                    .build());

            user1.addOrder(order1);

            // 연관관계로 인해 얽혀있는 3개의 엔티티가 모두 저장!
            siteUserRepository.save(user1);
        }
    }
}
