package com.ll.coffeeBean.domain.order.controller;

import com.ll.coffeeBean.domain.order.dto.PutMenuOrderRqDTO;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.dto.PostOrderRequestDto;
import com.ll.coffeeBean.domain.order.dto.PostOrderResponseDto;
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
	RsData<PutMenuOrderRqDTO> modifyOrder(@PathVariable(name = "orderId") long orderId,
										  @RequestBody @Valid PutMenuOrderRqDTO reqDetailOrders) {
		// PutMenuOrderRqDTO 를 통해 커피콩들의 아이디와 수량이 요청 바디로 넘어옴

		// orderId 에 해당하는 주문 찾기
		MenuOrder menuOrder = orderService.findById(orderId)
				.orElseThrow(() -> new ServiceException("404", "해당 주문을 찾을 수 없습니다. ID: " + orderId));

		// 찾은 주문과 사용자 요청 서비스로 전달
		PutMenuOrderRqDTO orderPayload = orderService.modify(menuOrder, reqDetailOrders);

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

    @PostMapping
    public RsData<PostOrderResponseDto> createOrder(@RequestBody @Valid PostOrderRequestDto request) {
        PostOrderResponseDto response = orderService.createOrder(request);
        return new RsData<>(
                "201-1",
                "주문이 완료되었습니다.",
                response
        );
    }
}
