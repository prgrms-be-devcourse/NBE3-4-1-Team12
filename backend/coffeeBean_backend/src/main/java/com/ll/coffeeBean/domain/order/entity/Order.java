package com.ll.coffeeBean.domain.order.entity;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

public class Order extends BaseTime {

    //coffeeBean 엔티티와 Order 엔티티 설계 다시해야함.
    @ManyToMany(fetch = FetchType.LAZY)
    private CoffeeBean coffeeBean;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser customer;
}
