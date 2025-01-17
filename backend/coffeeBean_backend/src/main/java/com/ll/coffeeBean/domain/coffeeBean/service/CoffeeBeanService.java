package com.ll.coffeeBean.domain.coffeeBean.service;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.coffeeBean.repository.CoffeeBeanRepository;
import com.ll.coffeeBean.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void createBean(String name, int price, int quantity) {
        CoffeeBean coffeeBean = CoffeeBean
                .builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
        coffeeBeanRepository.save(coffeeBean);
    }

	// 재고
	public void changeStockWithValidation(CoffeeBean coffeeBean, int orderQuantity) {
		if (coffeeBean.getQuantity() < orderQuantity) {
			throw new ServiceException("400-1", "재고가 부족합니다. 남은 재고: " + coffeeBean.getQuantity());
		}
		coffeeBean.setQuantity(coffeeBean.getQuantity() - orderQuantity);
	}

	public CoffeeBean findByName(String name) {
		Optional<CoffeeBean> coffeeBeanOptional = coffeeBeanRepository.findByName(name);
		if(coffeeBeanOptional.isPresent()){
			return coffeeBeanOptional.get();
		} else {
			throw new ServiceException("404", "CoffeeBean을 찾을 수 없습니다.");
		}
	}
}
