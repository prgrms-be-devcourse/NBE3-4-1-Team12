package com.ll.coffeeBean.domain.order.service;


import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.repository.DetailOrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DetailOrderService {

    private final DetailOrderRepository detailOrderRepository;

    @Transactional
    public DetailOrder create(String name, int quantity, MenuOrder menuOrder) {
        DetailOrder detailOrder = new DetailOrder();
        detailOrder.setName(name);
        detailOrder.setQuantity(quantity);
        detailOrder.setOrder(menuOrder);

        detailOrderRepository.save(detailOrder);

        return detailOrder;
    }


}