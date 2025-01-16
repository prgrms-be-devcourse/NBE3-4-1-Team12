package com.ll.coffeeBean.domain.siteUser.entity;

import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class SiteUser extends BaseTime {

    @Column(unique = true)
    private String email;

    @Column
    private String address;

    @Column
    private String postCode;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuOrder> menu;
}
