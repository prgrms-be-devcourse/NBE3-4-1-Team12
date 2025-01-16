package com.ll.coffeeBean.domain.order.repository;

import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<MenuOrder, Long> {

}
