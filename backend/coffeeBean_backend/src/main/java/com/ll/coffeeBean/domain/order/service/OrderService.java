package com.ll.coffeeBean.domain.order.service;

import static com.ll.coffeeBean.domain.order.enums.OrderStatus.READY_FOR_DELIVERY;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.coffeeBean.repository.CoffeeBeanRepository;
import com.ll.coffeeBean.domain.coffeeBean.service.CoffeeBeanService;
import com.ll.coffeeBean.domain.order.dto.BeanNameQuantityDTO;
import com.ll.coffeeBean.domain.order.dto.GetResMenuOrderDto;
import com.ll.coffeeBean.domain.order.dto.PostDetailOrderDto;
import com.ll.coffeeBean.domain.order.dto.PostOrderRequestDto;
import com.ll.coffeeBean.domain.order.dto.PostOrderResponseDto;
import com.ll.coffeeBean.domain.order.dto.PutMenuOrderRqDTO;
import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.entity.PastOrder;
import com.ll.coffeeBean.domain.order.enums.OrderStatus;
import com.ll.coffeeBean.domain.order.repository.DetailOrderRepository;
import com.ll.coffeeBean.domain.order.repository.OrderRepository;
import com.ll.coffeeBean.domain.order.repository.PastOrderRepository;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.SiteUserRepository;
import com.ll.coffeeBean.global.exceptions.ServiceException;
import com.ll.coffeeBean.standard.PageDto.PageDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final DetailOrderRepository detailOrderRepository;
    private final PastOrderRepository pastOrderRepository;

	private final CoffeeBeanService coffeeBeanService;
    private final SiteUserRepository siteUserRepository;
    private final CoffeeBeanRepository coffeeBeanRepository;
    private final SiteUserRepository userRepository;

    public long count() {
        return orderRepository.count();
    }

    //pageable 설정 후 jpa를 유저 정보와 pageable를 이용해 page 데이터 탐색
    public PageDto<GetResMenuOrderDto> getList(SiteUser siteUser, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<MenuOrder> paging = this.orderRepository.findByCustomer(pageable, siteUser);

        Page<GetResMenuOrderDto> pagingOrderDto = paging.map(GetResMenuOrderDto::new);
        PageDto<GetResMenuOrderDto> pageDto = new PageDto<>(pagingOrderDto);
        return pageDto;

    }

    @Transactional
    public MenuOrder create(SiteUser siteUser) {

        MenuOrder menuOrder = new MenuOrder();
        menuOrder.setCustomer(siteUser);
        orderRepository.save(menuOrder);

        return menuOrder;
    }

    /**
     * TODO : 효율적인 스케쥴링 정하기, print -> 로그로 변경하기
     */
    @Transactional
    public void processOrderByScheduling() {
        System.out.println("========================");
        System.out.println("Start Scheduled!!\n\n");

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);

        List<MenuOrder> orders = orderRepository.findByCreateDateGreaterThanEqualAndCreateDateBefore(startDate,
                endDate);

        // 24시간 동안 쌓인 주문 처리
        for (MenuOrder order : orders) {
            processOrder(order);
        }

        System.out.println("\n\n========================");
        System.out.println("End Scheduled!!\n\n");
    }

    /**
     * TODO : PastOrder DB에 DetailOrder 개별로 저장할 것인가, MenuOrder 로 저장할 것인가
     * 주문 처리 (추후 구현) 처리 방법 정의 후 구현 예정
     */
    @Transactional
    public void processOrder(MenuOrder order) {
        /**
         * TODO : 작업 처리, 처리된 작업 및 처리 도중 오류 로깅
         */
        int totalPrice = 0;

        List<DetailOrder> orders = order.getOrders();

        PastOrder pastOrder = PastOrder.builder()
                .customer(order.getCustomer())
                .orderStatus(OrderStatus.DELIVERED)
                .build();

        while (!orders.isEmpty()) {
            DetailOrder detailOrder = orders.getFirst();
            order.removeDetail(detailOrder);
            pastOrder.addDetail(detailOrder);
        }

        // 처리된 작업은 지난 주문 DB에 저장
        pastOrderRepository.save(pastOrder);

        // 모든 작업 처리 후, 기존의 Order DB 모두 삭제
        order.getCustomer().removeOrder(order);
    }

    public Optional<MenuOrder> findById(long id) {
        return orderRepository.findById(id);
    }

    // 주문 수정 비즈니스 로직 (MenuOrder 안에 있는 DetailOrder 수정)
    @Transactional
    public PutMenuOrderRqDTO modify(MenuOrder menuOrder, PutMenuOrderRqDTO reqDetailOrders) {
        // reqBody 에 담겨있는 주문 목록 받기
        List<BeanNameQuantityDTO> reqBeansDTOList = reqDetailOrders.getCoffeeOrders();

        // 받아온 주문 목록의 각 커피콩 주문 별 처리
        for (BeanNameQuantityDTO reqBean : reqBeansDTOList) {
            // reqBean : 수정 원하는 커피콩의 이름과 수량

            CoffeeBean coffeeBean = coffeeBeanService.findByName(reqBean.getName());

            DetailOrder detailOrderToChange = menuOrder.getOrders()
                    .stream()
                    .filter(detailOrder -> detailOrder.getName().equals(coffeeBean.getName()))
                    .findFirst().get();

            // 재고관련 확인 및 처리
            int changeQuantity = reqBean.getQuantity() - detailOrderToChange.getQuantity(); // 신규 - 기존
            coffeeBeanService.changeStockWithValidation(coffeeBean, changeQuantity);

            // 커피콩 주문 수량 변경
            if (reqBean.getQuantity() == 0) {
                // 변경 수량이 0이면 아예 DetailOrder 를 삭제 (수량이 0인 주문은 없도록)
                menuOrder.removeDetail(detailOrderToChange);
                detailOrderRepository.delete(detailOrderToChange);
            } else {
                // 실제 수량 변경 로직
                detailOrderToChange.setQuantity(reqBean.getQuantity());
            }
        }

        // 고객의 현재 주문 상태 DTO 에 담아 반환
        List<BeanNameQuantityDTO> beanNameQuantityDTOList = new ArrayList<>();
        for (DetailOrder beanOrders : menuOrder.getOrders()) {
            BeanNameQuantityDTO beanNameQuantityDto = new BeanNameQuantityDTO(beanOrders.getName(), beanOrders.getQuantity());
            beanNameQuantityDTOList.add(beanNameQuantityDto);
        }
        PutMenuOrderRqDTO orderReqDTO = new PutMenuOrderRqDTO(); // 응답 형식에 따름
        orderReqDTO.setCoffeeOrders(beanNameQuantityDTOList);
        return orderReqDTO;
    }



    @Transactional
	public void deleteOrder(MenuOrder menuOrder) {
        // 커피콩 재고 수량 초기화 (주문 취소)
        for(DetailOrder detailOrder : menuOrder.getOrders()) {
            CoffeeBean coffeeBean = coffeeBeanService.findByName(detailOrder.getName());
            coffeeBeanService.changeStockWithValidation(coffeeBean, -detailOrder.getQuantity());
        }
		orderRepository.delete(menuOrder);
	}



    @Transactional
    public PostOrderResponseDto createOrder(PostOrderRequestDto request) {
        // 회원 정보 확인 및 업데이트
        SiteUser customer = siteUserRepository.findByEmail(request.customer().getEmail())
                .orElseGet(() -> {
                    SiteUser newCustomer = SiteUser
                            .builder()
                            .email(request.customer().getEmail())
                            .build();
                    return siteUserRepository.save(newCustomer); // 새로운 고객 저장
                });

        // 주소 갱신
        customer.setAddress(request.customer().getAddress());
        customer.setPostCode(request.customer().getPostcode());

        // 주문 생성
        MenuOrder menuOrder = MenuOrder
                .builder()
                .orderStatus(READY_FOR_DELIVERY)
                .customer(customer)
                .build();

        customer.getOrders().add(menuOrder);

        int totalPrice = 0;
        List<DetailOrder> detailOrders = new ArrayList<>();

        for (PostDetailOrderDto product : request.products()) {
            // CoffeeBean 조회
            CoffeeBean coffeeBean = coffeeBeanRepository.findById(product.id())
                    .orElseThrow(() -> new ServiceException("404-1", "존재하지 않는 원두입니다."));

            // 재고 확인 및 차감
            coffeeBeanService.changeStockWithValidation(coffeeBean, product.quantity());
            coffeeBeanRepository.save(coffeeBean); // 변경된 재고 저장


            // DetailOrder 생성 및 추가
            DetailOrder detailOrder = DetailOrder
                    .builder()
                    .name(coffeeBean.getName())
                    .price(coffeeBean.getPrice())
                    .quantity(product.quantity())
                    .order(menuOrder)
                    .build();
            detailOrders.add(detailOrder);

            totalPrice += coffeeBean.getPrice() * product.quantity();
        }

        // 주문 저장
        menuOrder.setOrders(detailOrders);
        orderRepository.save(menuOrder);

        // 응답 생성
        return new PostOrderResponseDto(menuOrder.getId(),
                request.customer(),
                request.products(),
                totalPrice,
                menuOrder.getOrderStatus().toString());
    }
}
