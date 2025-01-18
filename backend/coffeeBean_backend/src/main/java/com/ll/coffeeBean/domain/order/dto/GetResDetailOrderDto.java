package com.ll.coffeeBean.domain.order.dto;

import com.ll.coffeeBean.domain.order.entity.DetailOrder;


import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
public class GetResDetailOrderDto {

    @NonNull
    private final long id;

    private String name;

    // 커피콩 한 종류 주문 수량
    private Integer quantity;

    public GetResDetailOrderDto(DetailOrder detailOrder) {
        this.id = detailOrder.getId();
        this.name=detailOrder.getName();
        this.quantity=detailOrder.getQuantity();

    }
}

