package com.reactiveApp;

//import org.mockito.InjectMocks;

//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import com.reactiveApp.clients.ProductServiceClient;
//import com.reactiveApp.model.ProductInventoryDTO;
//
//@SpringBootTest
//class ClientLayerTesting {
//
//	@Mock
//	private WebClient webClient;
//
//	@InjectMocks
//	private ProductServiceClient productServiceClient;
//	
//	private ProductInventoryDTO productInventoryDTO1;
//	private ProductInventoryDTO productInventoryDTO2;
//
//	
//}

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactiveApp.clients.ProductServiceClient;
import com.reactiveApp.model.ProductInventoryDTO;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ClientLayerTesting {
	private static MockWebServer mockwebserver;
	private static ProductServiceClient prodcuctServiceClient;
	private static ProductInventoryDTO inventoryDTO1;
	private static ProductInventoryDTO inventoryDTO2;
	private static ProductInventoryDTO updatedInventory;

	@BeforeAll
	static void setUp() throws IOException {
		mockwebserver = new MockWebServer();
		mockwebserver.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockwebserver.shutdown();
	}

	@BeforeEach
	void initialize() {
		String baseUrl = String.format("http://localhost:%s", mockwebserver.getPort());
		prodcuctServiceClient = new ProductServiceClient(WebClient.builder().baseUrl(baseUrl).build());
		inventoryDTO1 = new ProductInventoryDTO(12, "IN_STOCK", "1");
		inventoryDTO2 = new ProductInventoryDTO(0, "OUT_OF_STOCK", "2");
	}

	@Test
	void getInventoryByProductIdTest() throws Exception {
		// inventoryDTO1 = new ProductInventoryDTO(12, "IN_STOCK", "1");
		ObjectMapper objMapper = new ObjectMapper();
		mockwebserver.enqueue(new MockResponse().setBody(objMapper.writeValueAsString(inventoryDTO1))
				.addHeader("Content-Type", "application/json"));

		Mono<ProductInventoryDTO> inventoryMono = prodcuctServiceClient.getInventoryByProductId("1");

		StepVerifier.create(inventoryMono).expectNextMatches(inventory -> inventory.getProductId().equals("1"))
				.verifyComplete();
	}

	@Test
	void getAllInventoryTest() throws Exception {

		ObjectMapper objMapper = new ObjectMapper();
		mockwebserver.enqueue(
				new MockResponse().setBody(objMapper.writeValueAsString(Arrays.asList(inventoryDTO1, inventoryDTO2)))
						.addHeader("Content-Type", "application/json"));
		Flux<ProductInventoryDTO> inventoryMono = prodcuctServiceClient.getAllInventory();

		StepVerifier.create(inventoryMono).expectNext(inventoryDTO1).expectNext(inventoryDTO2).verifyComplete();
	}

	@Test
	void createInventoryTest() throws Exception {
		ObjectMapper objMapper = new ObjectMapper();

		mockwebserver.enqueue(new MockResponse().setBody(objMapper.writeValueAsString(inventoryDTO1))
				.addHeader("Content-Type", "application/json"));

		Mono<ProductInventoryDTO> inventoryMono = prodcuctServiceClient.createInventory(inventoryDTO1);

		StepVerifier.create(inventoryMono).expectNextMatches(inventory -> inventory.getProductId().equals("1"))
				.verifyComplete();
	}

	@Test
	void updateInventoryTest() throws Exception {
		updatedInventory = new ProductInventoryDTO(32, "IN_STOCK", "1");
		ObjectMapper objMapper = new ObjectMapper();

		mockwebserver.enqueue(new MockResponse().setBody(objMapper.writeValueAsString(updatedInventory))
				.addHeader("Content-Type", "application/json"));

		Mono<ProductInventoryDTO> inventoryMono = prodcuctServiceClient.updateInventory(inventoryDTO1);

		StepVerifier.create(inventoryMono).expectNextMatches(inventory -> inventory.getStockCount() == 32)
				.verifyComplete();
	}

	@Test
	void deleteInventoryTest() throws Exception {
		ObjectMapper objMapper = new ObjectMapper();

		mockwebserver.enqueue(new MockResponse().setBody(objMapper.writeValueAsString(inventoryDTO1))
				.addHeader("Content-Type", "application/json"));

		Mono<ProductInventoryDTO> inventoryMono = prodcuctServiceClient.deleteInventory(inventoryDTO1.getProductId());

		StepVerifier.create(inventoryMono).expectNextMatches(inventory -> inventory.getProductId().equals("1"))
				.verifyComplete();
	}

}
