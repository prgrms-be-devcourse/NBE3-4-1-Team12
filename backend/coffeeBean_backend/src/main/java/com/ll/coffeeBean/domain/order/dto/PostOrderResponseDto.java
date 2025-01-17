package com.ll.coffeeBean.domain.order.dto;

import com.ll.coffeeBean.domain.siteUser.dto.CustomerDto;

import java.util.List;

public record PostOrderResponseDto (
        Long orderId,  // 주문 id
        CustomerDto customer, // 고객 정보
        List<PostDetailOrderDto> products, // 상품 정보
        long totalPrice, // 총 금액
        String status // 주문 상태
) { }