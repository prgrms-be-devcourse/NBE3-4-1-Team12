//package com.ll.coffeeBean.domain.coffeeBean.entity;

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
}
