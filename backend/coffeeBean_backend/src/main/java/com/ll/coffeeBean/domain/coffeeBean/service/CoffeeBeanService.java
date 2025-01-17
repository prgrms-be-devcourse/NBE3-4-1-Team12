package com.ll.coffeeBean.domain.coffeeBean.service;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.coffeeBean.repository.CoffeeBeanRepository;
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
}
