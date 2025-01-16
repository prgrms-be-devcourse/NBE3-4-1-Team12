package com.ll.coffeeBean.domain.order.dto;

import com.ll.coffeeBean.domain.siteUser.dto.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostOrderResponseDto {
    private long orderId; // 주문 id
    private CustomerDto customer; // 고객 정보
    private List<PostDetailOrderDto> products; // 상품 정보
    private long totalPrice; // 총 금액
    private String status; // 주문 상태
}