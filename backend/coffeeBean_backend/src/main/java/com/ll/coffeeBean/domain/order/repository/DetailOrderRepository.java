package com.ll.coffeeBean.domain.order.repository;

import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailOrderRepository extends JpaRepository<DetailOrder, Long> {
}
