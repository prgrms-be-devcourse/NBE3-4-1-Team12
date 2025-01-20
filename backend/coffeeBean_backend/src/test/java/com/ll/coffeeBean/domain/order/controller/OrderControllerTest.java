package com.ll.coffeeBean.domain.order.controller;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.coffeeBean.repository.CoffeeBeanRepository;
import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.repository.DetailOrderRepository;
import com.ll.coffeeBean.domain.order.repository.OrderRepository;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.SiteUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SiteUserRepository siteUserRepository;
	@Autowired
	private DetailOrderRepository detailOrderRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CoffeeBeanRepository coffeeBeanRepository;

	@PersistenceContext
	private EntityManager entityManager;

	// 테스트 데이터 만들기
	@BeforeEach
	void beforeEach() {
		// 이전 AUTO_INCREMENT 초기화
		coffeeBeanRepository.deleteAll();
		orderRepository.deleteAll();
		detailOrderRepository.deleteAll();
		siteUserRepository.deleteAll();

		entityManager.createNativeQuery("ALTER TABLE coffee_bean ALTER COLUMN id RESTART WITH 1").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE menu_order ALTER COLUMN id RESTART WITH 1").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE detail_order ALTER COLUMN id RESTART WITH 1").executeUpdate();

		SiteUser user1 = new SiteUser();
		user1.setEmail("user1email@naver.com");
		siteUserRepository.save(user1);

		MenuOrder order1 = new MenuOrder();
		order1.setCustomer(user1);

        DetailOrder bean1Order = DetailOrder.builder()
                .name("bean1")
                .quantity(1)
                .price(1000)
                .build();
        order1.addDetail(bean1Order);

        DetailOrder bean2Order = DetailOrder.builder()
                .name("bean2")
                .quantity(1)
                .price(1000)
                .build();
        order1.addDetail(bean2Order);

        DetailOrder bean3Order = DetailOrder.builder()
                .name("bean2")
                .quantity(1)
                .price(1000)
                .build();

        order1.addDetail(bean3Order);

//		DetailOrder bean1Order = new DetailOrder("bean1", 1, 1000, order1);
//		detailOrderRepository.save(bean1Order);
//		DetailOrder bean2Order = new DetailOrder("bean2", 2, 1000, order1);
//		detailOrderRepository.save(bean2Order);
//		DetailOrder bean3Order = new DetailOrder("bean3", 3, 1000, order1);
//		detailOrderRepository.save(bean3Order);
//
//		List<DetailOrder> orderList = new ArrayList<>();
//		orderList.add(bean1Order);
//		orderList.add(bean2Order);
//		orderList.add(bean3Order);
//		order1.setOrders(orderList);

		orderRepository.save(order1);

		// 콩 재고 50 에서 시작
		// 각 콩별로 1, 2, 3개씩 주문 들어감
		CoffeeBean bean1 = new CoffeeBean("bean1", 1000, 49);
		coffeeBeanRepository.save(bean1);
		CoffeeBean bean2 = new CoffeeBean("bean2", 1000, 48);
		coffeeBeanRepository.save(bean2);
		CoffeeBean bean3 = new CoffeeBean("bean3", 1000, 47);
		coffeeBeanRepository.save(bean3);
	}

	@Test
	@DisplayName("PUT 요청 처리")
	void t1() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						put("/api/order/1")
								.content("""
										{
										   "coffeeOrders": [
										     {
										       "id": 1, "quantity": 5
										     },
										     {
										       "id": 2, "quantity": 3
										     }
										   ]
										}
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("modifyOrder"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value("200-1"))
				.andExpect(jsonPath("$.msg").value("1번 주문이 수정되었습니다."));
	}

	@Test
	@DisplayName("PUT 요청 처리 : 변경 수량이 0 인 경우")
	void t2() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						put("/api/order/1")
								.content("""
										{
										   "coffeeOrders": [
										     {
										       "id": 1, "quantity": 0
										     },
										     {
										       "id": 2, "quantity": 3
										     }
										   ]
										}
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("modifyOrder"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value("200-1"))
				.andExpect(jsonPath("$.msg").value("1번 주문이 수정되었습니다."));
	}

	@Test
	@DisplayName("PUT 요청 처리 : 너무 많이 시킴, 재고 없음 ㅠㅠ")
	void t3() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						put("/api/order/1")
								.content("""
										{
										   "coffeeOrders": [
										     {
										       "id": 1, "quantity": 100
										     },
										     {
										       "id": 2, "quantity": 3
										     }
										   ]
										}
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("modifyOrder"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value(matchesPattern(".*재고가 부족합니다.*")));
	}

	@Test
	@DisplayName("PUT 요청 처리 : 잘못 된 값 입력 (수량 음수 입력)")
	void t4() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						put("/api/order/1")
								.content("""
										{
										   "coffeeOrders": [
										     {
										       "id": 1, "quantity": -5
										     },
										     {
										       "id": 2, "quantity": 3
										     }
										   ]
										}
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("modifyOrder"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value(matchesPattern(".*주문 수량은 0 이상이어야 합니다.*")));

	}

	@Test
	@DisplayName("DELETE 요청 처리")
	void t5() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						delete("/api/order/1")
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("deleteOrder"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value("200-1"))
				.andExpect(jsonPath("$.msg").value("1번 주문이 삭제되었습니다."));
	}

	@Test
	@DisplayName("POST 주문")
	void t6() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						post("/api/order")
								.content("""
										{
										     "customer": {
										             "email": "test@test.com",
										             "address": "서울 어쩌구",
										             "postcode": "12345"
										     },
										     "products": [
										         {
										             "id": 1,
										             "quantity": 2
										         },
										         {
										             "id": 2,
										             "quantity": 3
										         }
										     ]
										 }
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		SiteUser user = siteUserRepository.findByEmail("test@test.com").get();
		assertThat(user.getOrders()).isNotEmpty();
		user.getOrders().forEach(order -> {
			assertThat(order.getCustomer().getEmail()).isEqualTo("test@test.com");
		});

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("createOrder"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.resultCode").value("201-1"))
				.andExpect(jsonPath("$.msg").value("주문이 완료되었습니다."))
				.andExpect(jsonPath("$.data.orderId").value(2))
				.andExpect(jsonPath("$.data.customer.email").value("test@test.com"))
				.andExpect(jsonPath("$.data.customer.address").value("서울 어쩌구"))
				.andExpect(jsonPath("$.data.customer.postcode").value("12345"))
				.andExpect(jsonPath("$.data.products[0].id").value(1))
				.andExpect(jsonPath("$.data.products[0].quantity").value(2))
				.andExpect(jsonPath("$.data.products[1].id").value(2))
				.andExpect(jsonPath("$.data.products[1].quantity").value(3))
				.andExpect(jsonPath("$.data.totalPrice").value(5000))
				.andExpect(jsonPath("$.data.status").value("READY_FOR_DELIVERY"));
	}

	@Test
	@DisplayName("POST 주문 : 재고 부족")
	void t7() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						post("/api/order")
								.content("""
										{
										     "customer": {
										             "email": "test@test.com",
										             "address": "서울 어쩌구",
										             "postcode": "12345"
										     },
										     "products": [
										         {
										             "id": 1,
										             "quantity": 50
										         }
										     ]
										 }
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("createOrder"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value("재고가 부족합니다. 남은 재고: 49"));
	}

	@Test
	@DisplayName("POST 주문 : 회원 postcode 누락")
	void t8() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						post("/api/order")
								.content("""
										{
										     "customer": {
										             "email": "test@test.com",
										             "address": "서울 어쩌구",
										             "postcode": ""
										     },
										     "products": [
										         {
										             "id": 1,
										             "quantity": 1
										         }
										     ]
										 }
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("createOrder"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value("customer.postcode-NotBlank-우편번호는 필수 입력 항목입니다."));
	}

	@Test
	@DisplayName("POST 주문 : 회원 email 누락")
	void t9() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						post("/api/order")
								.content("""
										{
										     "customer": {
										             "email": "",
										             "address": "서울 어쩌구",
										             "postcode": "12313"
										     },
										     "products": [
										         {
										             "id": 1,
										             "quantity": 1
										         }
										     ]
										 }
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("createOrder"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value("customer.email-NotBlank-이메일은 필수 입력 항목입니다."));
	}

	@Test
	@DisplayName("POST 주문 : 회원 email 형식 오류")
	void t10() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						post("/api/order")
								.content("""
										{
										     "customer": {
										             "email": "adsasd.asdasd",
										             "address": "서울 어쩌구",
										             "postcode": "12313"
										     },
										     "products": [
										         {
										             "id": 1,
										             "quantity": 1
										         }
										     ]
										 }
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("createOrder"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value("customer.email-Email-올바른 이메일 주소를 입력해야 합니다."));
	}

	@Test
	@DisplayName("POST 주문 : 주문 상품 없음")
	void t11() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						post("/api/order")
								.content("""
										{
										     "customer": {
										             "email": "test@test.com",
										             "address": "서울 어쩌구",
										             "postcode": "12313"
										     },
										     "products": [
										     ]
										 }
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("createOrder"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value("products-Size-최소 한 개 이상이어야 합니다."));
	}

	@Test
	@DisplayName("POST 주문 : 주문 수량 음수")
	void t12() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(
						post("/api/order")
								.content("""
										{
										     "customer": {
										             "email": "test@test.com",
										             "address": "서울 어쩌구",
										             "postcode": "12313"
										     },
										     "products": [
										         {
										             "id": 1,
										             "quantity": -1
										         }
										     ]
										 }
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(OrderController.class))
				.andExpect(handler().methodName("createOrder"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value("products[0].quantity-Min-1개 이상 주문해야 합니다."));
	}
}