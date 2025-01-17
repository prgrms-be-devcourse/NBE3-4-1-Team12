package com.ll.coffeeBean.domain.coffeeBean.service;

import com.ll.coffeeBean.domain.coffeeBean.dto.CoffeeBeanRequestDTO;
import com.ll.coffeeBean.domain.coffeeBean.dto.CoffeeBeanResponseDTO;
import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.coffeeBean.repository.CoffeeBeanRepository;
import com.ll.coffeeBean.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoffeeBeanService {
	private final CoffeeBeanRepository coffeeBeanRepository;

	public CoffeeBean findById(Long id) {
		return coffeeBeanRepository.findById(id).get();
	}

	public long count() {
		return coffeeBeanRepository.count();
	}

	// 재고
	public void reduceStockWithValidation(CoffeeBean coffeeBean, int orderQuantity) {
		if (coffeeBean.getQuantity() < orderQuantity) {
			throw new ServiceException("400-1", "재고가 부족합니다. 남은 재고: " + coffeeBean.getQuantity());
		}

		coffeeBean.setQuantity(coffeeBean.getQuantity() - orderQuantity);
	}


	public CoffeeBeanResponseDTO createCoffeeBean(CoffeeBeanRequestDTO reqBody) {

		if (coffeeBeanRepository.existsByName(reqBody.getName())) {
			throw new ServiceException("400-1", "이미 존재하는 원두입니다.");
		}

		CoffeeBean coffeeBean = new CoffeeBean(reqBody.getName(), reqBody.getPrice(), reqBody.getQuantity());
		coffeeBeanRepository.save(coffeeBean);

		return new CoffeeBeanResponseDTO(coffeeBean);

	}


	public CoffeeBeanResponseDTO modifyCoffeeBean(long id, Integer price, Integer quantity) {
		CoffeeBean coffeeBean = findById(id);

		coffeeBean.setPrice(price);

		//수정할 수량이 기존 원두나 추가된 원두 수량보다 작게 수정하면 오류 발생
		//조건
		if(coffeeBean.getQuantity() > quantity){
			throw new ServiceException("400-1", "수정할 수량이 기존 수량보다 작습니다.");
		}
		coffeeBean.setQuantity(quantity);

		coffeeBeanRepository.save(coffeeBean);

		return new CoffeeBeanResponseDTO(coffeeBean);
	}
}
