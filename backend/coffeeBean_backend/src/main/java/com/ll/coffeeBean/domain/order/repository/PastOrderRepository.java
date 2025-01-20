package com.ll.coffeeBean.domain.order.repository;

import com.ll.coffeeBean.domain.order.entity.PastOrder;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PastOrderRepository extends JpaRepository<PastOrder, Long> {

    List<PastOrder> findByCreateDateBefore(LocalDateTime endDate);

    Page<PastOrder> findByCustomer(SiteUser siteUser, Pageable pageable);
    PastOrder findByCustomer(SiteUser siteUser);
}
