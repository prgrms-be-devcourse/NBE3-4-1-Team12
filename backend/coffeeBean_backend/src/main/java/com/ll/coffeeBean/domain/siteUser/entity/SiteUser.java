package com.ll.coffeeBean.domain.siteUser.entity;

import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.entity.PastOrder;
import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteUser extends BaseTime {

    @Column(unique = true)
    private String email;

    @Column
    private String address;

    @Column
    private String postCode;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuOrder> menu;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PastOrder> pastOrders;
}
