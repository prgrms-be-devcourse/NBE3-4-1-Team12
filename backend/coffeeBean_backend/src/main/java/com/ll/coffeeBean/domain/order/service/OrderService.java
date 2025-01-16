package com.ll.coffeeBean.domain.order.service;

import com.ll.coffeeBean.domain.order.dto.DetailOrderDTO;
import com.ll.coffeeBean.domain.order.dto.OrderReqDTO;
import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;

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
