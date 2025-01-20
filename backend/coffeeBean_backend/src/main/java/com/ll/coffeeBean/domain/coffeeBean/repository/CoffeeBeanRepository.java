package com.ll.coffeeBean.domain.coffeeBean.repository;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoffeeBeanRepository extends JpaRepository<CoffeeBean, Long> {
    Optional<CoffeeBean> findByName(String name);


    boolean existsByName(String name);
}
