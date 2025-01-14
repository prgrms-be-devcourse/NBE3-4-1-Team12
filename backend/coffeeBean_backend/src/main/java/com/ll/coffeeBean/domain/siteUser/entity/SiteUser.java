package com.ll.coffeeBean.domain.siteUser.entity;

import com.ll.coffeeBean.domain.order.entity.Order;
import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import java.util.List;

public class SiteUser extends BaseTime {

    @Column(unique = true)
    private String email;

    @Column
    private String address;

    @Column
    private String postCode;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> menu;
}
