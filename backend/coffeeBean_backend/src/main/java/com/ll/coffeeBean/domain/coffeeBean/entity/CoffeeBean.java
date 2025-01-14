package com.ll.coffeeBean.domain.coffeeBean.entity;

import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;

public class CoffeeBean extends BaseTime {

    @Column
    private String name;

    @Column
    private Integer price;
}
