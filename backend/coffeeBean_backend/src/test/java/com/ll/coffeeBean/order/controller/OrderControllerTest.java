package com.ll.coffeeBean.order.controller;

import com.ll.coffeeBean.domain.coffeeBean.entity.CoffeeBean;
import com.ll.coffeeBean.domain.coffeeBean.repository.CoffeeBeanRepository;
import com.ll.coffeeBean.domain.order.controller.OrderController;
import com.ll.coffeeBean.domain.order.entity.DetailOrder;
import com.ll.coffeeBean.domain.order.entity.MenuOrder;
import com.ll.coffeeBean.domain.order.repository.DetailOrderRepository;
import com.ll.coffeeBean.domain.order.repository.OrderRepository;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
	private UserRepository userRepository;
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
		userRepository.deleteAll();

		entityManager.createNativeQuery("ALTER TABLE coffee_bean ALTER COLUMN id RESTART WITH 1").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE menu_order ALTER COLUMN id RESTART WITH 1").executeUpdate();

		SiteUser user1 = new SiteUser();
		user1.setEmail("user1email@naver.com");
		userRepository.save(user1);

		MenuOrder order1 = new MenuOrder();
		order1.setCustomer(user1);

		DetailOrder bean1Order = new DetailOrder("bean1", 1, 1000, order1);
		detailOrderRepository.save(bean1Order);
		DetailOrder bean2Order = new DetailOrder("bean2", 2, 1000, order1);
		detailOrderRepository.save(bean2Order);
		DetailOrder bean3Order = new DetailOrder("bean3", 3, 1000, order1);
		detailOrderRepository.save(bean3Order);

		List<DetailOrder> orderList = new ArrayList<>();
		orderList.add(bean1Order);
		orderList.add(bean2Order);
		orderList.add(bean3Order);
		order1.setOrders(orderList);

		orderRepository.save(order1);

		// 콩 재고 50 에서 시작
		// 각 콩별로 1, 2, 3개씩 주문 들어감
		CoffeeBean bean1 = new CoffeeBean("bean1", 1000, 49);
		coffeeBeanRepository.save(bean1);
		System.out.println(bean1.getId());
		CoffeeBean bean2 = new CoffeeBean("bean2", 1000, 48);
		coffeeBeanRepository.save(bean2);
		System.out.println(bean2.getId());
		CoffeeBean bean3 = new CoffeeBean("bean3", 1000, 47);
		coffeeBeanRepository.save(bean3);
		System.out.println(bean3.getId());
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
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.resultCode").value("409"))
				.andExpect(jsonPath("$.msg").value("재고가 부족합니다.ㅜㅜㅜ"));
	}

	@Test
	@DisplayName("DELETE 요청 처리")
	void t4() throws Exception {
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
}