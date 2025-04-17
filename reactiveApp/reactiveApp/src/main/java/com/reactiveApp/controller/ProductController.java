package com.reactiveApp.controller;
/**
 * This class provides the rest APIs(end points) for certain operations
 * @author arman_a
 * @return_type_of_endpoints JSON objects
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.reactiveApp.model.Product;
import com.reactiveApp.model.ProductWithInventoryDTO;
import com.reactiveApp.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductController {

	@Autowired
	ProductService productService;

	@PostMapping("/product")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Product> addProduct(@RequestBody ProductWithInventoryDTO product) {
		return productService.addProduct(product);
	}

	@GetMapping("/product")
	@ResponseStatus(HttpStatus.OK)
	public Flux<ProductWithInventoryDTO> getAllProducts() {
		return productService.getAllProducts();

	}

	@GetMapping("/product/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Cacheable(value = "product", key = "#id", condition = "#id!=null")
	public Mono<ProductWithInventoryDTO> getProductById(@PathVariable("id") String id) {
		return productService.getProductById(id);

	}

	@Hidden
	@DeleteMapping("/product/{id}")
	@ResponseStatus(HttpStatus.OK)
	@CacheEvict(value = "product", key = "#id", condition = "#id!=null")
	public Mono<ProductWithInventoryDTO> deleteById(@PathVariable("id") String id) {
		return productService.deleteById(id);

	}

	@PutMapping("/product/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	@CachePut(value = "product", key = "#a1")
	public Mono<Product> updateProduct(@RequestBody ProductWithInventoryDTO updateProduct, @PathVariable("id") String id) {
		return productService.updateProduct(updateProduct, id);

	}
	

}
