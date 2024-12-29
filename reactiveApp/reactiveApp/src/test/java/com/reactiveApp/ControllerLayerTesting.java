package com.reactiveApp;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.reactiveApp.controller.ProductController;
import com.reactiveApp.model.Product;
import com.reactiveApp.model.ProductWithInventoryDTO;
import com.reactiveApp.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ProductController.class)
class ControllerLayerTesting {

	@MockBean
	private ProductService productService;

	@InjectMocks
	private ProductController productController;
	@Autowired
	private WebTestClient webTestClient;

	private Product p1;
	private Product p2;
	private Product updated_p1;

	private ProductWithInventoryDTO productWithInventoryDTO1;
	private ProductWithInventoryDTO productWithInventoryDTO2;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		// webTestClient =WebTestClient.bindToController(productController).build();
		p1 = new Product("1", "Iphone 13", 12300.89, "Iphone Product");
		p2 = new Product("2", "boAt Airpods", 4500.9, "boAt Product");
		productWithInventoryDTO1 = ProductWithInventoryDTO.builder().product(p1).stockCount(1).stockStatus("IN_STOCK")
				.build();
		productWithInventoryDTO2 = ProductWithInventoryDTO.builder().product(p2).stockCount(0)
				.stockStatus("OUT_OF_STOCK").build();

	}

	@Test
	void addProductTest() {
		when(productService.addProduct(Mockito.any())).thenReturn(Mono.just(p1));

		webTestClient.post().uri("/product").contentType(MediaType.APPLICATION_JSON).bodyValue(p1).exchange()
				.expectStatus().isCreated().expectBody(Product.class).isEqualTo(p1);
		verify(productService, times(1)).addProduct(Mockito.any());
	}

	@Test
	void getAllProductTest() {
		when(productService.getAllProducts()).thenReturn(Flux.just(productWithInventoryDTO1, productWithInventoryDTO2));

		webTestClient.get().uri("/product").exchange().expectStatus().isOk()
				.expectBodyList(ProductWithInventoryDTO.class).hasSize(2)
				.contains(productWithInventoryDTO1, productWithInventoryDTO2);

		verify(productService, times(1)).getAllProducts();
	}

	@Test
	void GetProductByIdTest() {

		when(productService.getProductById(Mockito.anyString())).thenReturn(Mono.just(productWithInventoryDTO1));

		webTestClient.get().uri("/product/1").exchange().expectStatus().isOk().expectBody(ProductWithInventoryDTO.class)
				.isEqualTo(productWithInventoryDTO1);

		verify(productService, times(1)).getProductById(Mockito.anyString());
	}

	@Test
	public void testDeleteProductById() {
		when(productService.deleteById(Mockito.anyString())).thenReturn(Mono.just(productWithInventoryDTO1));

		webTestClient.delete().uri("/product/1").exchange().expectStatus().isOk();
		verify(productService, times(1)).deleteById(Mockito.anyString());
	}

	@Test
	public void testUpdateProductById() {
		// updated_p1 = new Product("1", "Iphone 13 Pro", 3, 24600.0, "Iphone product",
		// "IN_STOCK");
		when(productService.updateProduct(Mockito.any(), Mockito.anyString())).thenReturn(Mono.just(p1));

		webTestClient.put().uri("/product/1").bodyValue(p1).exchange().expectStatus().isCreated()
				.expectBody(Product.class).isEqualTo(p1);

		verify(productService, times(1)).updateProduct(Mockito.any(), Mockito.anyString());
	}
}
