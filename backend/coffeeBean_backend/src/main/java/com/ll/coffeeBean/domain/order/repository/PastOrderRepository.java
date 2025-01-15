package com.ll.coffeeBean.domain.order.repository;

import com.ll.coffeeBean.domain.order.entity.PastOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PastOrderRepository extends JpaRepository<PastOrder, Long> {
    List<PastOrder> findAllByCustomerId(long id);
}
