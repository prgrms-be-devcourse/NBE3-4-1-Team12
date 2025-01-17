package com.ll.coffeeBean.domain.order.entity;

import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailOrder extends BaseTime {
    @Column
    private String name;

    // 커피콩 한 종류 주문 수량
    @Column
    private Integer quantity;

    //프론트에서 가격 데이터를 받거나
    //CoffeeBean db 데이터에서 가격 정보를 얻음.
    //서로 공통된 속성인 name 으로 커피콩 정보 검색
    @Column
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuOrder order;
}
