package com.ll.coffeeBean.domain.order.dto;

import com.ll.coffeeBean.domain.siteUser.dto.CustomerDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    @NotBlank(message = "회원 정보는 필수입니다.")
    private CustomerDto customer; // 회원 정보
    @NotBlank(message = "주문할 상품이 없습니다.")
    private List<OrderProductDto> products; // 상품 정보
}
