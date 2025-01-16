package com.ll.coffeeBean.domain.order.repository;

import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<MenuOrder, Long> {
    List<MenuOrder> findByCreateDateGreaterThanEqualAndCreateDateBefore(LocalDateTime startDate, LocalDateTime endDate);
}
