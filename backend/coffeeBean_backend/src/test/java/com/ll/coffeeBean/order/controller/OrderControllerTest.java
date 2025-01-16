package com.ll.coffeeBean.order.controller;

import com.ll.coffeeBean.domain.order.controller.OrderController;
import com.ll.coffeeBean.domain.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private OrderService orderService;

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
}