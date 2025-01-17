package com.ll.coffeeBean.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PostDetailOrderDto(
        @NotNull
        Long id, // 상품 id

        @Min(value = 1, message = "1개 이상 주문해야 합니다.")
        @NotNull
        Integer quantity // 구매 수량
<<<<<<< HEAD
) { }
=======
) { }
>>>>>>> 40814ec (refactor: dto recode로 변경)
