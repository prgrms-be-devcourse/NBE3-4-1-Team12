package com.ll.coffeeBean.domain.order.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    READY_FOR_DELIVERY("배송 준비중"),
    DELIVERED("배송 완료"),
    CANCELLED("취소");


    private final String value;
}
