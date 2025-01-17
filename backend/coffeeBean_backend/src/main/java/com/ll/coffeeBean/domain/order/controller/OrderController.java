package com.ll.coffeeBean.domain.order.controller;

import com.ll.coffeeBean.domain.order.dto.*;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.service.DetailOrderService;
import com.ll.coffeeBean.domain.order.service.OrderService;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.service.SiteUserService;
import com.ll.coffeeBean.global.exceptions.ServiceException;
import com.ll.coffeeBean.global.rsData.RsData;
import com.ll.coffeeBean.standard.PageDto.PageDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
	private final DetailOrderService detailOrderService;
	private final SiteUserService siteUserService;



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

    @PostMapping
    public RsData<PostOrderResponseDto> createOrder(@RequestBody @Valid PostOrderRequestDto request) {

        PostOrderResponseDto response = orderService.createOrder(request);

        return new RsData<>(
                "201-1",
                "주문이 완료되었습니다.",
                response
        );
    }


	record LoginDto (
			@NonNull
			@Email
			String email
	) {
	}
	// page num,pageSize, 이메일 데이터를 받아 이메일에 해당하는 MenuOrder 데이터를 page 형태로 받아옴
	//일련번호인 email을 통해 유저 정보 검색
	// 성공시 200번 반환, 서비스 단계에서 DTO에 매핑시켜 필요한 정보만 전송
	//유저 정보가 없는 경우 NoSuchElementException 반환
	@GetMapping("/history")
	RsData<PageDto<GetResMenuOrderDto>> items(@RequestParam(defaultValue = "1") int page,
												 @RequestParam(defaultValue = "10") int pageSize,
												 @RequestBody LoginDto loginDto ) {

		String email = loginDto.email;
		Optional<SiteUser> optionalSiteUser=siteUserService.findByEmail(email);
		PageDto<GetResMenuOrderDto> pageDto=new PageDto<>();
		SiteUser siteUser;

		if(optionalSiteUser.isEmpty()) {
			return new RsData<>(
					"404-1",
					"해당 이메일에 해당하는 주문 내역이 존재하지 않습니다."
					,pageDto
			);
		}
		siteUser=optionalSiteUser.get();

		pageDto=orderService.getList(siteUser,page,pageSize);

		return new RsData<>(
				"200-1",
				"모든 주문 내역이 조회되었습니다.",
				pageDto
		);
	}


	/**
	 * [GET] /api/order/history/{id}
	 * - 요청: 주문 번호 (PathVariable)
	 * - 응답: 하나의 주문에 대한 전체 정보(PagingResMenuOrderDto)를 반환 (상세 주문 항목 포함)
	 */
	@GetMapping("/history/{id}")
	public ResponseEntity<RsData<Map<String, Object>>> getOrderDetail(@PathVariable long id) {
		Optional<MenuOrder> optionalOrder = orderService.findById(id);


		if (optionalOrder.isEmpty()) {
			// 404 응답: 주문을 찾을 수 없을 때 요청된 ID 포함
			Map<String, Object> errorDetails = new HashMap<>();
			errorDetails.put("orderId", id);
			errorDetails.put("error", "해당 id로 주문을 찾을 수 없습니다. id를 다시 확인해주세요");

			return ResponseEntity.status(404)
					.body(new RsData<>("404-1", "주문을 찾을 수 없습니다.", errorDetails));
		}


		MenuOrder menuOrder = optionalOrder.get();

		GetResMenuOrderDto menuOrderDto = new GetResMenuOrderDto(menuOrder);

		List<GetResDetailOrderDto> detailOrders = menuOrder.getOrders()
				.stream()
				.map(GetResDetailOrderDto::new)
				.collect(Collectors.toList());

		Map<String, Object> response = new HashMap<>();
		response.put("menuOrder", menuOrderDto);

		return ResponseEntity.ok(new RsData<>("200-1", "주문 상세 조회 성공", response));
	}

}
