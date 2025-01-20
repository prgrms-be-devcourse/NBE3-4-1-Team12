package com.ll.coffeeBean.domain.order.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.SiteUserRepository;
import java.nio.charset.StandardCharsets;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SiteUserRepository siteUserRepository;


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
										       "name": "Columbia Nariñó", "quantity": 5
										     },
										     {
										       "name": "Brazil Serra Do Caparao", "quantity": 3
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
										       "name": "Columbia Nariñó", "quantity": 0
										     },
										     {
										       "name": "Brazil Serra Do Caparao", "quantity": 3
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
										       "name": "Columbia Nariñó", "quantity": 100
										     },
										     {
										       "name": "Brazil Serra Do Caparao", "quantity": 3
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
										       "name": "Columbia Nariñó", "quantity": -5
										     },
										     {
										       "name": "Brazil Serra Do Caparao", "quantity": 32
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
				.andExpect(jsonPath("$.data.orderId").value(4))
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