package com.ll.coffeeBean.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRqDTO {

	private List<DetailOrderDTO> coffeeOrders;
}
