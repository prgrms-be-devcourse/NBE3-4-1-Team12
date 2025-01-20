package com.ll.coffeeBean.coffeeBean.controller;

import com.ll.coffeeBean.domain.coffeeBean.controller.CoffeeBeanController;
import com.ll.coffeeBean.domain.coffeeBean.dto.CoffeeBeanRequestDTO;
import com.ll.coffeeBean.domain.coffeeBean.dto.CoffeeBeanResponseDTO;
import com.ll.coffeeBean.domain.coffeeBean.service.CoffeeBeanService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
	private CoffeeBeanService coffeeBeanService;

	@Test
	@DisplayName("원두 추가")
	void post1() throws Exception{
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
				.andExpect(handler().methodName("createCoffeeBean"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.resultCode").value("201-1"))
				.andExpect(jsonPath("$.msg").value("원두가 추가되었습니다."));
	}


	@Test
	@DisplayName("원두 추가 이름이 중복되는 경우")
	void post2() throws Exception{
		CoffeeBeanRequestDTO reqBody = new CoffeeBeanRequestDTO("새로 추가하는 커피콩 이름", 123, 50);
		coffeeBeanService.createCoffeeBean(reqBody);

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
				.andExpect(handler().methodName("createCoffeeBean"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value("이미 존재하는 원두입니다."));
	}

	@Test
	@DisplayName("원두 수정")
	void put1() throws Exception{
		// 커피콩 생성 (생성된 커피콩의 id 사용해서 PUT 요청)
		CoffeeBeanRequestDTO reqBody = new CoffeeBeanRequestDTO("커피콩1", 123, 50);
		CoffeeBeanResponseDTO createdBean = coffeeBeanService.createCoffeeBean(reqBody);

		ResultActions resultActions = mockMvc
				.perform(
						put("/api/bean/%d".formatted(createdBean.getId()))
								.content("""
										{
											"price" : 1234,
											"quantity" : 60
										}
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(CoffeeBeanController.class))
				.andExpect(handler().methodName("modifyCoffeeBean"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value("200-1"))
				.andExpect(jsonPath("$.msg").value("원두가 수정되었습니다."))
				.andExpect(jsonPath("$.data.id").value(createdBean.getId()))
				.andExpect(jsonPath("$.data.name").value(createdBean.getName()))
				.andExpect(jsonPath("$.data.price").value(1234))
				.andExpect(jsonPath("$.data.quantity").value(60));
	}

	@Test
	@DisplayName("원두 수정 : 가격 입력 안됨")
	void put2() throws Exception{
		// 커피콩 생성 (생성된 커피콩의 id 사용해서 PUT 요청)
		CoffeeBeanRequestDTO reqBody = new CoffeeBeanRequestDTO("커피콩1", 123, 50);
		CoffeeBeanResponseDTO createdBean = coffeeBeanService.createCoffeeBean(reqBody);

		ResultActions resultActions = mockMvc
				.perform(
						put("/api/bean/%d".formatted(createdBean.getId()))
								.content("""
										{
											"quantity" : 60
										}
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(CoffeeBeanController.class))
				.andExpect(handler().methodName("modifyCoffeeBean"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value("price-NotNull-must not be null"));
	}

	@Test
	@DisplayName("원두 수정 : 수량 적게 입력됨")
	void put3() throws Exception{
		// 커피콩 생성 (생성된 커피콩의 id 사용해서 PUT 요청)
		CoffeeBeanRequestDTO reqBody = new CoffeeBeanRequestDTO("커피콩1", 123, 50);
		CoffeeBeanResponseDTO createdBean = coffeeBeanService.createCoffeeBean(reqBody);

		ResultActions resultActions = mockMvc
				.perform(
						put("/api/bean/%d".formatted(createdBean.getId()))
								.content("""
										{
											"price" : 1234,
											"quantity" : 40
										}
										""".stripIndent())
								.contentType(
										new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
				)
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(CoffeeBeanController.class))
				.andExpect(handler().methodName("modifyCoffeeBean"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.resultCode").value("400-1"))
				.andExpect(jsonPath("$.msg").value("수정할 수량이 기존 수량보다 작습니다."));
	}

}

