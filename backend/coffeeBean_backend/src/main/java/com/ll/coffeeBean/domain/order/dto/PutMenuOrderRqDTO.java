package com.ll.coffeeBean.domain.order.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PutMenuOrderRqDTO {

	@Valid
	private List<BeanIdQuantityDTO> coffeeOrders;
}
