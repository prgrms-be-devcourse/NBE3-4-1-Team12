package com.ll.coffeeBean.domain.order.repository;

import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<MenuOrder, Long> {
    List<MenuOrder> findByCreateDateGreaterThanEqualAndCreateDateBefore(LocalDateTime startDate, LocalDateTime endDate);
    Page<MenuOrder> findByCustomer(Pageable pageable, SiteUser siteUser);

    List<MenuOrder> findAllById(Long id);
    Optional<MenuOrder> findFirstByOrderByIdDesc();
}
