package com.ll.coffeeBean.domain.order.dto;

import lombok.Getter;

@Getter
public class OrderProductDto {
    private long id; // 상품 id
    private String name; // 상품 이름
    private int quantity; // 구매 수량
}
