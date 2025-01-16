package com.ll.coffeeBean.domain.order.repository;

import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<MenuOrder, Long> {
    List<MenuOrder> findByCreateDateGreaterThanEqualAndCreateDateBefore(LocalDateTime startDate, LocalDateTime endDate);
}