package com.ll.coffeeBean.domain.order.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

//dto 변경?
@Getter
@Setter
public class PutRepAndResDetailOrderDTO {
	private Long Id;

	@Min(value = 0L, message = "주문 수량은 0 이상이어야 합니다.")
	private Integer quantity;
}
