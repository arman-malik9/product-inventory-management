package com.inventory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.inventory.InventoryModel.ProductInventory;
import com.inventory.InventoryRepository.ProductInventoryRepo;
import com.inventory.InventoryService.ProductInventoryService;
import com.inventory.Utilities.Status;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class ServiceLayerTesting {
	@Mock
	private ProductInventoryRepo productInventoryRepo;

	@InjectMocks
	private ProductInventoryService productInventoryService;

	private ProductInventory productInventory1;
	private ProductInventory productInventory2;
	private ProductInventory updated_ProductInventory;

	@BeforeEach
	void setUp() throws Exception {

		MockitoAnnotations.openMocks(this);
		productInventory1 = ProductInventory.builder().id("1").stockCount(1).stockStatus(Status.IN_STOCK)
				.productId("100").build();
		productInventory2 = ProductInventory.builder().id("2").stockCount(0).stockStatus(Status.OUT_OF_STOCK)
				.productId("200").build();
	}

	@Test
	void getAllProductInventoryTest() {

		when(productInventoryRepo.findAll()).thenReturn(Flux.just(productInventory1, productInventory2));
		Flux<ProductInventory> allProductInventory = productInventoryService.getAllProductInventory(); // Service call

		allProductInventory.subscribe(System.out::println);
		StepVerifier.create(allProductInventory) // Used to create TestCase scenario for Unit Test
				.expectNext(productInventory1).expectNext(productInventory2).verifyComplete();
		verify(productInventoryRepo, times(1)).findAll();

	}

	@Test
	void getProductInventoryTest() {

		when(productInventoryRepo.findById("1")).thenReturn(Mono.just(productInventory1));
		Mono<ProductInventory> productInventory = productInventoryService.getProductInventory("1"); // Service call

		StepVerifier.create(productInventory) // Used to create TestCase scenario for Unit Test
				.expectNext(productInventory1).verifyComplete();
		verify(productInventoryRepo, times(1)).findById("1");

	}

	@Test
	void createProductInventoryTest_IN_STOCK() {
		ProductInventory productInventory = ProductInventory.builder().id("1").stockCount(10).productId("1000").build();
		ProductInventory expectedInventory = ProductInventory.builder().id("1").stockCount(10)
				.stockStatus(Status.IN_STOCK).productId("1000").build();
		when(productInventoryRepo.save(Mockito.any())).thenReturn(Mono.just(expectedInventory));
		Mono<ProductInventory> createdInventory = productInventoryService.createProductInventory(productInventory);
		StepVerifier.create(createdInventory).expectNext(expectedInventory).verifyComplete();
		verify(productInventoryRepo).save(Mockito.any());
	}

	@Test
	void createProductInventoryTest_OUT_OF_STOCK() {
		ProductInventory productInventory = ProductInventory.builder().productId("1").stockCount(0).build();

		ProductInventory expectedInventory = ProductInventory.builder().productId("1").stockCount(10)
				.stockStatus(Status.OUT_OF_STOCK).build();
		when(productInventoryRepo.save(Mockito.any())).thenReturn(Mono.just(expectedInventory));
		Mono<ProductInventory> createdInventory = productInventoryService.createProductInventory(productInventory);
		StepVerifier.create(createdInventory).expectNext(expectedInventory).verifyComplete();
		verify(productInventoryRepo).save(Mockito.any());
	}

	@Test
	void createProductInventoryTest_AVAILABLE_SOON() {
		ProductInventory productInventory = ProductInventory.builder().productId("1").stockCount(-1).build();

		ProductInventory expectedInventory = ProductInventory.builder().productId("1").stockCount(10)
				.stockStatus(Status.AVAILABLE_SOON).build();
		when(productInventoryRepo.save(Mockito.any())).thenReturn(Mono.just(expectedInventory));
		Mono<ProductInventory> createdInventory = productInventoryService.createProductInventory(productInventory);
		StepVerifier.create(createdInventory).expectNext(expectedInventory).verifyComplete();
		verify(productInventoryRepo).save(Mockito.any());
	}
	
	@Test
	void updateProductInventoryTest_IN_STOCK() {
		updated_ProductInventory = ProductInventory.builder().id("1").stockCount(1).stockStatus(Status.IN_STOCK)
				.productId("100").build();
		when(productInventoryRepo.findById(Mockito.anyString())).thenReturn(Mono.just(productInventory1));
		when(productInventoryRepo.save(Mockito.any())).thenReturn(Mono.just(productInventory1));
		Mono<ProductInventory> result = productInventoryService.updateProductInventory(updated_ProductInventory, "1");
		StepVerifier.create(result).expectNext(updated_ProductInventory).verifyComplete();
		// .expectNextMatches(Mono.just(product1)).verifyComplete();

		verify(productInventoryRepo).save(Mockito.any());
	}
	@Test
	void updateProductInventoryTest_OUT_OF_STOCK() {
		updated_ProductInventory = ProductInventory.builder().id("1").stockCount(0).stockStatus(Status.OUT_OF_STOCK)
				.productId("100").build();
		when(productInventoryRepo.findById(Mockito.anyString())).thenReturn(Mono.just(productInventory1));
		when(productInventoryRepo.save(Mockito.any())).thenReturn(Mono.just(productInventory1));
		Mono<ProductInventory> result = productInventoryService.updateProductInventory(updated_ProductInventory, "1");
		StepVerifier.create(result).expectNext(updated_ProductInventory).verifyComplete();
		// .expectNextMatches(Mono.just(product1)).verifyComplete();

		verify(productInventoryRepo).save(Mockito.any());
	}
	@Test
	void updateProductInventoryTest_AVAILABLE_SOON() {
		updated_ProductInventory = ProductInventory.builder().id("1").stockCount(-1).stockStatus(Status.AVAILABLE_SOON)
				.productId("100").build();
		when(productInventoryRepo.findById(Mockito.anyString())).thenReturn(Mono.just(productInventory1));
		when(productInventoryRepo.save(Mockito.any())).thenReturn(Mono.just(productInventory1));
		Mono<ProductInventory> result = productInventoryService.updateProductInventory(updated_ProductInventory, "1");
		StepVerifier.create(result).expectNext(updated_ProductInventory).verifyComplete();
		// .expectNextMatches(Mono.just(product1)).verifyComplete();

		verify(productInventoryRepo).save(Mockito.any());
	}
	

	@Test
	void updateProductInventoryTest_NotFound() {

		when(productInventoryRepo.findById(Mockito.anyString())).thenReturn(Mono.empty());
		Mono<ProductInventory> result = productInventoryService.updateProductInventory(updated_ProductInventory, "1");
		StepVerifier.create(result).expectNext().verifyComplete();
		verify(productInventoryRepo).findById(Mockito.anyString());
		verify(productInventoryRepo, never()).save(any(ProductInventory.class));
	}

	@Test
	void deleteProductInventoryTest_OnSuccess() {
		when(productInventoryRepo.findById("1")).thenReturn(Mono.just(productInventory1));
		when(productInventoryRepo.deleteById("1")).thenReturn(Mono.empty());
		Mono<ProductInventory> productInventory = productInventoryService.deleteProductInventory("1");
		StepVerifier.create(productInventory).expectNext(productInventory1).verifyComplete();
		verify(productInventoryRepo).deleteById("1");
	}

	@Test
	void deleteProductInventoryTest_NotFound() {
		when(productInventoryRepo.findById("1")).thenReturn(Mono.empty());
		Mono<ProductInventory> result = productInventoryService.deleteProductInventory("1");
		StepVerifier.create(result).expectNext().verifyComplete();
		verify(productInventoryRepo, never()).deleteById("1");
	}

	// productID testCases
	
	@Test
	void updateByProductIdTest_IN_STOCK() {
		updated_ProductInventory = ProductInventory.builder().id("1").stockCount(1).stockStatus(Status.IN_STOCK)
				.productId("100").build();
		when(productInventoryRepo.findByProductId(Mockito.anyString())).thenReturn(Mono.just(productInventory1));
		when(productInventoryRepo.save(Mockito.any())).thenReturn(Mono.just(productInventory1));
		Mono<ProductInventory> result = productInventoryService.updateByProductId(updated_ProductInventory, "100");
		StepVerifier.create(result).expectNext(updated_ProductInventory).verifyComplete();
		// .expectNextMatches(Mono.just(product1)).verifyComplete();

		verify(productInventoryRepo).save(Mockito.any());
	}
	@Test
	void updateByProductIdTest_OUT_OF_STOCK() {
		updated_ProductInventory = ProductInventory.builder().id("1").stockCount(0).stockStatus(Status.OUT_OF_STOCK)
				.productId("100").build();
		when(productInventoryRepo.findByProductId(Mockito.anyString())).thenReturn(Mono.just(productInventory1));
		when(productInventoryRepo.save(Mockito.any())).thenReturn(Mono.just(productInventory1));
		Mono<ProductInventory> result = productInventoryService.updateByProductId(updated_ProductInventory, "100");
		StepVerifier.create(result).expectNext(updated_ProductInventory).verifyComplete();
		// .expectNextMatches(Mono.just(product1)).verifyComplete();

		verify(productInventoryRepo).save(Mockito.any());
	}

	@Test
	void updateByProductIdTest_AVAILABLE_SOON() {
		updated_ProductInventory = ProductInventory.builder().id("1").stockCount(-1).stockStatus(Status.AVAILABLE_SOON)
				.productId("100").build();
		when(productInventoryRepo.findByProductId(Mockito.anyString())).thenReturn(Mono.just(productInventory1));
		when(productInventoryRepo.save(Mockito.any())).thenReturn(Mono.just(productInventory1));
		Mono<ProductInventory> result = productInventoryService.updateByProductId(updated_ProductInventory, "100");
		StepVerifier.create(result).expectNext(updated_ProductInventory).verifyComplete();
		// .expectNextMatches(Mono.just(product1)).verifyComplete();

		verify(productInventoryRepo).save(Mockito.any());
	}

	@Test
	void updateByProductIdTest_NotFound() {

		when(productInventoryRepo.findByProductId(Mockito.anyString())).thenReturn(Mono.empty());
		Mono<ProductInventory> result = productInventoryService.updateByProductId(updated_ProductInventory, "100");
		StepVerifier.create(result).expectNext().verifyComplete();
		verify(productInventoryRepo).findByProductId(Mockito.anyString());
		verify(productInventoryRepo, never()).save(any(ProductInventory.class));
	}

	@Test
	void deleteInventoryByProductId_OnSuccess() {
		when(productInventoryRepo.findByProductId(Mockito.anyString())).thenReturn(Mono.just(productInventory1));
		when(productInventoryRepo.deleteByProductId(Mockito.anyString())).thenReturn(Mono.just(productInventory1));
		Mono<ProductInventory> productInventory = productInventoryService.deleteInventoryByProductId("100");
		StepVerifier.create(productInventory).expectNext(productInventory1).verifyComplete();
		verify(productInventoryRepo).deleteByProductId(Mockito.anyString());
	}

	@Test
	void deleteInventoryByProductId_NotFound() {
		when(productInventoryRepo.findByProductId(Mockito.anyString())).thenReturn(Mono.empty());
		when(productInventoryRepo.deleteByProductId(Mockito.anyString())).thenReturn(Mono.empty());
		Mono<ProductInventory> result = productInventoryService.deleteInventoryByProductId("1000");
		StepVerifier.create(result).expectNext().verifyComplete();
		verify(productInventoryRepo).deleteByProductId(Mockito.anyString());
	}

	@Test
	void getProductInventoryByIdTest() {

		when(productInventoryRepo.findByProductId("100")).thenReturn(Mono.just(productInventory1));
		Mono<ProductInventory> productInventory = productInventoryService.getProductInventoryById("100"); // Service call
		StepVerifier.create(productInventory) // Used to create TestCase scenario for Unit Test
				.expectNext(productInventory1).verifyComplete();
		verify(productInventoryRepo, times(1)).findByProductId("100");

	}

}
