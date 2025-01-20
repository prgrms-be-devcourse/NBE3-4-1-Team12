package com.ll.coffeeBean.domain.coffeeBean.dto;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CoffeeBeanResponseDTO {
	private Long id;
	private String name;
	private Integer price;
	private Integer quantity;

	public CoffeeBeanResponseDTO(CoffeeBean coffeeBean) {
		this.id = coffeeBean.getId();
		this.name = coffeeBean.getName();
		this.price = coffeeBean.getPrice();
		this.quantity = coffeeBean.getQuantity();
	}
}
