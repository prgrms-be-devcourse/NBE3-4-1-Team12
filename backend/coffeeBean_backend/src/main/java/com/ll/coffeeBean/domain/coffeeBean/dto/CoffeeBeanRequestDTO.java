package com.ll.coffeeBean.domain.coffeeBean.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeBeanRequestDTO {

	@NotBlank(message = "이름은 필수 입력입니다.")
	private String name;

	@NotNull
	private Integer price;

	@NotNull
	private Integer quantity;


}
