package com.ll.coffeeBean.domain.coffeeBean.repository;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeBeanRepository extends JpaRepository<CoffeeBean, Long> {
}
