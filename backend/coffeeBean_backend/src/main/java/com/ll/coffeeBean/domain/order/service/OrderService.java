package com.ll.coffeeBean.domain.order.service;

import com.ll.coffeeBean.domain.order.dto.DetailOrderDTO;
import com.ll.coffeeBean.domain.order.dto.OrderReqDTO;
import com.ll.coffeeBean.domain.order.dto.ProcessOrderDto;
import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.entity.PastOrder;
import com.ll.coffeeBean.domain.order.enums.OrderStatus;
import com.ll.coffeeBean.domain.order.repository.OrderRepository;
import com.ll.coffeeBean.domain.order.repository.PastOrderRepository;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PastOrderRepository pastOrderRepository;

    /**
     * TODO : 효율적인 스케쥴링 정하기, print -> 로그로 변경하기
     * What : 어차피 매일 14시에 실행되는데, 굳이 atTime 까지 쓸 필요가 있는가
     */
    public void processOrderByScheduled() {
        System.out.println("========================");
        System.out.println("Start Scheduled!!\n\n");

//        LocalDateTime endDate = LocalDateTime.now().toLocalDate().atTime(14, 0);
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);

        List<MenuOrder> orders = orderRepository.findByCreateDateGreaterThanEqualAndCreateDateBefore(startDate, endDate);

        for (MenuOrder order : orders) {
            List<DetailOrder> detailOrders = order.getOrders();
            List<ProcessOrderDto> orderDtos = new ArrayList<>();
            int totalPrice = 0;

            for (DetailOrder detailOrder : detailOrders) {
                int menuTotalPrice = detailOrder.getPrice() * detailOrder.getQuantity();    // 메뉴별 총액
                totalPrice += menuTotalPrice;

                orderDtos.add(ProcessOrderDto.builder()
                        .name(detailOrder.getName())
                        .quantity(detailOrder.getQuantity())
                        .price(detailOrder.getPrice())
                        .menuTotalPrice(menuTotalPrice)
                        .build());
            }

            processOrder(orderDtos, order.getCustomer(), totalPrice);
        }

        // 모든 작업 처리 후, 기존의 Order DB 모두 삭제
        orderRepository.deleteAll(orders);

        System.out.println("\n\n========================");
        System.out.println("End Scheduled!!\n\n");
    }

    /**
     * 주문 처리 (추후 구현) 처리 방법 정의 후 구현 예정
     */
    public void processOrder(List<ProcessOrderDto> processOrderDtos, SiteUser customer, int totalPrice) {
        /**
         * TODO : 작업 처리, 처리된 작업 및 처리 도중 오류 로깅
         */

        // 처리된 작업은 지난 주문 DB에 저장
        for (ProcessOrderDto processOrderDto : processOrderDtos) {
            pastOrderRepository.save(PastOrder.builder()
                    .name(processOrderDto.getName())
                    .price(processOrderDto.getPrice())
                    .menuTotalPrice(processOrderDto.getMenuTotalPrice())
                    .totalPrice(totalPrice)
                    .quantity(processOrderDto.getQuantity())
                    .customer(customer)
                    .orderStatus(OrderStatus.DELIVERED)
                    .build());
        }
    }



	public long count() {
		return orderRepository.count();
	}

	public Optional<MenuOrder> findById(long id) {
		Optional<MenuOrder> order = orderRepository.findById(id);

		if(order.isEmpty()) {
			System.out.println("Order not found for ID: " + id);
		} else {
			System.out.println("Order found: " + order.get().getId());
		}

		return order;
	}


	// 주문 수정 (MenuOrder 안에 있는 DetailOrder 수정)
	// MenuOrder : 고객의 주문 정보
	// DetailOrder : 각 커피콩 별 주문
	// OrderReqDTO : DetailOrderDTO 리스트 담겨있음
	// DetailOrderDTO : DetailOrder 와 유사. id 와 quantity 만 있음
	public OrderReqDTO modify(MenuOrder menuOrder, OrderReqDTO reqBody) {
		List<DetailOrderDTO> orderDTOList = reqBody.getCoffeeOrders();

		for(DetailOrderDTO detailOrderDTO : orderDTOList){
			DetailOrder beanOrderToChange = menuOrder.getOrders()
					.stream()
					.filter(beanOrder -> beanOrder.getId().equals(detailOrderDTO.getId()))
					.findFirst().get();

			beanOrderToChange.setQuantity(detailOrderDTO.getQuantity());
		}

		List<DetailOrderDTO> detailOrderDTOList = new ArrayList<>();
		for(DetailOrder beanOrders : menuOrder.getOrders()) {
			DetailOrderDTO detailOrderDTO = new DetailOrderDTO();
			detailOrderDTO.setId(beanOrders.getId());
			detailOrderDTO.setQuantity(beanOrders.getQuantity());
			detailOrderDTOList.add(detailOrderDTO);
		}
		OrderReqDTO orderReqDTO = new OrderReqDTO();
		orderReqDTO.setCoffeeOrders(detailOrderDTOList);

		return orderReqDTO;
	}
}
