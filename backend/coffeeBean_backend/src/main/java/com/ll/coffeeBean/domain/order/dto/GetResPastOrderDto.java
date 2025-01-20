package com.ll.coffeeBean.domain.order.dto;

import com.ll.coffeeBean.domain.order.entity.PastOrder;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class GetResPastOrderDto {

    @NonNull
    private final long id;

    @NonNull
    private final LocalDateTime createDate;

    @NonNull
    private final LocalDateTime modifyDate;

    @NonNull
    private String email;

    @NonNull
    private String address;

    @NonNull
    private String postCode;

    @NonNull
    private List<GetResDetailOrderDto> orders;

    public GetResPastOrderDto(PastOrder order) {
        this.id = order.getId();
        this.createDate = order.getCreateDate();
        this.modifyDate = order.getModifyDate();

        SiteUser siteUser = order.getCustomer();

        this.email = siteUser.getEmail();
        this.address = siteUser.getAddress();
        this.postCode = siteUser.getPostCode();
        this.orders = order.getOrders().stream()
                .map(GetResDetailOrderDto::new)
                .collect(Collectors.toList());
    }
}
