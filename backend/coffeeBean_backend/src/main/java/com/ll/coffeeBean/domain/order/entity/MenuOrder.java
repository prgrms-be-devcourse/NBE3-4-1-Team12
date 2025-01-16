package com.ll.coffeeBean.domain.order.entity;

import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class MenuOrder extends BaseTime {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailOrder> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser customer;
}