package com.ll.coffeeBean.domain.coffeeBean.entity;

import com.ll.coffeeBean.global.exceptions.ServiceException;
import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoffeeBean extends BaseTime {

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private Integer quantity;

    // 재고
    public void reduceStockWithValidation(int orderQuantity) {
        if (this.quantity < orderQuantity) {
            throw new ServiceException("400-1", "재고가 부족합니다. 남은 재고: " + this.quantity);
        }
        this.quantity -= orderQuantity;
    }
}
