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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PastOrder extends BaseTime {

    @OneToMany(mappedBy = "pastOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetailOrder> orders = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser customer;

    @Column
    private OrderStatus orderStatus;

    public void addDetail(DetailOrder detailOrder) {
        detailOrder.setPastOrder(this);
        orders.add(detailOrder);
    }
}
