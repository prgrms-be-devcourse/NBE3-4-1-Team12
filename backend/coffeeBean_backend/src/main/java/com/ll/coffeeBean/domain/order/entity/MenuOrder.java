package com.ll.coffeeBean.domain.order.entity;

import com.ll.coffeeBean.domain.order.enums.OrderStatus;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.global.jpa.entity.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
public class MenuOrder extends BaseTime {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<DetailOrder> orders = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser customer;

    @Column
    private OrderStatus orderStatus;

    public void addDetail(DetailOrder order) {
        this.orders.add(order);
    }

    public void removeDetail(DetailOrder detailOrder) {
        detailOrder.setOrder(null);
        this.orders.remove(detailOrder);
    }
}
