package com.ll.coffeeBean.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProcessOrderDto {
    private final String name;
    private final int quantity;
    private final int price;
    private final int menuTotalPrice;
}
