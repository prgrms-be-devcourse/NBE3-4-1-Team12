package com.ll.coffeeBean.domain.order.controller;

import com.ll.coffeeBean.domain.order.dto.OrderReqDTO;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.service.OrderService;
import com.ll.coffeeBean.global.exceptions.ServiceException;
import com.ll.coffeeBean.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;



	@PutMapping("/{orderId}")
	RsData<OrderReqDTO> modifyOrder(@PathVariable(name = "orderId") long orderId,
									@RequestBody @Valid OrderReqDTO reqBody) {

		System.out.println("주문 id : " + orderId);

		MenuOrder menuOrder = orderService.findById(orderId)
				.orElseThrow(() -> new ServiceException("404", "해당 주문을 찾을 수 없습니다. ID: " + orderId));

		OrderReqDTO orderResDTO = orderService.modify(menuOrder, reqBody);

		return new RsData<>(
				"200-1", "%d번 주문이 수정되었습니다.".formatted(orderId),
				// TODO : 수정된 주문 내역 응답
				orderResDTO
		);
	}


	/*
	@deleteMapping("/api/order/{id}")
Rsdata<Void> deleteOrder(@PathVariable long id) {
		Order order = orderService.findById(id);

		orderService.deleteOrder(order);

		return new RsData<> (
				"200-1", "%d번 주문이 삭제되었습니다." .formatted(id)
};
*/
}
