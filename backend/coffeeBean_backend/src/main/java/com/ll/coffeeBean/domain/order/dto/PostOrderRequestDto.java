package com.ll.coffeeBean.domain.order.dto;

import com.ll.coffeeBean.domain.siteUser.dto.CustomerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostOrderRequestDto (
        @Valid
        @NotNull(message = "회원 정보는 필수입니다.")
        CustomerDto customer, // 회원 정보

        @NotNull(message = "상품 정보는 필수입니다.")
        @Size(min = 1, message = "최소 한 개 이상이어야 합니다.")
        List<@Valid PostDetailOrderDto> products // 상품 정보
) { }
