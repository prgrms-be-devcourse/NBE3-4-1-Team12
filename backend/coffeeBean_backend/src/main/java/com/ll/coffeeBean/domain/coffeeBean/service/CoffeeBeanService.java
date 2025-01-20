package com.ll.coffeeBean.domain.coffeeBean.service;

import com.ll.coffeeBean.domain.coffeeBean.dto.CoffeeBeanRequestDTO;
import com.ll.coffeeBean.domain.coffeeBean.dto.CoffeeBeanResponseDTO;
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

    public CoffeeBeanResponseDTO createCoffeeBean(CoffeeBeanRequestDTO reqBody) {
        if (coffeeBeanRepository.existsByName(reqBody.getName())) { //이미 존재하는 원두인지 확인
            throw new ServiceException("400-1", "이미 존재하는 원두입니다.");
        }


        CoffeeBean coffeeBean = new CoffeeBean(reqBody.getName(), reqBody.getPrice(), reqBody.getQuantity());
        coffeeBeanRepository.save(coffeeBean);

        return new CoffeeBeanResponseDTO(coffeeBean);

    }

    public CoffeeBeanResponseDTO modifyCoffeeBean(CoffeeBean coffeeBean, Integer price, Integer quantity) {

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
