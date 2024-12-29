package com.reactiveApp;

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

import com.reactiveApp.clients.ProductServiceClient;
import com.reactiveApp.model.Product;
import com.reactiveApp.model.ProductInventoryDTO;
import com.reactiveApp.model.ProductWithInventoryDTO;
import com.reactiveApp.repository.ProductRepo;
import com.reactiveApp.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class ServiceLayerTesting {

	@Mock
	private ProductRepo productRepo;
	
	@Mock
	private ProductServiceClient productServiceClient;

	@InjectMocks
	private ProductService productService;

	private Product product1;
	private Product product2;
	private Product product3;

	//private Product updated_product1;
	private ProductWithInventoryDTO productWithInventoryDTO1;
	private ProductWithInventoryDTO productWithInventoryDTO2;
	private ProductWithInventoryDTO productWithInventoryDTO3;
	private ProductInventoryDTO productInventoryDTO1;
	private ProductInventoryDTO productInventoryDTO2;
	private ProductInventoryDTO productInventoryDTO3;

	@BeforeEach
	void setUp() throws Exception {

		MockitoAnnotations.openMocks(this);
		product1 = new Product("1", "Iphone 13", 2.67, "Iphone Product");
		product2 = new Product("2", "boAt Airpods", 5.78, "boAt Product");
		product3 = Product.builder().id("3").productName("Samsung S23").price(89.87).description("Samsung Phone India").build();
		productWithInventoryDTO1 = ProductWithInventoryDTO.builder().product(product1).stockCount(1).stockStatus("IN_STOCK").build(); //new ProductWithInventoryDTO(product1,1,"IN_STOCK");
		productWithInventoryDTO2 = ProductWithInventoryDTO.builder().product(product2).stockCount(0).stockStatus("OUT_OF_STOCK").build();//new ProductWithInventoryDTO(product2,0,"OUT_OF_STOCK");
		productWithInventoryDTO3 = ProductWithInventoryDTO.builder().product(product2).stockCount(-1).stockStatus("AVAILABLE_SOON").build();//new ProductWithInventoryDTO(product2,0,"OUT_OF_STOCK");
		productInventoryDTO1 = new ProductInventoryDTO(1,"IN_STOCK", "1");
		productInventoryDTO2 = new ProductInventoryDTO(0,"OUT_OF_STOCK", "2");
		productInventoryDTO3 = new ProductInventoryDTO(-1,"AVAILABLE_SOON", "2");

	}
		
	@Test
	void getAllProductsTest() {
		when(productRepo.findAll()).thenReturn(Flux.just(product1,product2));
		when(productServiceClient.getAllInventory()).thenReturn(Flux.just(productInventoryDTO1,productInventoryDTO2));
		
		Flux<ProductWithInventoryDTO> allProducts = productService.getAllProducts();
		StepVerifier.create(allProducts).expectNext(productWithInventoryDTO1).expectNext(productWithInventoryDTO2).verifyComplete();
		
		verify(productRepo, times(1)).findAll();
		verify(productServiceClient, times(1)).getAllInventory();
	}
	@Test
	void getProductByIdTest() {
		when(productRepo.findById(Mockito.anyString())).thenReturn(Mono.just(product1));
		when(productServiceClient.getInventoryByProductId(Mockito.anyString())).thenReturn(Mono.just(productInventoryDTO1));
		Mono<ProductWithInventoryDTO> product = productService.getProductById("1");
		StepVerifier.create(product).expectNext(productWithInventoryDTO1).verifyComplete();
		verify(productRepo).findById(Mockito.anyString());

	}

	@Test
	void addProductTest_IN_STOCK() {
		when(productRepo.save(Mockito.any())).thenReturn(Mono.just(product1));
		when(productServiceClient.createInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO1));
		Mono<Product> product = productService.addProduct(productWithInventoryDTO1);
		StepVerifier.create(product).expectNext(product1).verifyComplete();
		verify(productRepo).save(Mockito.any());
		verify(productServiceClient).createInventory(Mockito.any());

	}
	
	@Test
	void addProductTest_OUT_OF_STOCK() {
		when(productRepo.save(Mockito.any())).thenReturn(Mono.just(product2));
		when(productServiceClient.createInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO2));
		Mono<Product> product = productService.addProduct(productWithInventoryDTO2);
		StepVerifier.create(product).expectNext(product2).verifyComplete();
		verify(productRepo).save(Mockito.any());
		verify(productServiceClient).createInventory(Mockito.any());

	}
	@Test
	void addProductTest_AVAILABLE_SOON() {
		when(productRepo.save(Mockito.any())).thenReturn(Mono.just(product3));
		when(productServiceClient.createInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO3));
		Mono<Product> product = productService.addProduct(productWithInventoryDTO3);
		StepVerifier.create(product).expectNext(product3).verifyComplete();
		verify(productRepo).save(Mockito.any());
		verify(productServiceClient).createInventory(Mockito.any());

	}
	@Test
	void updateProductTest_OnSuccess_IN_STOCK() {
		when(productRepo.findById(Mockito.anyString())).thenReturn(Mono.just(product1));
		when(productRepo.save(Mockito.any())).thenReturn(Mono.just(product1));
		when(productServiceClient.updateInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO1));
		Mono<Product> result = productService.updateProduct(productWithInventoryDTO1, "1");
		StepVerifier.create(result).expectNext(product1).verifyComplete();		
		verify(productRepo).findById(Mockito.anyString());
		verify(productRepo).save(Mockito.any());
		verify(productServiceClient).updateInventory(Mockito.any());
	}
	@Test
	void updateProductTest_OnSuccess_OUT_OF_STOCK() {
		when(productRepo.findById(Mockito.anyString())).thenReturn(Mono.just(product2));
		when(productRepo.save(Mockito.any())).thenReturn(Mono.just(product2));
		when(productServiceClient.updateInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO2));
		Mono<Product> result = productService.updateProduct(productWithInventoryDTO2, "2");
		StepVerifier.create(result).expectNext(product2).verifyComplete();		
		verify(productRepo).findById(Mockito.anyString());
		verify(productRepo).save(Mockito.any());
		verify(productServiceClient).updateInventory(Mockito.any());
	}
	@Test
	void updateProductTest_OnSuccess_AVAILABLE_SOON() {
		when(productRepo.findById(Mockito.anyString())).thenReturn(Mono.just(product3));
		when(productRepo.save(Mockito.any())).thenReturn(Mono.just(product3));
		when(productServiceClient.updateInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO3));
		Mono<Product> result = productService.updateProduct(productWithInventoryDTO3, "3");
		StepVerifier.create(result).expectNext(product3).verifyComplete();		
		verify(productRepo).findById(Mockito.anyString());
		verify(productRepo).save(Mockito.any());
		verify(productServiceClient).updateInventory(Mockito.any());
	}

	@Test
	void UpdateProductTest_NotFound() {
		when(productRepo.findById(Mockito.anyString())).thenReturn(Mono.empty());
		Mono<Product> result = productService.updateProduct(productWithInventoryDTO1, "1");
		StepVerifier.create(result).expectNext().verifyComplete();
		verify(productRepo).findById(Mockito.anyString());
		verify(productRepo, never()).save(any(Product.class));
	}

	@Test
	void deleteProductTest_OnSuccess() {
		when(productRepo.findById(Mockito.anyString())).thenReturn(Mono.just(product1));
		when(productRepo.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
		when(productServiceClient.deleteInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO1));
		Mono<ProductWithInventoryDTO> product = productService.deleteById("1");
		StepVerifier.create(product).expectNext().verifyComplete();
		verify(productRepo).findById(Mockito.anyString());
		verify(productRepo).deleteById(Mockito.anyString());
		verify(productServiceClient).deleteInventory(Mockito.any());
	}

	@Test
	void deleteProductTest_NotFound() {
		when(productRepo.findById(Mockito.anyString())).thenReturn(Mono.empty());
		when(productRepo.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
		when(productServiceClient.deleteInventory(Mockito.anyString())).thenReturn(Mono.empty());
		Mono<ProductWithInventoryDTO> result = productService.deleteById("1");
		StepVerifier.create(result).expectNext().verifyComplete();
		verify(productRepo).findById(Mockito.anyString());
		verify(productRepo).deleteById(Mockito.anyString());
		verify(productServiceClient).deleteInventory(Mockito.anyString());
	}
	
}
