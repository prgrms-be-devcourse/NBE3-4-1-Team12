package com.ll.coffeeBean.domain.order.dto;

import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetResMenuOrderDto {

    @NonNull
    private final long id;

    @NonNull
    private final LocalDateTime createDate;

    @NonNull
    private final LocalDateTime modifyDate;

    @NonNull
    private String email;    // 고객 이메일

    @NonNull
    private String address;          // 배송 주소

    @NonNull
    private String postCode;

    @NonNull
    private List<GetResDetailOrderDto> orders;

    public GetResMenuOrderDto(MenuOrder order) {
        this.id = order.getId();
        this.createDate = order.getCreateDate();
        this.modifyDate = order.getModifyDate();
        this.orders = order.getOrders()
                .stream()
                .map(GetResDetailOrderDto::new)
                .collect(Collectors.toList());
        SiteUser siteUser=order.getCustomer();
        this.email=siteUser.getEmail();
        this.address=siteUser.getAddress();
        this.postCode=siteUser.getPostCode();
    }
}
