package com.ll.coffeeBean.domain.coffeeBean.entity;

import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


//커피콩 정보와 재고량을 저장하는 엔티티
@Entity
@Getter
@Setter
public class CoffeeBean extends BaseTime {

    @Column
    private String name;

    @Column
    private Integer price;

    //총 재고 수
    @Column
    private Integer quantity;
}
