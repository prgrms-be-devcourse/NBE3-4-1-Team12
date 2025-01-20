package com.ll.coffeeBean.domain.order.controller;


import com.ll.coffeeBean.domain.order.dto.GetResDetailOrderDto;
import com.ll.coffeeBean.domain.order.dto.GetResMenuOrderDto;
import com.ll.coffeeBean.domain.order.dto.PostOrderRequestDto;
import com.ll.coffeeBean.domain.order.dto.PostOrderResponseDto;
import com.ll.coffeeBean.domain.order.dto.PutMenuOrderRqDTO;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.service.OrderService;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.service.SiteUserService;
import com.ll.coffeeBean.global.exceptions.ServiceException;
import com.ll.coffeeBean.global.rsData.RsData;
import com.ll.coffeeBean.standard.PageDto.PageDto;
import com.ll.coffeeBean.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@Tag(name = "OrderController", description = "주문 컨트롤러")
public class OrderController {
    private final OrderService orderService;
    private final SiteUserService siteUserService;


    @PutMapping("/{orderId}")
    @Operation(summary = "주문 수정")
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
    @Operation(summary = "주문 삭제")
    RsData<Void> deleteOrder(@PathVariable(name = "orderId") long orderId) {
        MenuOrder menuOrder = orderService.findById(orderId)
                .orElseThrow(() -> new ServiceException("404", "해당 주문을 찾을 수 없습니다. ID: " + orderId));
        orderService.deleteOrder(menuOrder);
        return new RsData<>(
                "200-1", "%d번 주문이 삭제되었습니다.".formatted(orderId));
    }


    @PostMapping
    @Operation(summary = "주문 등록")
    public RsData<PostOrderResponseDto> createOrder(@RequestBody @Valid PostOrderRequestDto request) {
        PostOrderResponseDto response = orderService.createOrder(request);
        return new RsData<>(
                "201-1",
                "주문이 완료되었습니다.",
                response
        );
    }


    @GetMapping("/login")
    RsData<Empty> login(@RequestParam(name = "email", required = true)
                        @NotBlank(message = "이메일을 입력해주세요.")
                        @Email(message = "올바른 이메일 형식이 아닙니다.") String email) {

        return new RsData<>(
                "200-21",
                "이메일 검증 성공."

        );
    }


    /**
     * [GET] /api/order/history/{id} - 요청: 주문 번호 (PathVariable) - 응답: 하나의 주문에 대한 전체 정보(PagingResMenuOrderDto)를 반환 (상세 주문
     * 항목 포함)
     */
    @GetMapping("/history/{id}")
    @Operation(summary = "주문 단건조회")
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


    // page num,pageSize, 이메일 데이터를 받아 이메일에 해당하는 MenuOrder 데이터를 page 형태로 받아옴
    //일련번호인 email을 통해 유저 정보 검색
    // 성공시 200번 반환, 서비스 단계에서 DTO에 매핑시켜 필요한 정보만 전송
    //유저 정보가 없는 경우 NoSuchElementException 반환
    @GetMapping("/history")
    RsData<PageDto<GetResMenuOrderDto>> items(@RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(name = "email", required = true) String email) {

        Optional<SiteUser> optionalSiteUser = siteUserService.findByEmail(email);
        PageDto<GetResMenuOrderDto> pageDto = new PageDto<>();
        SiteUser siteUser;

        if (optionalSiteUser.isEmpty()) {
            return new RsData<>(
                    "404-1",
                    "해당 이메일에 해당하는 주문 내역이 존재하지 않습니다."
                    , pageDto
            );
        }
        siteUser = optionalSiteUser.get();

        pageDto = orderService.getList(siteUser, page, pageSize);

        return new RsData<>(
                "200-1",
                "모든 주문 내역이 조회되었습니다.",
                pageDto
        );
    }


}
