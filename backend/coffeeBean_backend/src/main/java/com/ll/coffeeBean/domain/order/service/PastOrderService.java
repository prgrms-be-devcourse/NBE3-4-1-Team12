package com.ll.coffeeBean.domain.order.service;

import com.ll.coffeeBean.domain.order.dto.GetResPastOrderDto;
import com.ll.coffeeBean.domain.order.entity.PastOrder;
import com.ll.coffeeBean.domain.order.repository.PastOrderRepository;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.standard.PageDto.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PastOrderService {
    private final PastOrderRepository pastOrderRepository;

    public PageDto<GetResPastOrderDto> getList(SiteUser siteUser, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<PastOrder> paging = pastOrderRepository.findByCustomer(pageable, siteUser);

        Page<GetResPastOrderDto> pagingOrderDto = paging.map(GetResPastOrderDto::new);
        return new PageDto<>(pagingOrderDto);
    }
}
