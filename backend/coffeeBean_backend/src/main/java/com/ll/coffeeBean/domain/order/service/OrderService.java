package com.ll.coffeeBean.domain.order.service;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.coffeeBean.service.CoffeeBeanService;
import com.ll.coffeeBean.domain.order.dto.DetailOrderDTO;
import com.ll.coffeeBean.domain.order.dto.OrderRqDTO;
import com.ll.coffeeBean.domain.order.dto.ProcessOrderDto;
import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.entity.PastOrder;
import com.ll.coffeeBean.domain.order.enums.OrderStatus;
import com.ll.coffeeBean.domain.order.repository.OrderRepository;
import com.ll.coffeeBean.domain.order.repository.PastOrderRepository;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.global.exceptions.ServiceException;
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
	private final CoffeeBeanService coffeeBeanService;

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

		return order;
	}

	// 주문 수량과 재고 관련 로직
	// 주문 수정 로직용, 다른 곳에서 사용하려면 수정 필요
	public void checkStockQuantity(DetailOrderDTO detailOrderDTO, DetailOrder beanOrderToChange) {

		CoffeeBean coffeeBean = coffeeBeanService.findById(detailOrderDTO.getId());
		
		// 변경되어야 하는 수량 : 신규 수량 - 기존 수량
		int changeQuantity =  detailOrderDTO.getQuantity() - beanOrderToChange.getQuantity(); // 변경되는 수량

		// 재고가 있는지 확인
		if(coffeeBean.getQuantity() - changeQuantity < 0) {
			// 그건 안됨! 오류 뿜기
			throw new ServiceException("409", "재고가 부족합니다.ㅜㅜㅜ");
		} else {
			// 재고 변경
			coffeeBean.setQuantity(coffeeBean.getQuantity() - changeQuantity);
		}

		System.out.println("변경된 재고 : " + coffeeBean.getQuantity() + " / 재고가 %d 만큼 변경되었습니다.".formatted(changeQuantity));
	}

	// 주문 수정 비즈니스 로직 (MenuOrder 안에 있는 DetailOrder 수정)
	// MenuOrder : 고객의 주문 정보
	// DetailOrder : 각 커피콩 별 주문
	// OrderReqDTO : DetailOrderDTO 리스트 담겨있음
	// DetailOrderDTO : DetailOrder 와 유사. id 와 quantity 만 있음
	public OrderRqDTO modify(MenuOrder menuOrder, OrderRqDTO reqBody) {
		// reqBody 에 담겨있는 주문 목록 받기
		List<DetailOrderDTO> orderDTOList = reqBody.getCoffeeOrders();

		// 받아온 주문들의 각 커피콩 별 주문 처리
		for(DetailOrderDTO detailOrderDTO : orderDTOList){
			// 요청된 수량 변경 해야 하는 id 의 커피콩 찾기
			DetailOrder beanOrderToChange = menuOrder.getOrders()
					.stream()
					.filter(beanOrder -> beanOrder.getId().equals(detailOrderDTO.getId()))
					.findFirst().get();

			// 재고관련 확인 및 처리
			checkStockQuantity(detailOrderDTO, beanOrderToChange);

			// 커피콩 주문 수량 변경
			if(detailOrderDTO.getQuantity() == 0) {
				// 변경 수량이 0이면 아예 DetailOrder 를 삭제 (수량이 0인 주문은 없도록)
				menuOrder.getOrders().remove(beanOrderToChange);
			} else {
				// 실제 수량 변경 로직
				beanOrderToChange.setQuantity(detailOrderDTO.getQuantity());
			}
		}

		// DTO 에 담기 위한 작업들
		// DetailOrder 에는 id 와 수량 외에도 가격 등 필요 없는 부분들 많음
		List<DetailOrderDTO> detailOrderDTOList = new ArrayList<>();
		for(DetailOrder beanOrders : menuOrder.getOrders()) {
			DetailOrderDTO detailOrderDTO = new DetailOrderDTO();
			detailOrderDTO.setId(beanOrders.getId());
			detailOrderDTO.setQuantity(beanOrders.getQuantity());
			detailOrderDTOList.add(detailOrderDTO);
		}
		OrderRqDTO orderReqDTO = new OrderRqDTO();
		orderReqDTO.setCoffeeOrders(detailOrderDTOList);

		return orderReqDTO;
	}

	public void deleteOrder(MenuOrder menuOrder) {
		orderRepository.delete(menuOrder);
	}
}
