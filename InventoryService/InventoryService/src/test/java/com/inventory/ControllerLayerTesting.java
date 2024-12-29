package com.inventory;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.inventory.InventoryController.ProductInventoryController;
import com.inventory.InventoryModel.ProductInventory;
import com.inventory.InventoryService.ProductInventoryService;
import com.inventory.Utilities.Status;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ProductInventoryController.class)
class ControllerLayerTesting {

	@MockBean
	private ProductInventoryService productInventoryService;

	@InjectMocks
	private ProductInventoryController productInventoryController;
	@Autowired
	private WebTestClient webTestClient;

	private ProductInventory productInventory1;
	private ProductInventory productInventory2;
	private ProductInventory updated_ProductInventory;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		// webTestClient =WebTestClient.bindToController(productController).build();
		productInventory1 = ProductInventory.builder().id("1").stockCount(1).stockStatus(Status.IN_STOCK)
				.productId("100").build();
		productInventory2 = ProductInventory.builder().id("2").stockCount(0).stockStatus(Status.OUT_OF_STOCK)
				.productId("200").build();
	}

	@Test
	void createProductInventoryTest() {
		when(productInventoryService.createProductInventory(productInventory1))
				.thenReturn(Mono.just(productInventory1));

		webTestClient.post().uri("/productInventory").contentType(MediaType.APPLICATION_JSON)
				.bodyValue(productInventory1).exchange().expectStatus().isCreated().expectBody(ProductInventory.class)
				.isEqualTo(productInventory1);

		verify(productInventoryService, times(1)).createProductInventory(productInventory1);
	}

	@Test
	void getAllProductInventoryTest() {
		when(productInventoryService.getAllProductInventory())
				.thenReturn(Flux.just(productInventory1, productInventory2));

		webTestClient.get().uri("/productInventory").exchange().expectStatus().isOk().expectBodyList(ProductInventory.class)
				.hasSize(2).contains(productInventory1, productInventory2);

		verify(productInventoryService, times(1)).getAllProductInventory();
	}

	@Test
	void getProductInventoryTest() {

		when(productInventoryService.getProductInventory("1")).thenReturn(Mono.just(productInventory1));

		webTestClient.get().uri("/productInventory/1").exchange().expectStatus().isOk()
				.expectBody(ProductInventory.class).isEqualTo(productInventory1);

		verify(productInventoryService, times(1)).getProductInventory("1");
	}

	@Test
	public void deleteProductInventoryTest() {
		when(productInventoryService.deleteProductInventory("1")).thenReturn(Mono.just(productInventory1));

		webTestClient.delete().uri("/productInventory/1").exchange().expectStatus().isOk();
		verify(productInventoryService, times(1)).deleteProductInventory("1");
	}
	

	@Test
	public void updateProductInventoryTest() {
		updated_ProductInventory = ProductInventory.builder().id("1").stockCount(-1).stockStatus(Status.AVAILABLE_SOON)
				.productId("100").build();
		when(productInventoryService.updateProductInventory(updated_ProductInventory, "1")).thenReturn(Mono.just(updated_ProductInventory));

		webTestClient.put().uri("/productInventory/1").bodyValue(updated_ProductInventory).exchange().expectStatus().isCreated()
				.expectBody(ProductInventory.class).isEqualTo(updated_ProductInventory);

		verify(productInventoryService, times(1)).updateProductInventory(updated_ProductInventory, "1");
	}

//product ID
	@Test
	public void deleteInventoryByProductIdTest() {
		when(productInventoryService.deleteInventoryByProductId("100")).thenReturn(Mono.just(productInventory1));
		webTestClient.delete().uri("/productInventory/deleteInventory/100").exchange().expectStatus().isOk();
		verify(productInventoryService, times(1)).deleteInventoryByProductId("100");
	}
	
	@Test
	public void updateByProductIdTest() {
		updated_ProductInventory = ProductInventory.builder().id("1").stockCount(-1).stockStatus(Status.AVAILABLE_SOON)
				.productId("100").build();
		when(productInventoryService.updateByProductId(updated_ProductInventory, "100")).thenReturn(Mono.just(updated_ProductInventory));

		webTestClient.put().uri("/productInventory/byProductId/100").bodyValue(updated_ProductInventory).exchange().expectStatus().isCreated()
				.expectBody(ProductInventory.class).isEqualTo(updated_ProductInventory);

		verify(productInventoryService, times(1)).updateByProductId(updated_ProductInventory, "100");
	}
	
	@Test
	void getByProductIdTest() {

		when(productInventoryService.getProductInventoryById("100")).thenReturn(Mono.just(productInventory1));

		webTestClient.get().uri("/productInventory/get/100").exchange().expectStatus().isOk()
				.expectBody(ProductInventory.class).isEqualTo(productInventory1);

		verify(productInventoryService, times(1)).getProductInventoryById("100");
	}
	
}
