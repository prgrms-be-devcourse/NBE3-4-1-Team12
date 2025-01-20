package com.ll.coffeeBean.domain.order.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PutMenuOrderRqDTO {

	@Valid
	private List<BeanIdQuantityDTO> coffeeOrders;
}
