package com.ll.coffeeBean.domain.order.controller;

import com.ll.coffeeBean.domain.order.dto.PutRepAndResOrderRqDTO;
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
	RsData<PutRepAndResOrderRqDTO> modifyOrder(@PathVariable(name = "orderId") long orderId,
											   @RequestBody @Valid PutRepAndResOrderRqDTO reqBody) {

		// orderId 에 해당하는 주문 찾기
		MenuOrder menuOrder = orderService.findById(orderId)
				.orElseThrow(() -> new ServiceException("404", "해당 주문을 찾을 수 없습니다. ID: " + orderId));

		// 찾은 주문과 사용자 요청 서비스로 전달
		PutRepAndResOrderRqDTO orderPayload = orderService.modify(menuOrder, reqBody);

		// 상태코드와 메시지, 수정 요청한 사용자의 주문 내용 응답에 보냄
		return new RsData<>(
				"200-1", "%d번 주문이 수정되었습니다.".formatted(orderId),
				orderPayload
		);
	}


	@DeleteMapping("/{orderId}")
	RsData<Void> deleteOrder(@PathVariable(name = "orderId") long orderId) {

		MenuOrder menuOrder = orderService.findById(orderId)
				.orElseThrow(() -> new ServiceException("404", "해당 주문을 찾을 수 없습니다. ID: " + orderId));

		orderService.deleteOrder(menuOrder);

		return new RsData<> (
				"200-1", "%d번 주문이 삭제되었습니다." .formatted(orderId));
		}

}
