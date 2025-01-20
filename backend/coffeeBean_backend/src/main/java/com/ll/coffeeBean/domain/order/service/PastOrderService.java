package com.ll.coffeeBean.domain.order.service;

import com.ll.coffeeBean.domain.order.dto.GetResPastOrderDto;
import com.ll.coffeeBean.domain.order.entity.PastOrder;
import com.ll.coffeeBean.domain.order.repository.PastOrderRepository;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.standard.PageDto.PageDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PastOrderService {
    private final PastOrderRepository pastOrderRepository;

    public PageDto<GetResPastOrderDto> getList(SiteUser siteUser, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<PastOrder> paging = pastOrderRepository.findByCustomer(siteUser, pageable);

        Page<GetResPastOrderDto> pagingOrderDto = paging.map(GetResPastOrderDto::new);
        return new PageDto<>(pagingOrderDto);
    }

    @Transactional
    public void processPastOrderByScheduling() {
        System.out.println("========================");
        System.out.println("Start Scheduled!!\n\n");

        LocalDateTime endDate = LocalDateTime.now().minusMonths(3);

        List<PastOrder> pastOrders = pastOrderRepository.findByCreateDateBefore(endDate);

        // 3개월 전까지의 PastOrder 모두 삭제
        for (PastOrder pastOrder : pastOrders) {
            processPastOrder(pastOrder);
        }

        System.out.println("\n\n========================");
        System.out.println("End Scheduled!!\n\n");
    }

    /**
     * 3개월이 지난 주문 목록 삭제
     */
    @Transactional
    public void processPastOrder(PastOrder order) {
        order.getCustomer().removePastOrder(order);
    }
}
