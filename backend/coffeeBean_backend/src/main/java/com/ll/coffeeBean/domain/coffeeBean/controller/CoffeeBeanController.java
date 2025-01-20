package com.ll.coffeeBean.domain.coffeeBean.controller;


import com.ll.coffeeBean.domain.coffeeBean.dto.CoffeeBeanRequestDTO;
import com.ll.coffeeBean.domain.coffeeBean.dto.CoffeeBeanResponseDTO;
import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.coffeeBean.service.CoffeeBeanService;
import com.ll.coffeeBean.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bean")
@Tag(name = "CoffeeBeanController", description = "커피콩 컨트롤러")
public class CoffeeBeanController {

	private final CoffeeBeanService CoffeeBeanService;

	@PostMapping
	@Operation(summary = "커피콩 등록")
	public RsData<CoffeeBeanResponseDTO> createCoffeeBean(@Valid @RequestBody
															  CoffeeBeanRequestDTO reqBody) {

		CoffeeBeanResponseDTO response = CoffeeBeanService.createCoffeeBean(reqBody);

		return new RsData<>(
				"201-1",
				"원두가 추가되었습니다.",
				response
		);

	}

	record UpdateCoffeeBeanRequest(
			@NotNull
			Integer price,
			@NotNull
			Integer quantity
	){}

	@PutMapping("/{id}")
	@Operation(summary = "커피콩 수정")
	public RsData<CoffeeBeanResponseDTO> modifyCoffeeBean(@PathVariable(name = "id") long id,
															 @RequestBody @Valid UpdateCoffeeBeanRequest reqBody) {

		CoffeeBean coffeeBean = CoffeeBeanService.findById(id);
		CoffeeBeanResponseDTO response = CoffeeBeanService.modifyCoffeeBean(coffeeBean, reqBody.price, reqBody.quantity);

		return new RsData<>(
				"200-1",
				"원두가 수정되었습니다.",
				response
		);
	}

	// DELETE 요청은 일단은 처리하지 않기로
}
