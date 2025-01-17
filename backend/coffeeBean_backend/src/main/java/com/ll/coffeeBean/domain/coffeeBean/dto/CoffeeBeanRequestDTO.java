package com.ll.coffeeBean.domain.coffeeBean.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoffeeBeanRequestDTO {

	@NotBlank(message = "이름은 필수 입력입니다.")
	private String name;

	@NotNull
	private Integer price;

	@NotNull
	private Integer quantity;


}
