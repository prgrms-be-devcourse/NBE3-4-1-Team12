package com.ll.coffeeBean.domain.order.dto;

import com.ll.coffeeBean.domain.siteUser.dto.CustomerDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PostOrderRequestDto {
    private CustomerDto customer; // 회원 정보
    private List<PostDetailOrderDto> products; // 상품 정보
}
