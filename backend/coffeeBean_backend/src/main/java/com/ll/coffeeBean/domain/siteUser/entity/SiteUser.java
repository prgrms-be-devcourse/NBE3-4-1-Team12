package com.ll.coffeeBean.domain.siteUser.entity;

import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.entity.PastOrder;
import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;

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
    @Builder.Default
    private List<MenuOrder> menu = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PastOrder> pastOrders;

    public void addMenu(MenuOrder menu) {
        this.menu.add(menu);
        menu.setCustomer(this);
    }

    public void removeMenu(MenuOrder menu) {
        this.menu.remove(menu);
    }

    public void removePastOrders(PastOrder pastOrder) {
        this.pastOrders.remove(pastOrder);
    }
}
