package com.ll.coffeeBean.coffeeBean.controller;

import com.ll.coffeeBean.domain.coffeeBean.controller.CoffeeBeanController;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CoffeeBeanControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CoffeeBeanController coffeeBeanController;

	@Test
	@DisplayName("원두 추가")
	void t1() throws Exception{
		ResultActions resultActions = mockMvc
				.perform(
						post("/api/bean")
								.content("""
										{
											"name" : "새로 추가하는 커피콩 이름",
											"price" : 123,
											"quantity" : 50
										}
									
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(CoffeeBeanController.class))
				.andExpect(handler().methodName("createCoffeebean"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.resultCode").value("201-1"))
				.andExpect(jsonPath("$.msg").value("원두가 추가되었습니다."));
	}

}

