package com.ll.coffeeBean.domain.order.entity;

import com.ll.coffeeBean.domain.order.enums.OrderStatus;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PastOrder extends BaseTime {
    @Column
    private String name;

    @Column
    private Integer quantity;

    @Column
    private Integer price;

    @Column
    private Integer menuTotalPrice;

    @Column
    private Integer totalPrice;

    @Column
    private OrderStatus orderStatus;

    @ManyToOne
    private SiteUser customer;
}
