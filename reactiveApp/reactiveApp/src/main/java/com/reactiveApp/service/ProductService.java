package com.reactiveApp.service;

/**
 * Product Service Class which has some necessary methods like create/update/delete/get 
 * @author arman_a
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reactiveApp.clients.ProductServiceClient;
import com.reactiveApp.model.Product;
import com.reactiveApp.model.ProductInventoryDTO;
import com.reactiveApp.model.ProductWithInventoryDTO;
import com.reactiveApp.repository.ProductRepo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j // used for logging
public class ProductService {

	// for persisting the data into database.
	@Autowired
	private ProductRepo productRepo;

	// client call for interacting with the Inventory service
	@Autowired
	private ProductServiceClient client;

	// for handling the data of Inventory Service
	ProductInventoryDTO dto = new ProductInventoryDTO();

	/**
	 * Adding the product and inventory details of that product.
	 * 
	 * @param product
	 * @return combine result of product and its inventory details.
	 */
	public Mono<Product> addProduct(ProductWithInventoryDTO product) {
		Product objProduct = Product.builder().productName(product.getProduct().getProductName())
				.price(product.getProduct().getPrice()).description(product.getProduct().getDescription()).build();

		return productRepo.save(objProduct).flatMap(addedProduct -> {
			log.info(addedProduct.getProductName() + " added into database.");
			if (product.getStockCount() > 0) {
				dto = ProductInventoryDTO.builder()
						.stockCount(product.getStockCount()).stockStatus("IN_STOCK").productId(addedProduct.getId())
						.build();
			} else if (product.getStockCount() == 0) {
				dto = ProductInventoryDTO.builder()
						.stockCount(product.getStockCount()).stockStatus("OUT_OF_STOCK").productId(addedProduct.getId())
						.build();
			} else {
				dto = ProductInventoryDTO.builder()
						.stockCount(product.getStockCount()).stockStatus("AVAILABLE_SOON")
						.productId(addedProduct.getId()).build();
			}
			return client.createInventory(dto).flatMap(produtDTO -> {
				 log.info("Inventory of product of "+ addedProduct.getId() + "is created successfully.");
				return Mono.just(addedProduct);
			});
		}).switchIfEmpty(Mono.empty());
	}

	/**
	 * Fetch the all products details
	 * @param not required
	 * @return combine result of all products and their inventory details.
	 */
	public Flux<ProductWithInventoryDTO> getAllProducts() {
		Flux<ProductWithInventoryDTO> fluxprod = Flux.zip(productRepo.findAll(), client.getAllInventory())
				.flatMap(tuple -> {
					Product product = tuple.getT1();
					ProductInventoryDTO inventory = tuple.getT2();
					if (inventory.getProductId().equals(product.getId())) {
						return Flux.just(ProductWithInventoryDTO.builder().product(product)
								.stockCount(inventory.getStockCount()).stockStatus(inventory.getStockStatus())
								.build());
					} else {
						return Flux.empty();
					}
				});
		return fluxprod;
	}

	/**
	 * Fetch the specific product details
	 * 
	 * @param id
	 * @return combine result of specific product and its inventory details.
	 */
	@Cacheable(value = "product")
	public Mono<ProductWithInventoryDTO> getProductById(String id) {

		Mono<ProductWithInventoryDTO> prodDTO = Mono.zip(productRepo.findById(id), client.getInventoryByProductId(id))
				.flatMap(tuple -> {
					Product prod = tuple.getT1();
					ProductInventoryDTO inventory = tuple.getT2();
					return Mono.just(ProductWithInventoryDTO.builder().product(prod)
							.stockCount(inventory.getStockCount()).stockStatus(inventory.getStockStatus()).build());
				});
		System.out.println(prodDTO);
		return prodDTO.switchIfEmpty(Mono.empty());

	}

	/**
	 * delete the product details and its inventory simultaneously
	 * 
	 * @param id
	 * @return deleted product
	 */
	@CacheEvict(value = "product")
	public Mono<ProductWithInventoryDTO> deleteById(String id) {
		Mono<ProductWithInventoryDTO> prodDTO = Mono
				.zip(productRepo.findById(id), client.deleteInventory(id), productRepo.deleteById(id))
				.flatMap(tuple -> {
					Product prod = tuple.getT1();
					ProductInventoryDTO inventory = tuple.getT2();
					if (prod != null) {
						return Mono.just(ProductWithInventoryDTO.builder().product(prod)
								.stockCount(inventory.getStockCount()).stockStatus(inventory.getStockStatus()).build());
					} else {
						return Mono.empty();
					}
				});

		return prodDTO;
	}

	/**
	 * update the product details and its inventory simultaneously
	 * 
	 * @param updateProduct
	 * @param id
	 * @return updated product
	 */
	@CachePut(value = "product",key = "#a1")
	public Mono<Product> updateProduct(ProductWithInventoryDTO updateProduct, String id) {

		return productRepo.findById(id).flatMap(existingProduct -> {

			Product exitsProduct = Product.builder().id(existingProduct.getId())
					.productName(updateProduct.getProduct().getProductName())
					.price(updateProduct.getProduct().getPrice())
					.description(updateProduct.getProduct().getDescription()).build();

			return productRepo.save(exitsProduct);
		}).flatMap(updatedProduct -> {
			log.info("Product with id " + id + " updated.");

			if (updateProduct.getStockCount() > 0) {
				dto = ProductInventoryDTO.builder().stockStatus("IN_STOCK").stockCount(updateProduct.getStockCount())
						.productId(updatedProduct.getId()).build();
			} else if (updateProduct.getStockCount() == 0) {
				dto = ProductInventoryDTO.builder().stockStatus("OUT_OF_STOCK")
						.stockCount(updateProduct.getStockCount()).productId(updatedProduct.getId()).build();

			} else {
				dto = ProductInventoryDTO.builder().stockStatus("AVAILABLE_SOON")
						.stockCount(updateProduct.getStockCount()).productId(updatedProduct.getId()).build();
			}
			return client.updateInventory(dto).flatMap(inventory -> {
				log.info("Inventory of product with id " + id + " updated.");
				return Mono.just(updatedProduct);
			});
		}).switchIfEmpty(Mono.empty());

	}

}
