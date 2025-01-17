package com.ll.coffeeBean.domain.coffeeBean.repository;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeBeanRepository extends JpaRepository<CoffeeBean, Long> {
	boolean existsByName(String name);
}
